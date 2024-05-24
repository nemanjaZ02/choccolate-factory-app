<template>
  <div v-if="!factory">
    <h1>FACTORY WITH THIS ID DOES NOT EXIST!</h1>
  </div>
  <div v-else-if="dataLoaded && loggedInUser.role == 'MANAGER' && loggedInUser.factoryId == factory.id">
      <label>YOU ARE MANAGER OF THIS FACTORYYYYY</label>
      <table>
        <thead>
          <th style="font-size: 52px">{{ factory.name }}</th>
          <th>
            <img :src="factory.logo" alt="Logo" width="250" height="250">
          </th>
        </thead>
        <tr>
          <td>Working Status: </td>
          <td>{{ factory.status }}</td>
        </tr>
        <tr>
          <td>City: </td>
          <td>{{ factory.location.adress.city }} </td>
        </tr>
        <tr>
          <td>Address: </td>
          <td>{{ factory.location.adress.street + " " + factory.location.adress.streetNum + ", " + factory.location.adress.city }} </td>
        </tr>
        <tr>
          <td>Average rate:</td>
          <td>{{ factory.rating }}</td>
        </tr>
        <tr v-for="c in factory.chocolates"> 
          <td>
            <img :src="c.image" alt="Chocolate" width="150" height="150">
          </td>
          <td>{{ c.name }}</td>
        </tr>
      </table>
  </div>
  <div v-else-if="dataLoaded">
      <table>
        <thead>
          <th style="font-size: 52px">{{ factory.name }}</th>
          <th>
            <img :src="factory.logo" alt="Logo" width="250" height="250">
          </th>
        </thead>
        <tr>
          <td>Working Status: </td>
          <td>{{ factory.status }}</td>
        </tr>
        <tr>
          <td>City: </td>
          <td>{{ factory.location.adress.city }} </td>
        </tr>
        <tr>
          <td>Address: </td>
          <td>{{ factory.location.adress.street + " " + factory.location.adress.streetNum + ", " + factory.location.adress.city }} </td>
        </tr>
        <tr>
          <td>Average rate:</td>
          <td>{{ factory.rating }}</td>
        </tr>
        <tr v-for="c in factory.chocolates"> 
          <td>
            <img :src="c.image" alt="Chocolate" width="150" height="150">
          </td>

          <td>{{ c.name }}</td>
        </tr>
      </table>
  </div>
</template>

<script setup>
import axios from 'axios';
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';

const route = useRoute();
const factory = ref({});
const dataLoaded = ref(false);

const loggedInUser = ref({ id: "", username: "", password: "", name: "", surname: "", gender: "", birthday: "", role: ""});
const isLoggedIn = ref(false);

onMounted(() => {
  axios.get(`http://localhost:8080/ChoccolateAppREST/rest/ChocolateFactoryService/getById/${route.params.factoryId}`).then(response => {
    if(!response.data) {
      factory.value = null;
      return;
    }
    factory.value = response.data;

    axios.get("http://localhost:8080/ChoccolateAppREST/rest/getLoggedInUser").then(response=>{
            loggedInUser.value=response.data;  
            dataLoaded.value = true;
        })
  });
});

</script>