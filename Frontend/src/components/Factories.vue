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
                    <input class="form-control mr-sm-2" type="number" step="any" v-model="averageRatingFilter" placeholder="Search" aria-label="Search" pattern=".*\S.*">
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
                    <button v-if="loggedInUser.role == 'ADMIN'"  v-on:click="showAddForm()" style="margin-left: 50px ;" value="add factory">Add Factory</button>
                    <div class="container">
                        <div class="row">
                            <div class="col-lg-4 col-md-6 mb-4" v-for="f in filteredFactories" :key="f.id" @click="showDetails(f)">
                                <div class="factory">
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
                                            <li class="list-group-item">{{ "Rating: " + f.rating }}</li>
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

const map = ref()
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

function createMap()
{
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

    for(let factory of factories.value)
    {
        L.marker([factory.location.longitude, factory.location.latitude]).addTo(map.value)
        .bindPopup(factory.name)
        .openPopup().on('click', () => {
            showDetails(factory); 
        }).on('mouseover', function () {
            this.openPopup();
        });
    }   
}

function loadFactories() {
    axios.get('http://localhost:8080/ChoccolateAppREST/rest/ChocolateFactoryService/getAll').then(response => {
            factories.value = response.data   
            filteredFactories.value = factories.value
            createMap();
        });
} 

function search() {
    filteredFactories.value = factories.value.filter(factory => factory.name.toLowerCase().includes(factoryNameFilter.value.toLowerCase())
     &&  (chocolateNameFilter.value === "" || factory.chocolates.some(chocolate => chocolate.name.toLowerCase().includes(chocolateNameFilter.value.toLowerCase())))
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
            if (factory.chocolates.some(chocolate => chocolate.type === type)) {
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
            if (factory.chocolates.some(chocolate => chocolate.kind === kind)) {
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
</style>
