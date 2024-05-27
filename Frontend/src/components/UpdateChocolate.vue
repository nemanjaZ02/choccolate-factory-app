<template>
    <h1>Edit Chocolate</h1>
    <div>
    <div v-if="!chocolate">
      <h1>PAGE FOR THIS FACTORY DOES NOT EXIST!</h1>
    </div>
    <div  >
    <form   @submit.prevent="updateChocolate()">
       
    <table >
      <tr>
        <th>Name:</th>
        <th><input v-model="chocolate.name" type="text" ></th>
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
        <th><input type="submit" value="Edit"></th>
      </tr>
    </table>
    </form>
  </div>
  </div>
</template>
<script setup>

import axios from 'axios';
import {onMounted, ref} from 'vue';
import {useRouter} from 'vue-router';
import {useRoute} from 'vue-router';
const chocolate = ref({});
const route = useRoute();
const router = useRouter();

onMounted(()=>{
  getChocolate();
})

function getChocolate()
{
  axios.get(`http://localhost:8080/ChoccolateAppREST/rest/chocolates/getChocolate/${route.params.chocolateId}`).then(response=>{
    chocolate.value = response.data;
  })
   
}
function updateChocolate()
{   
    axios.put('http://localhost:8080/ChoccolateAppREST/rest/chocolates/updateChocolate', this.chocolate, {
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
       .then(respone=>{
          router.push({name: 'factoryDetails', params: { factoryId: responseData.factoryId }})
        })
    });
}
</script>

