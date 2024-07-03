<template>
    <div v-if="dataLoaded && purchase.state == 'Processing'" style="margin-top: 100px;">
        <div class="container" style="padding: 10px;">
            <h1 style="font-weight: bold; text-align: center">Purchase detals</h1>
        <div class="row">
            <div class="col-md-2">
                <div class="card" style="width: 310px;">
                    <div class="card-header" style="font-weight: bold;">
                        User info
                    </div>
                    <div style="padding: 10px">
                        <p style="font-weight: bold;" class="card-text">Name:<label class="card-text" style="margin-left: 10px;">{{ purchase.customer.name }}</label></p>
                        <p style="font-weight: bold;" class="card-text">Surname:<label class="card-text" style="margin-left: 10px;">{{ purchase.customer.surname }}</label></p>
                        <p style="font-weight: bold;" class="card-text">Username:<label class="card-text" style="margin-left: 10px;">{{ purchase.customer.username }}</label></p>
                    </div>
                </div>
                <div class="card" style="width: 310px; margin-top: 20px;">
                    <div class="card-header" style="font-weight: bold;">
                        Purchase info
                    </div>
                    <div style="padding: 10px">
                         <p  style="font-weight: bold;" class="card-text">Date and time:<label class="card-text"  style="margin-left: 10px;">{{ formatDate(purchase.dateAndTime) }}</label></p>
                        <p style="font-weight: bold;" class="card-text">Factory:<label class="card-text" style="margin-left: 10px;">{{ factory.name }}</label></p>
                        <p style="font-weight: bold;" class="card-text">Purchase status:
                        <label class="card-text" :style="{ color: purchase.state === 'Accepted' ? 'green' : purchase.state === 'Declined' ? 'red' : '' }" style="margin-left: 10px;">
                         {{ purchase.state }}
                        </label>
                        </p>
                    </div>
                </div>
            </div>
            <div v-if="dataLoaded">
                <div class="card" style="margin-left: 200px;">
                    <h5 class="card-header">Purchase</h5>
                    <div v-if="dataLoaded" v-for="chocolate in purchase.chocolates" class="card-body">   
                        <img :src="chocolate.image" alt="chocolate" style="width: 150px; height: 150px;"/>     
                        <div class="chocolate-info">
                            <div>
                            <label style="font-size: 12; font-weight: bold" class="card-title">{{ chocolate.name }}</label>
                            </div>
                            <label class="card-text">{{ "Weight: " + chocolate.weight + "g" }}</label> 
                            <label class="card-text">{{ "Type: " + chocolate.type }}</label> 
                            <label class="card-text">{{ "Kind: " + chocolate.kind }}</label> 
                            <label class="card-text">{{ "Price: " + chocolate.price + "RSD" }}</label>
                            <label class="card-text">{{ "Quantity: " + chocolate.quantity }}</label>      
                        </div>   
                    </div>  
                    <div class="card-body">
                            <label class="card-text">{{ "In Total: " + purchase.price + "RSD" }}</label>
                    </div>     
             </div>
         </div>  
         <div style="display: flex; flex-direction: column; align-items: flex-start;">
         <button v-if="loggedInUser.role=='MANAGER'" type="button" class="btn btn-success" style="margin-bottom: 10px; height: 50px; margin-left: 50px;" @click="updatePurchaseStatus('Accepted')">Accept</button>
         <button v-if="loggedInUser.role=='CUSTOMER'" type="button" class="btn btn-danger" style="margin-bottom: 10px; height: 50px; margin-left: 50px;" @click="updatePurchaseStatus('Canceled')">Cancel</button>
         <button v-if="loggedInUser.role=='ADMIN'" type="button" class="btn btn-danger" style="margin-bottom: 10px; height: 50px; margin-left: 50px;" @click="deletePurchase(purchase.id)">Delete</button>
         <label v-if="loggedInUser.role=='CUSTOMER'" style="color: red; margin-left: 50px;" >Warning: Canceling the order results in losing points</label>
        <div style="display: flex; flex-direction: row; align-items: center; margin-left: 50px;">
        <button v-if="loggedInUser.role=='MANAGER'" type="button" class="btn btn-danger" style="margin-right: 10px; height: 50px;" @click="updatePurchaseStatus('Declined')">Decline</button>
        <input v-if="loggedInUser.role=='MANAGER' && isDeclined" placeholder="enter reason for declining purchase" style="height: 50px; width: 260px;" type="text" v-model="comment">
         </div>
        </div>
        </div>
        </div>
    </div>
    <div v-else>
        <h1 style="color: red;">Can not load purchase</h1>
    </div>
    <div v-else>
        <h1 style="color: red;">Purchase is already processed</h1>
    </div>
</template>
<script setup>
import axios from 'axios';
import { param } from 'jquery';
import { get } from 'ol/proj';
import {onMounted, ref} from 'vue';
import {useRouter, useRoute} from 'vue-router';
const route = useRoute();
const router = useRouter();
const loggedInUser = ref({});
const purchase = ref({id:0,chocolates:{},factoryId:0,dateAndTime:{},price:0,customer:{},state:'Processing', declineReason:""});
const purchaseToSend = ref({id:0,chocolates:{},factoryId:0,dateAndTime:{},price:0,customer:{},state:'Processing', declineReason:""});
const dataLoaded = ref(false);
const user = ref({chocolates:{}})
const factory = ref({name:""})
const isDeclined = ref(false)
const comment = ref("")
const purchases = ref([])
onMounted(()=>{
    loggedInUser.value = JSON.parse(localStorage.getItem('loggedUser'));
    loadPurchases();
    
})
function loadPurchases() {
    if(loggedInUser.value.role == 'MANAGER')
    {
        axios.get(`http://localhost:8080/ChoccolateAppREST/rest/purchases/getPurchasesForManager/${loggedInUser.value.id}`)
        .then(response=>{
            purchases.value = response.data 
            getPurchase();
      
    });
    }
    else if(loggedInUser.value.role == 'CUSTOMER')
    {
        axios.get(`http://localhost:8080/ChoccolateAppREST/rest/purchases/getPurchasesForCustomer/${loggedInUser.value.id}`)
        .then(response=>{
            purchases.value = response.data 
            getPurchase();
      
    }); 
    }
  
} 
function getPurchase()
{
    purchase.value = purchases.value.find(p => p.id == route.params.purchaseId);
    getFactory();

}

function formatDate(dateTime) {
    if(dateTime!=null){
    const date = new Date(dateTime.year, dateTime.monthValue - 1, dateTime.dayOfMonth);
   
    const formattedDate = date.toLocaleDateString('en-US', {
       year: 'numeric',
       month: 'short',
       day: 'numeric',
       hour: 'numeric',
       minute: 'numeric',
       second: 'numeric'
   });
   return formattedDate;
    }
  
}
function getFactory() {
    
    axios.get(`http://localhost:8080/ChoccolateAppREST/rest/ChocolateFactoryService/getFactoryById/${purchase.value.factoryId}`)
    .then(response=>{
        factory.value = response.data;
        dataLoaded.value = true;
      
    });
    
}

function updatePurchaseStatus(status){

    if(status == 'Declined')
    {
        isDeclined.value = true;
    }
    else
    {
        isDeclined.value = false;
        comment.value = "";
    }

    if(purchaseToSend.value.id == 0) {
        purchaseToSend.value = JSON.parse(JSON.stringify(purchase.value));
    }

    purchaseToSend.value.state = status
    purchaseToSend.value.declineReason = this.comment
    
    if((this.purchaseToSend.state == 'Declined' && this.purchaseToSend.declineReason!="") || (this.purchaseToSend.state == 'Accepted' && this.purchaseToSend.declineReason=="") || (this.purchaseToSend.state == 'Canceled' && this.purchaseToSend.declineReason==""))
    {
        purchaseToSend.value.dateAndTime = ref(null)
        axios.put('http://localhost:8080/ChoccolateAppREST/rest/purchases/updatePurchaseStatus', this.purchaseToSend, {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('jsonWebToken')}`
      }
    }) .then(response => {
            loggedInUser.value = JSON.parse(localStorage.getItem('loggedUser'));
            
          
            if (loggedInUser.value.role === 'MANAGER') {
                router.push({ name: 'managerPurchases'});
            } else if (loggedInUser.value.role === 'CUSTOMER') {
                router.push({ name: 'customerPurchases'});
            }
        }).catch(error => {
            console.error('Error updating purchase status:', error);
        });
    }
        
    return;
}
/*function deletePurchase(id){
    axios.post('http://localhost:8080/ChoccolateAppREST/rest/deleteUser', user.id, {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('jsonWebToken')}`,
        'Content-Type': 'application/json'
      }
      
    })
    .then(response=>{
        loadUsers();
    });
}*/

</script>
<style scoped>

.container {
    font-family: Arial, sans-serif;
    background-color: #dfd1c2;
    border-radius: 8px;
    margin-top: 50px;
}
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