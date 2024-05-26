<template>
<h1>Add Chocolate</h1>
<form @submit.prevent="addChocolate()">
<table>
  <tr>
    <th>Name:</th>
    <th><input v-model="chocolate.name" type="text"></th>
  </tr>
  <tr>
    <th>Price:</th>
    <th><input v-model="chocolate.price" type="number"></th>
  </tr>
  <tr>
    <th>Kind:</th>
    <th><input v-model="chocolate.kind" type="text"></th>
  </tr>
  <tr>
    <th>Type:</th>
    <th><input v-model="chocolate.type" type="text"></th>
  </tr>
  <tr>
    <th>Weight:</th>
    <th><input v-model="chocolate.weight" type="number"></th>
  </tr>
  <tr>
    <th>Description:</th>
    <th><input v-model="chocolate.description" type="text"></th>
  </tr>
  <tr>
    <th>Image:</th>
    <th><input v-model="chocolate.image" type="text" ></th>
  </tr>
  <tr>
    <th><input type="submit" value="Add"></th>
  </tr>
</table>
</form>
</template>


<script setup>

import axios from 'axios';
import {onMounted, ref} from 'vue';
import {useRouter} from 'vue-router';
import {useRoute} from 'vue-router';
const router = useRouter();
const route = useRoute();
const chocolate = ref({id:0, factoryId:route.params.factoryId, name:"", price:0, type:"", kind:"", status:"NotInStock", weight:0, description:"", image:"", quantity:0});
function addImage()
{
  chocolate.value.image = target.files[0]; 
}
function addChocolate()
{
   
    
    axios.post('http://localhost:8080/ChoccolateAppREST/rest/chocolates/addChocolate', this.chocolate, {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('jsonWebToken')}`
      }
    })
    .then(response=>{
       axios.put('http://localhost:8080/ChoccolateAppREST/rest/ChocolateFactoryService/addChocolate', this.chocolate, {
          headers: {
            'Authorization': `Bearer ${localStorage.getItem('jsonWebToken')}`
          }
       })
       .then(respone=>{
          router.push({name: 'factoryDetails', params: { factoryId: chocolate.factoryId }})
        })
    });
}



</script>