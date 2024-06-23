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
    <th>Type:</th>
    <th>
      <select v-model="chocolate.type" class="form-select">
        <option value="" disabled>Select Chocolate Type</option>
        <option value="Dark">Dark</option>
        <option value="Milk">Milk</option>
        <option value="White">White</option>
        <option value="Baking">Baking</option>
        <option value="Bittersweet">BiterSweet</option>
      </select>
    </th>
  </tr>
  <tr>
    <th>Kind:</th>
    <th>
      <select v-model="chocolate.kind" type="text">
        <option value="" disabled>Select Chocolate Kind</option>
        <option value="Nuts">With Nuts</option>
        <option value="Strawberry">With Strawberry</option>
        <option value="Filling">With Filling</option>
        <option value="Caramel">With Caramel</option>
        <option value="Cookies">With Cookie Crumbs</option>
        <option value="Rice">With Rice</option>
      </select>
    </th>
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
    <th><input type="file" @change="handleFileUpload"></th>
  </tr>
  <tr>
    <th><input type="submit" value="Add"></th>
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
const selectedFile = ref(null);
const router = useRouter();
const route = useRoute();
const chocolate = ref({id:0, factoryId:route.params.factoryId, name:"", price:0, type:"", kind:"", status:"NotInStock", weight:0, description:"", image:"", quantity:0, isDeleted: false});
const errorMessage = ref(" ");
function addChocolate()
{ 
    axios.post('http://localhost:8080/ChoccolateAppREST/rest/chocolates/addChocolate', this.chocolate, {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('jsonWebToken')}`
      }
      
    })
    .then(response=>{
          router.push({name: 'factoryDetails', params: { factoryId: chocolate.factoryId }})
    })
    .catch(error => {
        if(error.response.status === 400)
        {
          this.errorMessage = "You must select kind and type";
        }
        else
        {
          this.errorMessage = error.response.data
        }    
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
            chocolate.value.image = response.data;
          }
      })
}

                        

</script>