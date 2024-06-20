<template>
    <h1>Add Chocolate</h1>
    <form @submit.prevent="addChocolateFactory()">
    <table>
      <tr>
        <th>Name:</th>
        <th><input type="text" v-model="chocolateFactory.name"></th>
      </tr>
      <tr>
      <th>Work time:</th>
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
        <th v-if="availableManagers.length > 0">Choose a Manager:</th>
        <tr v-else>
          Create a manager
        </tr>
        <th v-if="availableManagers.length > 0">
             <select v-model="manager"  >
                <option v-for="manager in availableManagers" :value="manager" >
                  {{ manager.username }}
                </option>
              </select>
        </th>
       
        <tr v-else>
        <th >
          <form v-on:click="Register" v-on:submit="registerNewManager($event)">
        <table>
            <tr>
                <td>
                    <label>Username: </label>
                </td>
                <td>
                    <input type="text" v-model="manager.username" required>
                </td>     
            </tr>
            <tr>
                <td>
                    <label>Password: </label>
                </td>
                <td>
                    <input type="password" v-model="manager.password" required>
                </td> 
            </tr>
            <tr>
                <td>
                    <label>Re-Type Password: </label> 
                </td>
                <td>
                    <input type="password" v-model="confirmedPassword" required>
                </td> 
                <td>
                    <label v-bind:hidden="confirmedPassword == manager.password">Password are not matching!</label>
                </td> 
            </tr>
            <tr>
                <td>
                    <label>Name: </label>
                </td>
                <td>
                    <input type="text" v-model="manager.name" required>
                </td> 
            </tr>
            <tr>
                <td>
                    <label>Surname: </label>
                </td>
                <td>
                    <input type="text" v-model="manager.surname" required>
                </td> 
            </tr>
            <tr>
                <td>
                    <label>Gender: </label>
                </td>
                <td>
                    <select v-model="manager.gender" required>
                        <option>MALE</option>
                        <option>FEMALE</option>
                    </select>
                </td> 
            </tr>
            <tr>
                <td>
                    <label>Birthday: </label>
                </td>
                <td>
                    <input type="date" v-model="manager.birthday" required>
                </td> 
            </tr>
            <tr>
                <td>
                    <input type="submit" value="Register" v-bind:disabled="!(confirmedPassword == manager.password)" required>
                </td> 
            </tr>
        </table>
    </form>
       </th>
      </tr>
      </tr>
      <tr> 
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
    import {onMounted, ref} from 'vue';
    import {useRouter} from 'vue-router';
    import {useRoute} from 'vue-router';
    const confirmedPassword = ref("");
    const selectedFile = ref(null);
    const router = useRouter();
    const route = useRoute();
    const availableManagers = ref([])
    const manager = ref({factoryId:0, id:0, username:"", password:"", name:"", surname:"", gender:"", birthday:"", role:"MANAGER"})
    const chocolateFactory = ref({id:0,  name:"", chocolates:[],  workTime: {
    from: "",
    to: ""
  },  status:"CLOSED", location:{
    longitude:"",
    latitude:"",
    adress:{
      street:"",
      streetNum:"",
      city:"",
      postNum:0
    }
  }, logo:"", rating:""});
  const errorMessage = ref(" ");
  onMounted(()=>{
    getAvailableManagers();
})

    function addChocolateFactory()
    { 
       
        axios.post('http://localhost:8080/ChoccolateAppREST/rest/ChocolateFactoryService/addChocolateFactory', this.chocolateFactory, {
          headers: {
            'Authorization': `Bearer ${localStorage.getItem('jsonWebToken')}`
          }
          
        }).then(response=>{
      let responseData = response.data;
      manager.value.factoryId = responseData.id
      axios.put('http://localhost:8080/ChoccolateAppREST/rest/ChocolateFactoryService/updateManager', this.manager, {
          
          headers: {
            'Content-Type': 'application/json', // Explicitly set the Content-Type to application/json
            'Authorization': `Bearer ${localStorage.getItem('jsonWebToken')}`
          }
        })
       .then(respone=>{
          router.push("/")
        })
    });
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
      
    function updateManager() {
      manager.value.factoryId = this.chocolateFactory.id
      axios.put('http://localhost:8080/ChoccolateAppREST/rest/ChocolateFactoryService/updateManager', this.manager, {
          
          headers: {
            'Content-Type': 'application/json', // Explicitly set the Content-Type to application/json
            'Authorization': `Bearer ${localStorage.getItem('jsonWebToken')}`
          }
        }).catch(error => {
        this.errorMessage = error.response.data
    });
    }

    function registerNewManager(event) {
      event.preventDefault();
      axios.post("http://localhost:8080/ChoccolateAppREST/rest/registerManager", this.manager).then(response => {
        manager.value = response.data;
      }).catch(error => {
          console.log(error);
      });
    }
    </script>

