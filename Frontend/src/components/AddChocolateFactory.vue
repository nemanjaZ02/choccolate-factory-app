<template>
    <h1>Add Chocolate</h1>
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
    import axios from 'axios';
    import {watch,onMounted, ref} from 'vue';
    import {useRouter} from 'vue-router';
    import {useRoute} from 'vue-router';
    const confirmedPassword = ref("");
    const selectedFile = ref(null);
    const router = useRouter();
    const route = useRoute();
    const availableManagers = ref([])
    const errorMessage = ref(" ");
   const props = defineProps({
    coordinates: {
    type: Object, 
    required: true
  },
    adress: {
    type: Object, 
    required: true
  }  
  });
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
    getAvailableManagers();
})
 
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

    
      chocolateFactory.value.location.longitude = props.coordinates[1]
      chocolateFactory.value.location.latitude = props.coordinates[0]
      chocolateFactory.value.location.adress.city=props.adress.city
      chocolateFactory.value.location.adress.country=props.adress.country
      chocolateFactory.value.location.adress.street=props.adress.street
      chocolateFactory.value.location.adress.postNum=props.adress.postcode
      chocolateFactory.value.location.adress.streetNum=props.adress.housenumber
      
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
    
  function getAvailableManagers(){
      axios.get('http://localhost:8080/ChoccolateAppREST/rest/ChocolateFactoryService/getAvailableManagers').then(response=>{
        availableManagers.value = response.data;
    })
    }
    
    </script>

