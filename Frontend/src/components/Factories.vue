<template>
    <head>
        <link rel="stylesheet" href="https://unpkg.com/leaflet@1.7.1/dist/leaflet.css" />
        <link rel="stylesheet" href="https://unpkg.com/leaflet-geosearch/dist/geosearch.css" /> 
    </head>
    <nav class="navbar navbar-light" style="background-color: transparent; margin-top: 100px;">
        <div class = "container" style="height: 120px; width: 1000px;">
            <form class="form-inline" @submit.prevent="search()">
                <div>
                    <label style="font-size: 1.5em; padding: 10px;">Factory Name</label>
                    <input class="form-control mr-sm-2" v-model="factoryNameFilter" type="search" placeholder="Search" aria-label="Search" pattern=".*\S.*"> 
                </div>
                
                <div>
                    <label style="font-size: 1.5em; padding: 10px;">Chocolate Name</label>
                    <input class="form-control mr-sm-2" v-model="chocolateNameFilter" type="search" placeholder="Search" aria-label="Search" pattern=".*\S.*">
                </div>

                <div>
                    <label style="font-size: 1.5em; padding: 10px;">Location</label>
                    <input v-if="mapShown == false" type="button" class="form-control mr-sm-2" @click="showMap()" value="Open Map"></input>
                    <input v-else type="button" class="form-control mr-sm-2" @click="showMap()" value="Close Map"></input>
                </div>

                <div>
                    <label style="font-size: 1.5em; padding: 10px;">Average Rating</label>
                    <input class="form-control mr-sm-2" type="number" step="any" v-model="averageRatingFilter" placeholder="Search" aria-label="Search" pattern=".*\S.*" min="0" max="5">
                </div>
                <div>
                    <label style="color: transparent; font-size: 1.5em; padding: 10px;">a</label>
                    <button id="searchButton" class="btn btn-primary my-2 my-sm-0"  type="submit">Search</button>
                </div>

                <div :style="{ width: '100%', height: '50vh', zIndex: 1, visibility: mapShown ? 'visible' : 'hidden' }" ref="mapContainer">
                </div>
            </form>        
        </div>
    </nav>
    <div>
        <div class="row">
            <div class="col-md-2">
                <div class="container">
                    <div style="text-align: center"> 
                        <label style="font-size: 1.5em; padding: 10px;font-weight: bold; text-align: ">FILTERS</label>
                    </div>                 
                    <div>
                        <label>Show Only Open</label>
                    </div>
                    <div>
                        <div>
                            <input type="checkbox" group="onlyOpen" v-on:change="changeShowOnlyOpen()">Yes</input>
                        </div>
                    </div> 
                    <div>
                        <label>Chocolate Type</label>
                    </div>
                    <div>
                        <div>
                            <input type="checkbox" group="chocolateType" v-on:change="addChocolateTypeFilter('Dark')">Dark</input>
                        </div>
                        
                        <div>
                            <input type="checkbox" group="chocolateType" v-on:change="addChocolateTypeFilter('Milk')">Milk</input>
                        </div>
                        
                        <div>
                            <input type="checkbox" group="chocolateType" v-on:change="addChocolateTypeFilter('Baking')">Baking</input>
                        </div>
                        
                        <div>
                            <input type="checkbox" group="chocolateType" v-on:change="addChocolateTypeFilter('White')">White</input>
                        </div>
                        
                        <div>
                            <input type="checkbox" group="chocolateType" v-on:change="addChocolateTypeFilter('Bittersweet')">Bittersweet</input>
                        </div>
                    </div> 
                    <div>
                        <label>Chocolate Kind</label>
                    </div>
                    <div>
                        <div>
                            <input type="checkbox" group="chocolateKind" v-on:change="addChocolateKindFilter('Plain')">Plain</input>
                        </div>

                        <div>
                            <input type="checkbox" group="chocolateKind" v-on:change="addChocolateKindFilter('Nuts')">With Nuts</input>
                        </div>
                        
                        <div>
                            <input type="checkbox" group="chocolateKind" v-on:change="addChocolateKindFilter('Strawberry')">Strawberry</input>
                        </div>
                        
                        <div>
                            <input type="checkbox" group="chocolateKind" v-on:change="addChocolateKindFilter('Filling')">With Filling</input>
                        </div>
                        
                        <div>
                            <input type="checkbox" group="chocolateKind" v-on:change="addChocolateKindFilter('Caramel')">Caramel</input>
                        </div>
                        
                        <div>
                            <input type="checkbox" group="chocolateKind" v-on:change="addChocolateKindFilter('Cookies')">Cookie Crumbs</input>
                        </div>

                        <div>
                            <input type="checkbox" group="chocolateKind" v-on:change="addChocolateKindFilter('Rice')">Rice</input>
                        </div>
                    </div>
                </div>
            </div>
            <div class = "col-md-10">
                <div class="container">
                    <h1 style="text-align: center;">FACTORIES</h1> 
                    <select class="form-select form-select-lg mb-3" aria-label=".form-select-lg example" v-on:change="sortFactories()" v-model="sortOption"> 
                        <option disabled selected>Sort By</option>
                        <option value="name_asc">Name Ascending</option>
                        <option value="name_desc">Name Descending</option>
                        <option value="location_asc">Location Ascending</option>
                        <option value="location_desc">Location Descending</option>
                        <option value="rating_asc">Rating Ascending</option>
                        <option value="rating_desc">Rating Descending</option>
                    </select>
                    <button id="addFactoryButton" class="btn btn-primary my-2 my-sm-0" v-if="loggedInUser.role == 'ADMIN'"  v-on:click="showAddForm()" style="margin-left: 50px ;" value="add factory">Add Factory</button>
                    <div class="container">
                        <div class="row">
                            <div class="col-lg-4 col-md-6 mb-4" v-for="f in filteredFactories" @click="showDetails(f)" :key="f.id" >
                                <div v-if="f.isDeleted == false" class="factory">
                                    <div class="card">
                                        <img :src="f.logo" class="card-img-top" :alt="f.name">
                                        <div class="card-body">
                                            <h5 class="card-title">{{ f.name }}</h5>
                                            <p v-if="f.status === 'OPEN'" class="text-success">OPEN 🟢</p>
                                            <p v-else class="text-danger">CLOSED 🔴</p>
                                        </div>
                                        <ul class="list-group list-group-flush">
                                            <li class="list-group-item">{{ "Open: " + f.workTime.from + " - " + f.workTime.to}}</li>
                                            <li class="list-group-item">{{ f.location.adress.city + ", " + f.location.adress.country}}</li>
                                            <li class="list-group-item">{{ "Rating: " + f.rating.toFixed(2) }}</li>
                                            <li v-if="loggedInUser.role == 'ADMIN'" class="list-group-item">
                                                <button class="btn-danger" @click.stop="deleteFactory(f)">DELETE</button>
                                            </li>
                                        </ul>
                                    </div>
                                </div>
                            </div>
                        </div>    
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import axios from 'axios';
import { onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';
import L from 'leaflet'
import { GeoSearchControl, OpenStreetMapProvider } from 'leaflet-geosearch';
import 'leaflet/dist/leaflet.css'
import 'leaflet-defaulticon-compatibility/dist/leaflet-defaulticon-compatibility.css'
import "leaflet-defaulticon-compatibility";
import { load } from 'ol/Image';

const router = useRouter();
const factories = ref([]);
const filteredFactories = ref([]);

const factoryNameFilter = ref("");
const chocolateNameFilter = ref("");
const averageRatingFilter = ref(0);

const mapShown = ref(false)

const sortOption = ref("Sort By");

const chocolateTypeFilters = ref([]);
const chocolateKindFilters = ref([]);
const showOnlyOpen = ref(false);

const locationFilters = ref([]);

const map = ref(null)
const mapContainer = ref()
const loggedInUser = ref({role:""});

onMounted(() => {
    loadFactories();
    loggedInUser.value = JSON.parse(localStorage.getItem('loggedUser'));

    if(JSON.parse(localStorage.getItem('loggedUser')))
    {
        loggedInUser.value= JSON.parse(localStorage.getItem('loggedUser'));  
    }
    else {
        
        loggedInUser.value = { role: "" };
    }
})

function createMap() {
    map.value = L.map(mapContainer.value).setView([51.505, -0.09], 13);
    L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
    }).addTo(map.value);

    const provider = new OpenStreetMapProvider();
    const searchControl = new GeoSearchControl({
        provider: provider,
        style: 'bar',
        showMarker: true,
        retainZoomLevel: false,
        animateZoom: true,
        autoClose: true,
        searchLabel: 'Enter address'
    });

    map.value.addControl(searchControl);

    const sliderContainer = document.createElement('div');
    sliderContainer.style.position = 'absolute';
    sliderContainer.style.top = '10px'; 
    sliderContainer.style.right = '10px'; 
    sliderContainer.style.zIndex = '1000'; 
    sliderContainer.style.background = 'white'; 
    sliderContainer.style.padding = '10px'; 
    sliderContainer.style.borderRadius = '5px'; 
    sliderContainer.style.boxShadow = '0 2px 4px rgba(0, 0, 0, 0.1)'; 

    const slider = document.createElement('input');
    slider.type = 'range';
    slider.min = '1';
    slider.max = '50';
    slider.value = '10'; 
    slider.step = '1';
    slider.style.width = '180px'; 

    const sliderLabel = document.createElement('label');
    sliderLabel.innerHTML = 'Circle Radius (km): ';

    const radiusValue = document.createElement('span');
    radiusValue.style.marginLeft = '10px'; 
    sliderContainer.appendChild(sliderLabel);
    sliderContainer.appendChild(slider);
    sliderContainer.appendChild(radiusValue);

    mapContainer.value.appendChild(sliderContainer);

    let currentCircle = null;

    function updateCircleRadius(radiusInKm) {
        const circleRadius = radiusInKm * 1000;
        if (currentCircle) {
            currentCircle.setRadius(circleRadius);
        }
        radiusValue.textContent = radiusInKm + ' km';
        search();
        return circleRadius;
    }

    slider.addEventListener('input', function(e) {
        e.stopPropagation(); 

        const radiusInKm = parseInt(this.value);
        const circleRadius = updateCircleRadius(radiusInKm);

        const clickLocation = currentCircle ? currentCircle.getLatLng() : map.value.getCenter();
        const nearbyFactories = factories.value.filter(factory => {
            const factoryLocation = L.latLng(factory.location.longitude, factory.location.latitude);
            return clickLocation.distanceTo(factoryLocation) <= circleRadius;
        });

        console.log("Nearby factories within " + (circleRadius / 1000) + " km:", nearbyFactories);

        locationFilters.value = nearbyFactories.map(factory => factory.name);

        console.log("Location filters:", locationFilters.value);

        filterByLocation();
    });

    map.value.on('click', function (e) {
        const clickLocation = e.latlng;

        if (currentCircle) {
            map.value.removeLayer(currentCircle);
        }

        const circleRadius = updateCircleRadius(parseInt(slider.value));

        currentCircle = L.circle(clickLocation, { radius: circleRadius }).addTo(map.value);

        currentCircle.on('click', function (e) {
            map.value.removeLayer(currentCircle);
            currentCircle = null;
            document.getElementById('resultList').innerHTML = '';
            L.DomEvent.stopPropagation(e); 
        });

        const nearbyFactories = factories.value.filter(factory => {
            const factoryLocation = L.latLng(factory.location.longitude, factory.location.latitude);
            return clickLocation.distanceTo(factoryLocation) <= circleRadius;
        });

        console.log("Nearby factories within " + (circleRadius / 1000) + " km:", nearbyFactories);

        locationFilters.value = nearbyFactories.map(factory => factory.name);

        console.log("Location filters:", locationFilters.value);

        search();

        currentCircle.on('mousedown', L.DomEvent.stopPropagation);
    });

    for (let factory of factories.value) {
        if (factory.isDeleted === false) {
            L.marker([factory.location.longitude, factory.location.latitude]).addTo(map.value)
                .bindPopup(factory.name)
                .openPopup().on('click', () => {
                    showDetails(factory); 
                }).on('mouseover', function () {
                    this.openPopup();
                });
        }  
    }

    L.DomEvent.disableClickPropagation(sliderContainer);
}



function loadFactories() {
    axios.get('http://localhost:8080/ChoccolateAppREST/rest/ChocolateFactoryService/getAll').then(response => {
            factories.value = response.data   
            filteredFactories.value = factories.value.filter(factory => factory.isDeleted == false);
            filteredFactories.value.sort((a, b) => b.status.localeCompare(a.status));
            if(map.value == null)
                createMap();
            checkFactoriesWorkingStatus();
            setInterval(checkFactoriesWorkingStatus, 60000);
        });
} 

function checkFactoriesWorkingStatus() {
    filteredFactories.value.forEach(factory => {
        let currentTime = getCurrentTimeFormatted();
        let currentTimeParsed = parseTimeString(currentTime);

        let factoryTimeFrom = factory.workTime.from;
        let factoryTimeFromParsed = parseTimeString(factoryTimeFrom);
        let factoryTimeTo = factory.workTime.to;
        let factoryTimeToParsed = parseTimeString(factoryTimeTo);

        if(factoryTimeFromParsed <= currentTimeParsed && factoryTimeToParsed >= currentTimeParsed)
        {       
            factory.status = "OPEN";
        }
        else
        {
            factory.status = "CLOSED";
        } 
        
        axios.post('http://localhost:8080/ChoccolateAppREST/rest/ChocolateFactoryService/updateChocolateFactoryStatus', factory).then(response => {
        });
    });
}

function getCurrentTimeFormatted() {
    let now = new Date();
    let hours = now.getHours();
    let minutes = now.getMinutes();
    let seconds = now.getSeconds();
    let ampm = hours >= 12 ? 'PM' : 'AM';
    hours = hours % 12;
    hours = hours ? hours : 12;
    minutes = minutes < 10 ? '0' + minutes : minutes;
    seconds = seconds < 10 ? '0' + seconds : seconds;
    let strTime = hours + ':' + minutes + ':' + seconds + ' ' + ampm;
    return strTime;
}

function parseTimeString(timeString) {
    let [time, modifier] = timeString.split(' ');
    let [hours, minutes, seconds] = time.split(':');
    if (hours === '12') {
        hours = '00';
    }
    if (modifier === 'PM') {
        hours = parseInt(hours, 10) + 12;
    }
    let date = new Date();
    date.setHours(parseInt(hours, 10));
    date.setMinutes(parseInt(minutes, 10));
    date.setSeconds(parseInt(seconds, 10));
    return date;
}

function search() {
    filteredFactories.value = factories.value.filter(factory => factory.name.toLowerCase().includes(factoryNameFilter.value.toLowerCase())
     &&  (chocolateNameFilter.value === "" || factory.chocolates.some(chocolate => !chocolate.isDeleted && ((((loggedInUser.value.role == "CUSTOMER" || loggedInUser.value.role == "" || (loggedInUser.value.role == "MANAGER" && loggedInUser.value.factoryId != factory.id) || (loggedInUser.value.role == "EMPLOYEE" && loggedInUser.value.factoryId != factory.id)) && chocolate.quantity > 0 && chocolate.name.toLowerCase().includes(chocolateNameFilter.value.toLowerCase()))
      || (!(loggedInUser.value.role == "CUSTOMER" || loggedInUser.value.role == "" || (loggedInUser.value.role == "MANAGER" && loggedInUser.value.factoryId != factory.id) || (loggedInUser.value.role == "EMPLOYEE" && loggedInUser.value.factoryId != factory.id)) && chocolate.name.toLowerCase().includes(chocolateNameFilter.value.toLowerCase()))))))
     &&  (averageRatingFilter.value == "" || averageRatingFilter.value == 0 || (factory.rating >= averageRatingFilter.value - 0.5 && factory.rating <= averageRatingFilter.value + 0.5)))

    if(showOnlyOpen.value === true) {
        filteredFactories.value = filteredFactories.value.filter(factory => factory.status === "OPEN")
    }

    if(chocolateTypeFilters.value.length > 0) {
        filterByChocolateType();
    }    

    if(chocolateKindFilters.value.length > 0) {
        filterByChocolateKind();
    }

    if(locationFilters.value.length > 0) {
        filterByLocation();
    }
}

function sortFactories() {
  if (sortOption.value === "name_asc") {
    filteredFactories.value.sort((a, b) => a.name.localeCompare(b.name));
  } else if (sortOption.value === "name_desc") {
    filteredFactories.value.sort((a, b) => b.name.localeCompare(a.name));
  } else if (sortOption.value === "location_asc") {
    filteredFactories.value.sort((a, b) => a.location.adress.city.localeCompare(b.location.adress.city));
  } else if (sortOption.value === "location_desc") {
    filteredFactories.value.sort((a, b) => b.location.adress.city.localeCompare(a.location.adress.city));
  } else if (sortOption.value === "rating_asc") {
    filteredFactories.value.sort((a, b) => a.rating - b.rating);
  } else if (sortOption.value === "rating_desc") {
    filteredFactories.value.sort((a, b) => b.rating - a.rating);
  }
}

function addChocolateTypeFilter(chocolateType) {
    if(chocolateTypeFilters.value.includes(chocolateType)) {
        chocolateTypeFilters.value = chocolateTypeFilters.value.filter(type => type !== chocolateType);
    }
    else
    {
        chocolateTypeFilters.value.push(chocolateType);
    }
    search();
}

function addChocolateKindFilter(chocolateKind) {
    if(chocolateKindFilters.value.includes(chocolateKind)) {
        chocolateKindFilters.value = chocolateKindFilters.value.filter(kind => kind !== chocolateKind);
    }
    else
    {
        chocolateKindFilters.value.push(chocolateKind);
    }
    search();
}

function changeShowOnlyOpen() {
    if(showOnlyOpen.value === false) {
        showOnlyOpen.value = true;
    }
    else {
        showOnlyOpen.value = false;
    }
    search();
}

function filterByChocolateType() {
    let uniqueFactories = new Set();
    chocolateTypeFilters.value.forEach(type => {
    filteredFactories.value.forEach(factory => {
            if (factory.chocolates.some(chocolate => !chocolate.isDeleted && (((loggedInUser.value.role == "CUSTOMER" || loggedInUser.value.role == "" 
            || (loggedInUser.value.role == "MANAGER" && loggedInUser.value.factoryId != factory.id) || (loggedInUser.value.role == "EMPLOYEE" && loggedInUser.value.factoryId != factory.id)) && chocolate.quantity > 0 && chocolate.type === type) || (!(loggedInUser.value.role == "CUSTOMER" || loggedInUser.value.role == "" 
            || (loggedInUser.value.role == "MANAGER" && loggedInUser.value.factoryId != factory.id) || (loggedInUser.value.role == "EMPLOYEE" && loggedInUser.value.factoryId != factory.id)) && chocolate.type == type)))) {
                uniqueFactories.add(factory);
            }
        });
    });
    filteredFactories.value = Array.from(uniqueFactories);  
}

function filterByChocolateKind() {
    let uniqueFactories = new Set();
    chocolateKindFilters.value.forEach(kind => {
    filteredFactories.value.forEach(factory => {
            if (factory.chocolates.some(chocolate => !chocolate.isDeleted && (((loggedInUser.value.role == "CUSTOMER" || loggedInUser.value.role == "" 
            || (loggedInUser.value.role == "MANAGER" && loggedInUser.value.factoryId != factory.id) || (loggedInUser.value.role == "EMPLOYEE" && loggedInUser.value.factoryId != factory.id)) && chocolate.quantity > 0 && chocolate.kind === kind) || (!(loggedInUser.value.role == "CUSTOMER" || loggedInUser.value.role == ""
            || (loggedInUser.value.role == "MANAGER" && loggedInUser.value.factoryId != factory.id) || (loggedInUser.value.role == "EMPLOYEE" && loggedInUser.value.factoryId != factory.id)) && chocolate.kind == kind)))) {
                uniqueFactories.add(factory);
            }
        });
    });
    filteredFactories.value = Array.from(uniqueFactories);  
}

function filterByLocation() {
    let uniqueFactories = new Set();
    locationFilters.value.forEach(name => {
    filteredFactories.value.forEach(factory => {
            if(factory.name == name && !factory.isDeleted) {
                uniqueFactories.add(factory);
            }
        });
    });
    filteredFactories.value = Array.from(uniqueFactories); 
}

function showDetails(factory) {
    router.push({name: 'factoryDetails', params: { factoryId: factory.id }});
}

function showMap() {
    if(!mapShown.value)
    {
        mapShown.value = true;
    }
    else
    {
        mapShown.value = false;
    }
}
function showAddForm() {
  router.push('/addChocolateFactoryForm');
}

function deleteFactory(factory) {
    axios.post('http://localhost:8080/ChoccolateAppREST/rest/ChocolateFactoryService/deleteChocolateFactory', factory, { 
         headers: {
            'Authorization': `Bearer ${localStorage.getItem('jsonWebToken')}`
        }
        }).then(response => {
            response.data.chocolates.forEach(choco => {
                axios.post('http://localhost:8080/ChoccolateAppREST/rest/chocolates/deleteChocolate', choco, {
                    headers: {
                    'Authorization': `Bearer ${localStorage.getItem('jsonWebToken')}`
                    }
                })
                .then(response => {
                    
                })
                .catch(error => {
                    console.error(error);
                });
            });
        loadFactories();  
    });  
}
</script>

<style scoped>
.container {
    font-family: Arial, sans-serif;
    background-color: #dfd1c2;
    border-radius: 8px;
    margin-top: 50px;
}

.header {
    text-align: center;
    margin-bottom: 40px;
}

.header h1 {
    font-size: 2.5em;
    color: #333;
    margin-bottom: 20px;
}

.form-group {
    flex: 0 0 calc(50% - 10px);
    margin-bottom: 20px;
}

.form-group label {
    display: block;
    margin-bottom: 5px;
}

.form-group input {
    width: 100%;
    padding: 10px;
    border: 1px solid #ccc;
    border-radius: 4px;
}

.factory:hover .card {
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
    transform: translateY(-5px); 
    cursor: pointer;
}

.card {
    transition: transform 0.3s, box-shadow 0.3s;
}

#searchButton {
    background: rgba(123,63,0,1);
    
}

#searchButton:hover {
    background: rgb(83, 43, 0);
}
#addFactoryButton {
    background: rgba(123,63,0,1);
    
}

#addFactoryButton:hover {
    background: rgb(83, 43, 0);
}
</style>
