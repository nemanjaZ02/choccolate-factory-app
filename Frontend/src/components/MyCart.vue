<template>
  <label style="color: red; margin-top: 100px;">{{ errorMessage }}</label>
  <div v-if="dataLoaded && cart.chocolates.length != 0">
    <div class="card">
      <h5 class="card-header">My Cart</h5>
      <div v-if="dataLoaded" v-for="chocolate in cart.chocolates" class="card-body">   
          <img :src="chocolate.image" alt="chocolate" style="width: 150px; height: 150px;"/>     
          <div class="chocolate-info">
            <div>
              <label style="font-size: 12; font-weight: bold" class="card-title">{{ chocolate.name }}</label>
              <button class="btn btn-danger" style="margin-left: 12px;" v-on:click="removeChocolate(chocolate)">-</button>
            </div>
            <label class="card-text">{{ "Weight: " + chocolate.weight + "g" }}</label> 
            <label class="card-text">{{ "Type: " + chocolate.type }}</label> 
            <label class="card-text">{{ "Kind: " + chocolate.kind }}</label> 
            <label class="card-text">{{ "Price: " + chocolate.price + "RSD" }}</label>
            <div>
              <label class="card-text" >Quantity:</label>
              <input class="card-text" type="number" style="width: 40px;" v-model="chocolate.quantity" v-on:change="changeQuantity(chocolate)"/>
            </div>     
          </div>   
      </div>  
    <div class="card-body">
      <label class="card-text">{{ "In Total: " + totalPrice + "RSD" }}</label>
    </div> 
    <div class="card-body">
      <a href="#" class="btn btn-primary" v-on:click="confirmPurchase()">Confirm</a>
    </div>  
    </div>
  </div>
  <div v-else>
    <label style="color: red; font-size: 14;">Your cart is empty!</label>
  </div>
</template>

<script setup>
import axios from 'axios';
import {onMounted, ref} from 'vue';
import {useRouter, useRoute} from 'vue-router';
const chocolate = ref({});
const route = useRoute();
const router = useRouter();
const loggedInUser = ref({});
const cart = ref({chocolates:[], price:0});
const dataLoaded = ref(false);
const errorMessage = ref('');
let totalPrice = ref(0);

onMounted(()=>{
  getChocolate();
})

function getChocolate()
{
  if(route.params.chocolateId)
  {
    axios.get(`http://localhost:8080/ChoccolateAppREST/rest/chocolates/getChocolate/${route.params.chocolateId}`).then(response=>{
    chocolate.value = response.data;
    addChocolateToCart();
  })
  }
  else
  {
    addChocolateToCart();
  }
}

function addChocolateToCart()
{
  if(JSON.parse(localStorage.getItem('cart')))
  {
    cart.value= JSON.parse(localStorage.getItem('cart'));
    loggedInUser.value = JSON.parse(localStorage.getItem('loggedUser'));
  }
  else if(JSON.parse(localStorage.getItem('loggedUser')))
  {
    loggedInUser.value = JSON.parse(localStorage.getItem('loggedUser'));
    cart.value = loggedInUser.value.cart;
  }
  else
  {
    return;
  }

  if(Object.keys(chocolate.value).length > 0)
  {
    if(cart.value.chocolates.length > 0)
    {
      if(cart.value.chocolates[0].factoryId != chocolate.value.factoryId)
      {
        cart.value.chocolates = [];
        errorMessage.value = 'One Purchase can contain chocolates from only one factory!';
      }
    }

    let boughtChocolate = chocolate.value;  
  
    if(cart.value.chocolates.some(c => c.id === boughtChocolate.id))
    {
      if(cart.value.chocolates.find(c => c.id === boughtChocolate.id).quantity < chocolate.value.quantity)
      {
        cart.value.chocolates.find(c => c.id === boughtChocolate.id).quantity++;
      }  
    }
    else
    {
      boughtChocolate.quantity = 1;
      cart.value.chocolates.push(boughtChocolate);
    }

    localStorage.setItem('cart', JSON.stringify(cart.value));
    cart.value= JSON.parse(localStorage.getItem('cart'));
        
    calculateTotal();
  }
  if(cart.value.chocolates.length > 0)
  {
    calculateTotal();
  }

  dataLoaded.value = true;
}

function calculateTotal()
{
  totalPrice.value = 0;
  cart.value.chocolates.forEach(chocolate => {
    totalPrice.value += chocolate.price * chocolate.quantity;
    cart.value.price = totalPrice.value;
    localStorage.setItem('cart', JSON.stringify(cart.value));
  });
}

function changeQuantity(chocolate)
{
  let check;
  axios.get(`http://localhost:8080/ChoccolateAppREST/rest/chocolates/getChocolate/${chocolate.id}`).then(response=>{
    check = response.data;
    if(chocolate.quantity <= 0)
    {
      chocolate.quantity = 1;
      cart.value.chocolates.find(c => c.id === check.id).quantity = 1;
    }
    else if(chocolate.quantity >= check.quantity)
    {
      chocolate.quantity = check.quantity;
      cart.value.chocolates.find(c => c.id === check.id).quantity = check.quantity;
    }
    else
    {
      cart.value.chocolates.find(c => c.id === check.id).quantity = chocolate.quantity;
    }
    localStorage.setItem('cart', JSON.stringify(cart.value));
    calculateTotal();
  })
}

function confirmPurchase()
{
  axios.post(`http://localhost:8080/ChoccolateAppREST/rest/purchases/addPurchase`, this.cart, { 
    headers: {
        'Authorization': `Bearer ${localStorage.getItem('jsonWebToken')}`
      }
  }).then(response=>{
    localStorage.removeItem('cart');
    router.push('/').then(() => router.go(0));
  })
}

function removeChocolate(choco) {
  let index = cart.value.chocolates.findIndex(c => c.id === choco.id);

  if (index !== -1) {
    cart.value.chocolates.splice(index, 1);
    localStorage.setItem('cart', JSON.stringify(cart.value));
    calculateTotal();
  }
}
</script>
  
<style scoped>
.card-body {
  display: flex;
  align-items: center;
}

.chocolate-info {
  display: flex;
  flex-direction: column;
  margin-left: 10px;
}
</style>