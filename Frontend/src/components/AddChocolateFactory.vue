<template>
    <head>
        <link rel="stylesheet" href="https://unpkg.com/leaflet@1.7.1/dist/leaflet.css" />
        <link rel="stylesheet" href="https://unpkg.com/leaflet-geosearch/dist/geosearch.css" /> 
    </head>
    <h1 style="margin-top: 100px">Add Chocolate Factory</h1>
    <div :style="{ width: '1100px', height: '300px'}" ref="mapContainer"></div>
    <form @submit.prevent="addChocolateFactory()">
    <table>
      <tr>
        <td>Name:</td>
        <td><input type="text" v-model="chocolateFactory.name"></td>
      </tr>
      <tr>
      <th><label style="font-weight: bold;">Work time</label></th>
      </tr>
      <tr>
       <th> <label for="from" >From:</label></th>
      <th><input type="time"  v-model="chocolateFactory.workTime.from" ></th>
      </tr>
      <tr>
        <th><label for="to">To:</label></th>
        <th><input type="time" v-model="chocolateFactory.workTime.to"></th>
      </tr>
      <tr>
        <th>Logo:</th>
        <th><input type="file" @change="handleFileUpload"></th>
      </tr>
      <tr>
        <th v-if="availableManagers.length > 0"> <label style="font-weight: bold;">Choose a Manager:</label></th>
      </tr>
     
        <th v-if="availableManagers.length > 0">
             <select v-model="manager"  >
                <option v-for="manager in availableManagers" :value="manager" >
                  {{ manager.username }}
                </option>
              </select>
        </th>
        <tr v-if="availableManagers.length == 0">
        <label style="font-weight: bold;"> Create a manager</label>
      </tr> 
      <tr  v-if="availableManagers.length == 0" >
        <td>
          <label>Username: </label>
        </td>
        <td>
          <input type="text" v-model="manager.username" >
        </td> 
      </tr>
      <tr  v-if="availableManagers.length == 0">
        <td>
          <label>Password: </label>
        </td>
        <td>
            <input type="password" v-model="manager.password" >
        </td>
      </tr>
      <tr v-if="availableManagers.length == 0">
        <td>
            <label>Re-Type Password: </label> 
        </td>
        <td>
            <input type="password" v-model="confirmedPassword" >
        </td> 
        <td>
            <label v-bind:hidden="confirmedPassword == manager.password">Password are not matching!</label>
        </td> 
      </tr>
      <tr v-if="availableManagers.length == 0">
          <td>
            <label>Name: </label>
         </td>
         <td>
            <input type="text" v-model="manager.name" >
         </td> 
      </tr>
      <tr  v-if="availableManagers.length == 0">
          <td>
            <label>Surname: </label>
         </td>
         <td>
            <input type="text" v-model="manager.surname" >
         </td> 
      </tr>
      <tr  v-if="availableManagers.length == 0">
        <td>
           <label>Gender: </label>
        </td>
        <td>
          <select v-model="manager.gender" >
            <option>MALE</option>
            <option>FEMALE</option>
          </select>
        </td> 
      </tr>
      <tr v-if="availableManagers.length == 0">
        <td>
          <label>Birthday: </label>
        </td>
        <td>
          <input type="date" v-model="manager.birthday" >
        </td> 
      </tr>
      <tr > 
        <th><input type="submit" value="Add factory"></th>
      </tr>
      <tr>
        <th>  
          <label style="color: red">{{ errorMessage }}</label>
        </th>
      </tr>
    </table>


   
    </form>
    </template>

    <script setup>
    import { GeoSearchControl, OpenStreetMapProvider } from 'leaflet-geosearch';
    import 'leaflet/dist/leaflet.css'
    import 'leaflet-defaulticon-compatibility/dist/leaflet-defaulticon-compatibility.css'
    import "leaflet-defaulticon-compatibility";
    import axios from 'axios';
    import {watch,onMounted, ref} from 'vue';
    import {useRouter} from 'vue-router';
    import {useRoute} from 'vue-router';
    const confirmedPassword = ref("");
    const selectedFile = ref(null);
    const router = useRouter();
    const route = useRoute();
    const map = ref();
    const mapContainer = ref();
    const availableManagers = ref([])
    const errorMessage = ref(" ");
    const manager = ref({factoryId:0, id:0, username:"", password:"", name:"", surname:"", gender:"", birthday:"", role:"MANAGER"})
    const chocolateFactory = ref({id:0,  name:"", chocolates:[],  workTime: {
    from: "",
    to: ""
  },  status:"CLOSED", location:{
    longitude: "",
    latitude:  "",
    adress:{
      street:"",
      streetNum:"",
      city:"",
      country:"",
      postNum:0
    }
  }, logo:"", rating:""});
  
 
  
  onMounted(()=>{
    createMap();
    getAvailableManagers();
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
    let currentMarker = null;

    map.value.on('click', function (e) {
        if (currentMarker) {
            map.value.removeLayer(currentMarker);
        }
        currentMarker = L.marker(e.latlng).addTo(map.value);

        const { lat, lng } = e.latlng;

        fetch(`https://nominatim.openstreetmap.org/reverse?format=json&lat=${lat}&lon=${lng}&addressdetails=1`)
            .then(response => response.json())
            .then(data => {
                const { address } = data;
                const country = address.country || '';
                const city = address.city || address.town || address.village || '';
                const postalCode = address.postcode || '';
                const street = address.road || '';
                const houseNumber = address.house_number || '';

                chocolateFactory.value.location.longitude = lat
                chocolateFactory.value.location.latitude = lng
                chocolateFactory.value.location.adress.city = city
                chocolateFactory.value.location.adress.country = country
                chocolateFactory.value.location.adress.street = street
                chocolateFactory.value.location.adress.postNum = postalCode
                chocolateFactory.value.location.adress.streetNum = houseNumber

                currentMarker.bindPopup(`Country: ${country}<br>City: ${city}<br>Postal Code: ${postalCode}<br>Latitude: ${lat}<br>Longitude: ${lng}<br>Street: ${street}<br>Number: ${houseNumber}`).openPopup();
            })
            .catch(error => {
                console.error('Error fetching location details:', error);
            });
    });
}
  function addChocolateFactory()
  { 
    errorMessage.value = " ";
      if (
      manager.value.username === "" ||
      manager.value.name === "" ||
      manager.value.surname === ""||
      manager.value.password ===""||
      manager.value.gender ==""||
      manager.value.birthday==""||
      chocolateFactory.value.name ==""||
      chocolateFactory.value.workTime.from ==""||
      chocolateFactory.value.workTime.to == ""
      
      

     ) {
       errorMessage.value = "Please fill in all required fields.";
       
     }
    if(errorMessage.value===" ")
    {

      chocolateFactory.value.workTime.from = getTimeFormatted(chocolateFactory.value.workTime.from)
      chocolateFactory.value.workTime.to = getTimeFormatted(chocolateFactory.value.workTime.to)
      
      axios.post("http://localhost:8080/ChoccolateAppREST/rest/registerManager",this.manager, {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('jsonWebToken')}`
      }
      
    }).then(response => {
        manager.value = response.data;
        axios.post('http://localhost:8080/ChoccolateAppREST/rest/ChocolateFactoryService/addChocolateFactory', this.chocolateFactory, {
          headers: {
        'Authorization': `Bearer ${localStorage.getItem('jsonWebToken')}`,
        'Content-Type': 'application/json' 
      }
          
        }).then(response=>{
     
      manager.value.factoryId =  response.data.id
      axios.put('http://localhost:8080/ChoccolateAppREST/rest/ChocolateFactoryService/updateManager', this.manager, {
          
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${localStorage.getItem('jsonWebToken')}`
          }
        })
       .then(respone=>{
          router.push("/")
        })
      })
      });
    }
  }
        
  
    
  function handleFileUpload(event){
        selectedFile.value = event.target.files[0];
        const formData = new FormData();
        formData.append('file', selectedFile.value);
        axios.post('http://localhost:8080/ChoccolateAppREST/rest/image/upload', formData, {
          headers: {
             'Content-Type': 'multipart/form-data'
          }
        }).then( response => {
              if (response.status === 200) {
                chocolateFactory.value.logo = response.data;
              }
          })
  }
  
  function getTimeFormatted(time) {
    let [hours, minutes] = time.split(':')
    let seconds = 0
    let ampm = hours >= 12 ? 'PM' : 'AM';
    hours = hours % 12;
    hours = hours ? hours : 12;
    minutes = minutes < 10 ? '0' + minutes : minutes;
    seconds = seconds < 10 ? '0' + seconds : seconds;
    let strTime = hours + ':' + minutes + ':' + seconds + ' ' + ampm;
    return strTime;
}

  function getAvailableManagers(){
      axios.get('http://localhost:8080/ChoccolateAppREST/rest/ChocolateFactoryService/getAvailableManagers').then(response=>{
        availableManagers.value = response.data;
    })
    }
    
    </script>

