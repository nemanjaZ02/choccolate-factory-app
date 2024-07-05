<template>
  <div :style="{ width: '1100px', height: '300px', zIndex: 1 }" ref="mapContainer" class="map-container"></div>
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
              <td>Address:</td>
              <td>{{ factory.location.adress.street + " " + factory.location.adress.streetNum + ", " + factory.location.adress.city + ", " + factory.location.adress.postNum }}</td>
            </tr>
            <tr>
              <td>City:</td>
              <td>{{ factory.location.adress.city }}</td>
            </tr>
            <tr>
              <td>Working Status:</td>
              <td>{{ factory.status }}</td>
            </tr>
            <tr>
              <td>Average rate:</td>
              <td>{{ factory.rating.toFixed(2) }}</td>
            </tr>
          </tbody>
        </table>
        <div class="chocolate-div-container-container">
          <h1>CHOCOLATES</h1>
          <label style="margin-top: 50px; margin-left: 110px;" v-if="loggedInUser.factoryId == factory.id && loggedInUser.role == 'MANAGER'">
          <div class="button-container">
            <button v-on:click="showAddForm(factory.id)"  class="button-with-image">
              <img src="../../public/Images/addChocolate.png" style="width: 64px;"  alt="Image">
            </button>
            <button v-on:click="showAddEmployeeForm(factory.id)" style="margin-left: 60px;" class="button-with-image">
              <img src="../../public/Images/addUser.png" style="width: 40px; "  alt="Image">
            </button>
          </div>
          </label>
          <div class="chocolate-div-container">
            <div style="height: 200px;" v-for="c in factory.chocolates" class="chocolate-div">
              <table class="chocolate-table hover-table">
                <tr><img :src="c.image" alt="Chocolate" class="chocolate-image"></tr>
                <tr>
                  <td style="text-align: center; font-size: 15px; font-weight: bold;">
                    {{ c.name }}
                  </td> 
                </tr>
                <tr>
                  <td  style="text-align: center;">
                    <button id="toggleDescriptionButton" style="text-align: center;" v-on:click="toggleDescription(c)" class="btn btn-primary my-2 my-sm-0">Show Details</button>
                  </td>
                </tr>
                <tr>
                  <td>{{ "Type: " + c.type + ", " + c.kind }}</td>
                </tr>
                <tr>
                  <td>{{ "Price: " + c.price + "RSD/" + c.weight + "g" }}</td>
                </tr>
                <tr>
                  <td style="text-align: center; font-size: 15px;" v-if="c.quantity > 0">{{ "In Stock 🟢 (" + c.quantity + ")"}}</td>
                  <td style="text-align: center; font-size: 15px;" v-else>Not In Stock 🔴</td>
                </tr>
                <tr class="hover-row" v-if="loggedInUser.factoryId == factory.id && loggedInUser.role == 'MANAGER'">
                  <td  style="text-align: center;">
                    <button v-on:click="showUpdateForm(c)" class="btn">Edit</button>
                  </td>   
                </tr>
                <tr class="hover-row" v-if="loggedInUser.factoryId == factory.id && loggedInUser.role == 'MANAGER'">
                  <td style="text-align: center;">
                    <button v-on:click="deleteChocolate(c)" class="btn">Delete</button>
                  </td>   
                </tr>
                <tr class="hover-row" v-if="loggedInUser.factoryId == factory.id && loggedInUser.role == 'EMPLOYEE'">
                  <td style="text-align: center;">
                    <text>QTY </text>
                    <input type="number" v-model="c.quantity" style="width: 65px;">
                  </td>  
                </tr>
                <tr class="hover-row" v-if="loggedInUser.factoryId == factory.id && loggedInUser.role == 'EMPLOYEE'">
                  <td style="text-align: center;">
                    <button v-on:click="changeQuantity(c)" class="btn">Change</button>
                  </td>   
                </tr>
                <tr class="hover-row" v-if="loggedInUser.role == 'CUSTOMER' && c.quantity > 0">
                  <td  style="text-align: center;">
                    <button v-on:click="showMyCart(c)" class="btn">Buy</button>
                  </td>   
                </tr>
              </table>
              <div v-if="c.showDescription" class="description-popup">
                <div class="description-content">{{ c.description }}</div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>

  <div v-if="canUserComment" class="comment-form">
    <h1>Leave a Comment</h1>
    <form @submit.prevent="submitComment">
      <textarea v-model="comment.text" placeholder="Write your comment here..." required></textarea>
      <div class="rating">
        <span v-for="star in 5" :key="star" @click="setRating(star)" :class="{'active-star': star <= comment.rating}">★</span>
      </div>
      <button type="submit" class="btn-submit">Submit</button>
    </form>
  </div>

  <h1 v-if="comments.length > 0">Comments</h1>
  <div class="comment-form" v-for="comment in comments" style="background-color: #dfd1c2;">
    <div class="card-header" style="background-color: #d6cdc4;">
      {{ comment.customer.username }}
      <span style="color: yellow;" v-if="comment.status == 'PENDING'">PENDING</span>
      <span style="color: red;" v-if="comment.status == 'DECLINED'">DECLINED</span>
    </div>
    <div class="card-body">
      <div class="rating-readonly" style="background-color: #d6cdc4;;">
          <span v-for="star in 5" :key="star" :class="{'active-star': star <= comment.rating}">★</span>
      </div>
      <p class="card-text">{{ comment.text }}</p>
      <button class="btn btn-primary" v-if="comment.status == 'PENDING' && loggedInUser.role == 'MANAGER'" style="background-color: green;" v-on:click="approveComment(comment)">Approve</button>
      <button class="btn btn-secondary" v-if="comment.status == 'PENDING' && loggedInUser.role == 'MANAGER'" style="background-color: red; margin-left: 20px" v-on:click="declineComment(comment)">Decline</button>
      <button class="btn btn-danger" v-if="loggedInUser.role == 'ADMIN'" v-on:click="deleteComment(comment)">DELETE</button>
    </div>
  </div>
</template>

<script setup>
import axios from 'axios';
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import { useRouter } from 'vue-router';
import L from 'leaflet'
import 'leaflet/dist/leaflet.css'
import 'leaflet-defaulticon-compatibility/dist/leaflet-defaulticon-compatibility.css'
import "leaflet-defaulticon-compatibility";
import UpdateChocolate from './UpdateChocolate.vue';

const route = useRoute();
const factory = ref({});
const dataLoaded = ref(false);
const router = useRouter();
const canUserComment = ref(false);
const comment = ref({id: 0, customer: null, factoryId: 0, text: '', rating: 0, status: 'PENDING'});
const comments = ref([]);

const loggedInUser = ref({});

const map = ref()
const mapContainer = ref()

onMounted(() => {
  loadFactory();
  loadComments();
});

function loadComments() {
  axios.get(`http://localhost:8080/ChoccolateAppREST/rest/comments/getCommentsForFactory/${route.params.factoryId}`, {
    headers: {
      'Authorization': `Bearer ${localStorage.getItem('jsonWebToken')}`
    }
  }).then(response => {
    if (!response.data) {
      return;
    }
    comments.value = response.data.filter(comment => comment.isDeleted == false);
  });
}

function loadFactory() {
  axios.get(`http://localhost:8080/ChoccolateAppREST/rest/ChocolateFactoryService/getById/${route.params.factoryId}`).then(response => {
    if (!response.data) {
      factory.value = null;
      return;
    }
    factory.value = response.data;
    if (factory.value.chocolates.length > 0) {
      factory.value.chocolates = factory.value.chocolates.filter(chocolate => !chocolate.isDeleted);
    }

    if (JSON.parse(localStorage.getItem('loggedUser'))) {
      loggedInUser.value = JSON.parse(localStorage.getItem('loggedUser'));
    }

    dataLoaded.value = true;
    if(loggedInUser.value.role == 'CUSTOMER')
      checkCanUserComment();

    if(loggedInUser.value.role == 'CUSTOMER' || !loggedInUser.value.role || (loggedInUser.value.role == 'MANAGER' && loggedInUser.value.factoryId != factory.value.id) || (loggedInUser.value.role == 'EMPLOYEE' && loggedInUser.value.factoryId != factory.value.id))
      factory.value.chocolates = factory.value.chocolates.filter(chocolate => chocolate.quantity > 0);

    createMap();
  });
}

function createMap()
{
    map.value = L.map(mapContainer.value).setView([factory.value.location.latitude, factory.value.location.longitude], 13);
    L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
    }).addTo(map.value);

    L.marker([factory.value.location.longitude, factory.value.location.latitude]).addTo(map.value)
    .bindPopup(factory.value.name)
    .openPopup()
}

function checkCanUserComment() {
  axios.get(`http://localhost:8080/ChoccolateAppREST/rest/comments/canComment/${route.params.factoryId}`, {
    headers: {
      'Authorization': `Bearer ${localStorage.getItem('jsonWebToken')}`
    }
  }).then(response => {
    canUserComment.value = response.data;
  });
}

function showAddForm(factoryId) {
  router.push({ name: 'addChocolateForm', params: { factoryId: factoryId } });
}
function showAddEmployeeForm(factoryId){
  router.push({ name: 'addEmployeeView', params: { factoryId: factoryId } });
}
function showUpdateForm(chocolate) {
  router.push({ name: 'updateChocolateForm', params: { chocolateId: chocolate.id } });
}
function showMyCart(chocolate) {
  router.push({ name: 'myCartView', params: { chocolateId: chocolate.id } });
}
function deleteChocolate(chocolate) {
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

function setRating(star) {
  comment.value.rating = star;
}

function submitComment() {  
  comment.value.factoryId = factory.value.id;
  axios.post('http://localhost:8080/ChoccolateAppREST/rest/comments/addComment', comment.value, {
    headers: {
      'Authorization': `Bearer ${localStorage.getItem('jsonWebToken')}`
    }
  }).then(response => {
    router.go(0);
  });
}

function approveComment(comment) {
  axios.post(`http://localhost:8080/ChoccolateAppREST/rest/comments/approveComment`, comment, {
    headers: {
      'Authorization': `Bearer ${localStorage.getItem('jsonWebToken')}`
    }
  }).then(response => {
    router.go(0);
  });
}

function declineComment(comment) {
  axios.post(`http://localhost:8080/ChoccolateAppREST/rest/comments/declineComment`, comment, {
    headers: {
      'Authorization': `Bearer ${localStorage.getItem('jsonWebToken')}`
    }
  }).then(response => {
    router.go(0);
  });
}

function changeQuantity(chocolate) {
  axios.put('http://localhost:8080/ChoccolateAppREST/rest/chocolates/updateChocolate', chocolate, {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('jsonWebToken')}`
      }
    }) .then(response=>{
      let responseData = response.data;
       axios.put('http://localhost:8080/ChoccolateAppREST/rest/ChocolateFactoryService/updateChocolate', responseData, {
          headers: {
            'Authorization': `Bearer ${localStorage.getItem('jsonWebToken')}`
          }
       })
    });
}

function deleteComment(comment) {
  axios.post(`http://localhost:8080/ChoccolateAppREST/rest/comments/deleteComment`, comment, {
    headers: {
      'Authorization': `Bearer ${localStorage.getItem('jsonWebToken')}`
    }
  }).then(response => {
    loadComments();
  });

}

function toggleDescription(chocolate) {
    chocolate.showDescription = !chocolate.showDescription;
}
</script>

<style scoped>
.description-popup {
  position: absolute;
  top: 210px; 
  left: 0;
  width: 100%;
  background-color: #fff;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  z-index: 10;
  padding: 10px;
  border-radius: 5px;
}

.description-content {
  max-height: 300px; 
  overflow-y: auto;
}

.container {
  max-width: 960px;
  margin: 0 auto;
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  background-color: #dfd1c2;
  border-radius: 8px;
  margin-top: 20px;
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
  position: relative;
  margin-bottom: 20px;
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

.button-container {
      font-size: 0; 
  }

  .button-with-image {
      display: inline-block;
      background-color: transparent;
      border: none;
      border-radius: 5px;
      cursor: pointer;
      transition: background-color 0.3s ease;
      padding: 10px; 
      margin-right: 20px;
  }


  .button-with-image img {
      width: 100%; 
      height: auto; 
  }

  .button-with-image:hover {
      background-color: #ddd;
  }
.item {
  position: relative;
  padding: 10px;
}

.btn {
  opacity: 1;
  position: relative;
  transition: opacity 0.3s ease;
}

.chocolate-div:hover .btn {
  opacity: 1;
}

.chocolate-table {
  transition: background-color 0.3s ease;
}

.chocolate-table:hover {
  background-color: #ddd;
}

.comment-form {
  width: 50%;
  margin-top: 20px;
  margin-left: 25%;
  background-color: #f7f7f7;
  padding: 20px;
  border-radius: 8px;
}

.comment-form h1 {
  margin-bottom: 20px;
}

.comment-form textarea {
  width: 100%;
  height: 100px;
  padding: 10px;
  margin-bottom: 10px;
  border-radius: 5px;
  border: 1px solid #ccc;
}

.comment-form .rating {
  display: flex;
  justify-content: center;
  margin-bottom: 10px;
}

.comment-form .rating span {
  font-size: 30px;
  cursor: pointer;
  color: #ccc;
  transition: color 0.3s ease;
}

.comment-form .rating .active-star {
  color: gold;
}

.rating-readonly {
  display: flex;
  justify-content: center;
  margin-bottom: 10px;
}

.rating-readonly span {
  font-size: 30px;
  color: #ccc;
}

.rating-readonly .active-star {
  color: gold;
}

.comment-form .btn-submit {
  display: block;
  width: 100%;
  padding: 10px;
  background-color: #333;
  color: #fff;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-size: 16px;
  text-align: center;
  transition: background-color 0.3s ease;
}

.comment-form .btn-submit:hover {
  background-color: #555;
}

.map-container {
  width: 1100px;
  height: 300px;
  z-index: 1;
  margin-top: 100px;
  border-radius: 8px;
}

.hover-table .hover-row {
  opacity: 0;
  transition: opacity 0.3s ease;
}

.hover-table:hover .hover-row {
  opacity: 1;
}

#toggleDescriptionButton {
    background: rgba(123,63,0,1);
    
}

#toggleDescriptionButton:hover {
    background: rgb(83, 43, 0);
}
</style>
