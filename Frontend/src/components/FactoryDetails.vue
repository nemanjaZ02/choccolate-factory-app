<template>
  <div class="container">
    <div v-if="!factory">
      <h1>FACTORY WITH THIS ID DOES NOT EXIST!</h1>
    </div>
    <div v-else-if="dataLoaded && loggedInUser.role == 'MANAGER' && loggedInUser.factoryId == factory.id">
      <div class="manager-info">
        <label>YOU ARE MANAGER OF THIS FACTORY</label>
        <table class="factory-table">
          <thead>
            <tr>
              <th colspan="2" class="factory-name">{{ factory.name }}</th>
            </tr>
            <tr>
              <th colspan="2">
                <img :src="factory.logo" alt="Logo" class="factory-logo">
              </th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td>Working Status:</td>
              <td>{{ factory.status }}</td>
            </tr>
            <tr>
              <td>City:</td>
              <td>{{ factory.location.adress.city }}</td>
            </tr>
            <tr>
              <td>Address:</td>
              <td>{{ factory.location.adress.street + " " + factory.location.adress.streetNum + ", " + factory.location.adress.city }}</td>
            </tr>
            <tr>
              <td>Average rate:</td>
              <td>{{ factory.rating }}</td>
            </tr>
            <tr v-for="c in factory.chocolates" :key="c.name">
              <td>
                <img :src="c.image" alt="Chocolate" class="chocolate-image">
              </td>
              <td>{{ c.name }}</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
    <div v-else-if="dataLoaded">
      <table class="factory-table">
        <thead>
          <tr>
            <th colspan="2" class="factory-name">{{ factory.name }}</th>
          </tr>
          <tr>
            <th colspan="2">
              <img :src="factory.logo" alt="Logo" class="factory-logo">
            </th>
          </tr>
        </thead>
        <tbody>
          <tr>
            <td>Working Status:</td>
            <td>{{ factory.status }}</td>
          </tr>
          <tr>
            <td>City:</td>
            <td>{{ factory.location.adress.city }}</td>
          </tr>
          <tr>
            <td>Address:</td>
            <td>{{ factory.location.adress.street + " " + factory.location.adress.streetNum + ", " + factory.location.adress.city }}</td>
          </tr>
          <tr>
            <td>Average rate:</td>
            <td>{{ factory.rating }}</td>
          </tr>
          <tr v-for="c in factory.chocolates" :key="c.name">
            <td>
              <img :src="c.image" alt="Chocolate" class="chocolate-image">
            </td>
            <td>{{ c.name }}</td>
          </tr>
        </tbody>
      </table>
    </div>
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

<style scoped>
.container {
  max-width: 960px;
  margin: 0 auto;
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  background-color: #f7f7f7;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  border-radius: 8px;
  margin-top: 100px; 
}

h1 {
  color: #ff4b5c;
  text-align: center;
}

.manager-info label {
  display: block;
  font-size: 1.2rem;
  color: #ff4b5c;
  margin-bottom: 10px;
  text-align: center;
}

.factory-table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 20px;
}

.factory-table th,
.factory-table td {
  padding: 12px;
  text-align: left;
  border-bottom: 1px solid #ddd;
}

.factory-name {
  font-size: 36px;
  text-align: center;
  color: #333;
}

.factory-logo {
  display: block;
  margin: 0 auto;
  width: 250px;
  height: 250px;
  object-fit: contain;
}

.chocolate-image {
  width: 150px;
  height: 150px;
  object-fit: contain;
}

tbody tr:nth-child(even) {
  background-color: #f9f9f9;
}

tbody tr:hover {
  background-color: #f1f1f1;
}
</style>
