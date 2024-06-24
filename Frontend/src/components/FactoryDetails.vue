<template>
  <div class="container">
    <div v-if="!factory">
      <h1>PAGE FOR THIS FACTORY DOES NOT EXIST!</h1>
    </div>
    <div v-else-if="dataLoaded">
      <div class="manager-info">
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
          </tbody>
        </table>
        <div class="chocolate-div-container-container">
          <h1>CHOCOLATES</h1>
          <label style="margin-top: 50px; margin-left: 110px;" v-if="loggedInUser.factoryId == factory.id && loggedInUser.role == 'MANAGER'">
            <button v-on:click="showAddForm(factory.id)" class="button-with-image">
              <img src="../../public/Images/add.png" style="width: 30px;"  alt="Image">
            </button>
          </label>
          <div class="chocolate-div-container">
            <div style="height: 200px;" v-for="c in factory.chocolates" class="chocolate-div">
              <table class="chocolate-table">
                <tr><img :src="c.image" alt="Chocolate" class="chocolate-image"></tr>
                <tr>
                  <td style="text-align: center; font-size: 15px; font-weight: bold;">
                    {{ c.name }}
                  </td> 
                </tr>
                <tr>
                  <td style="text-align: center; font-size: 15px;" v-if="c.quantity > 0">{{ "In Stock 🟢 (" + c.quantity + ")"}}</td>
                  <td style="text-align: center; font-size: 15px;" v-else>Not In Stock 🔴</td>
                </tr>
                <tr v-if="loggedInUser.factoryId == factory.id">
                  <td  style="text-align: center;">
                    <button v-on:click="showUpdateForm(c)" class="btn">Edit</button>
                  </td>   
                </tr>
                <tr v-if="loggedInUser.factoryId == factory.id">
                  <td style="text-align: center;">
                    <button v-on:click="deleteChocolate(c)" class="btn">Delete</button>
                  </td>   
                </tr>
                <tr v-if="loggedInUser.role == 'CUSTOMER' && c.quantity > 0">
                  <td  style="text-align: center;">
                    <button v-on:click="showMyCart(c)" class="btn">Buy</button>
                  </td>   
                </tr>
              </table>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import axios from 'axios';
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import { useRouter} from 'vue-router';

const route = useRoute();
const factory = ref({});
const dataLoaded = ref(false);
const router = useRouter();

const loggedInUser = ref({});

onMounted(() => {
  loadFactory();
});

function loadFactory() {
  axios.get(`http://localhost:8080/ChoccolateAppREST/rest/ChocolateFactoryService/getById/${route.params.factoryId}`).then(response => {
    if(!response.data) {
      factory.value = null;
      return;
    }
    factory.value = response.data;
    if(factory.value.chocolates.length>0)
    {
      factory.value.chocolates = factory.value.chocolates.filter(chocolate => !chocolate.isDeleted);
    }
    
    
    if(JSON.parse(localStorage.getItem('loggedUser')))
    {
      loggedInUser.value= JSON.parse(localStorage.getItem('loggedUser'));  
    }
    
    dataLoaded.value = true;  
  });
}

function showAddForm(factoryId) {
  router.push({name: 'addChocolateForm', params: {factoryId: factoryId}});
}
function showUpdateForm(chocolate){
  router.push({ name: 'updateChocolateForm', params: { chocolateId: chocolate.id } });
}
function showMyCart(chocolate){
  router.push({ name: 'myCartView', params: { chocolateId: chocolate.id }});
}
function deleteChocolate(chocolate){
  axios.post('http://localhost:8080/ChoccolateAppREST/rest/chocolates/deleteChocolate', chocolate, {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('jsonWebToken')}`
      }
    })
    .then(response => {
       loadFactory();
    })
    .catch(error => {
      console.error(error);
    });
}
</script>

<style scoped>
.container {
  max-width: 960px;
  margin: 0 auto;
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  background-color: #dfd1c2;
  border-radius: 8px;
  margin-top: 100px; 
}

h1 {
  text-align: center;
}

.manager-info label {
  display: block;
  font-size: 1.2rem;
  margin-bottom: 10px;
  text-align: center;
}

.manager-info {
  display: flex;
}

.factory-table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 20px;
}

.chocolate-div-container-container {
    display: flex;
  width: 690px;
  flex-wrap: wrap;
  box-sizing: border-box;
  border-right: 50px solid #dfd1c2; 
  border-top-right-radius: 8px;
}

.chocolate-div-container {
  background-color: #f7f7f7;
  display: flex;
  width: 690px;
  flex-wrap: wrap;
  height: 650px;
  overflow-y: scroll;
  border-bottom: 50px solid #dfd1c2;
}

.chocolate-div {
  justify-content: center;
  align-items: center;
  margin: 25px;
}

.factory-table th,
.factory-table td {
  padding: 12px;
  text-align: left;
  border-bottom: 1px;
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
  background-color: #d6cdc4;
}
.button-with-image {
    display: inline-block;
    background-color: transparent;
    border: none;
    border-radius: 5px;
    cursor: pointer;
    transition: background-color 0.3s ease;
  }

  .button-with-image:hover {
    background-color: #ddd;
  
  }

  .item {
    position: relative;
    padding: 10px;
  }

.btn {
    opacity: 0;
    position:relative;  
    transition: opacity 0.3s ease;
}

.chocolate-div:hover .btn {
    opacity: 1;
}

.chocolate-table{
  transition: background-color 0.3s ease;
}

.chocolate-table:hover {
  background-color: #ddd;
}
</style>

