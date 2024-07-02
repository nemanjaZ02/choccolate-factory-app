<template>
    <div  class="card" style="width: 600px; margin-top: 200px">
  <div class="card-header">
  </div>
  <div class="card-body" >
    <h5 class="card-title">Add Employee</h5>
    <p class="card-text">Please enter employee details below</p>
    <form v-on:submit="registerNewUser($event)">
  <div class="mb-3">
    <label for="name" class="form-label">Name</label>
    <input type="text" class="form-control" v-model="newUser.name" required id="name">
  </div>
  <div class="mb-3">
    <label for="surname" class="form-label">Surname</label>
    <input type="text" class="form-control" v-model="newUser.surname" required id="surname">
  </div>
  <div class="mb-3">
    <label for="username" class="form-label">Username</label>
    <input type="text" class="form-control" v-model="newUser.username" required id="username">
  </div>
  <div class="mb-3">
    <label for="password" class="form-label">Password</label>
    <input type="password" class="form-control" v-model="newUser.password" required id="password">
  </div>
  <div class="mb-3">
    <label for="passwordRetype" class="form-label">Re-type password</label>
    <input type="password" class="form-control" v-model="confirmedPassword" required id="passwordRetype">
    <div id="emailHelp" v-bind:hidden="confirmedPassword == newUser.password" class="form-text">Password are not matching!</div>
  </div>
  <div class="mb-3">
    <label for="gender" class="form-label">Gender</label>
    <select class="form-control"  v-model="newUser.gender" required>
        <option>MALE</option>
        <option>FEMALE</option>
    </select>
    <div class="mb-3">
    <label for="date" class="form-label">Birthdate</label>
    <input type="date" class="form-control" v-model="newUser.birthday" required id="date">
  </div>
  </div>
  
  <button type="submit" class="btn btn-primary" v-bind:disabled="!(confirmedPassword == newUser.password)">Submit</button>
</form>
  </div>
</div>
   
</template>
<script setup>
import axios from 'axios';
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import {useRoute} from 'vue-router';

const router = useRouter();
const confirmedPassword = ref("");
const route = useRoute();
const newUser = ref({factoryId:route.params.factoryId,username:"",password:"",name:"",surname:"",gender:"",birthday:"",role:"EMPLOYEE"});

function registerNewUser(event) {
	event.preventDefault();
    axios.post("http://localhost:8080/ChoccolateAppREST/rest/registerEmployee", this.newUser, {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('jsonWebToken')}`
      }
      
    }).then(response => {
        router.push({name: 'factoryDetails', params: { factoryId: newUser.factoryId }})
    }).catch(error => {
        console.log(error);
    });
}
</script>
