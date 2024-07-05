<template>
    <nav  class="navbar navbar-light" style="background-color: transparent; margin-top: 100px;">
        <div class = "container" style="height: 120px; width: 1000px;">
            <form class="form-inline" @submit.prevent="search()">
                <div>
                    <label style="font-size: 1.5em; padding: 10px;">Minimum Price</label>
                    <input class="form-control mr-sm-2" type="number" step="any" v-model="minimumPrice" placeholder="Search" min="0" aria-label="Search" pattern=".*\S.*">
                </div>
                
                <div>
                    <label style="font-size: 1.5em; padding: 10px;">Maximum Price</label>
                    <input class="form-control mr-sm-2" type="number" step="any" v-model="maximumPrice" placeholder="Search" min="0" aria-label="Search" pattern=".*\S.*">
                </div>

                <div>
                    <label style="font-size: 1.5em; padding: 10px;">Minimum Date</label>
                    <input class="form-control mr-sm-2" type="date" step="any" v-model="minimumDate" placeholder="Search" aria-label="Search" pattern=".*\S.*">
                </div>

                <div>
                    <label style="font-size: 1.5em; padding: 10px;">Maximum Date</label>
                    <input class="form-control mr-sm-2" type="date" step="any" v-model="maximumDate" placeholder="Search" aria-label="Search" pattern=".*\S.*">
                </div>
                <div>
                    <label style="color: transparent; font-size: 1.5em; padding: 10px;">a</label>
                    <button id="searchButton" class="btn btn-primary my-2 my-sm-0"  type="submit">Search</button>
                </div>

                
            </form>        
        </div>
    </nav>
<div class = "col-md-10" style="margin-top: 100px; margin-left: 100px;">
    
    <div class="container">
        <h1 style="text-align: center;" >Purchases</h1>
        <select class="form-select form-select-lg mb-3" aria-label=".form-select-lg example" v-on:change="sortPurchases()" v-model="sortOption"> 
            <option disabled selected>Sort By</option>
            <option value="price_asc">Price Ascending</option>
            <option value="price_desc">Price Descending</option>
            <option value="date_asc">Date Ascending</option>
            <option value="date_desc">Date Descending</option>
        </select>
        <div class="row">
            <div class="col-lg-4 col-md-6 mb-4" v-if="dataLoaded" v-for="p in filteredPurchases" :key="p.id" @click="showDetails(p)" >
                <div class="purchase">
                    <div class="card">
                        <img src="../../public/Images/shoppingCart.png" class="card-img-top">
                        <div class="card-body">
                            <h5 class="card-title">Purchase {{p.id }}</h5>
                            <p v-if="p.state === 'Accepted'" class="text-success">ACCEPTED 🟢</p>
                            <p v-if="p.state === 'Declined'" class="text-danger">DECLINED 🔴</p>
                            <p v-if="p.state === 'Declined'" class="text-danger">{{"Reason:"+p.declineReason }}</p>
                            <p v-if="p.state === 'Canceled'" class="text-danger">CANCELED 🔴</p>
                            <p v-if="p.state === 'Processing'" class="text-warning">PROCESSING 🟡</p>
                        </div>
                        <ul class="list-group list-group-flush">
                            <li class="list-group-item">{{ "Purchase date: " +formatDate(p.dateAndTime)}}</li>
                            <li class="list-group-item">{{ "Price: "+p.price}}</li>
                            <li class="list-group-item">{{ "Customer: " + p.customer.username }}</li>
                            <li class="list-group-item"><button v-if="loggedInUser.role=='MANAGER'" type="button" class="btn-danger" style="margin-left: 10px; height: 50px; width: 90px;" @click.stop="deletePurchase(p.id)">Delete</button></li>
                        </ul>
                        <div>
                        
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</template>
<script setup>
import axios from 'axios';
import {onMounted, ref} from 'vue';
import {useRouter} from 'vue-router';
import {useRoute} from 'vue-router';

const router = useRouter();
const route = useRoute();
const purchases = ref([]);
const filteredPurchases = ref([]);
const factory = ref()
const sortOption = ref("Sort By");
const username = ref("")
const minimumDate = ref("")
const maximumDate = ref("")
const minimumPrice = ref("")
const maximumPrice = ref("")
const dataLoaded = ref(false)
const loggedInUser = ref(null)
onMounted(() => {
    loggedInUser.value = JSON.parse(localStorage.getItem('loggedUser'));
    loadPurchases();
    
})

function loadPurchases() {
    axios.get(`http://localhost:8080/ChoccolateAppREST/rest/purchases/getPurchasesForManager/${loggedInUser.value.id}`)
    .then(response=>{
        purchases.value = response.data 
        filteredPurchases.value = purchases.value  
        if (purchases.value.length > 0) {
            purchases.value = purchases.value.filter(purchase => !purchase.isDeleted);
            filteredPurchases.value = purchases.value;
        }
        dataLoaded.value = true;
      
    });
} 
function formatDate(dateTime) {
    const date = new Date(dateTime.year, dateTime.monthValue - 1, dateTime.dayOfMonth, dateTime.hour, dateTime.minute, dateTime.second);
   
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
function sortPurchases() {
    if (sortOption.value === "date_asc") {
        filteredPurchases.value.sort((a, b) => {
            const dateA = new Date(formatDate(a.dateAndTime));
            const dateB = new Date(formatDate(b.dateAndTime));
            return dateA - dateB;
        });
    } else if (sortOption.value === "date_desc") {
        filteredPurchases.value.sort((a, b) => {
            const dateA = new Date(formatDate(a.dateAndTime));
            const dateB = new Date(formatDate(b.dateAndTime));
            return dateB - dateA;
        });
    }else if (sortOption.value === "price_asc") {
        filteredPurchases.value.sort((a, b) => a.price - b.price);
    } else if (sortOption.value === "price_desc") {
        filteredPurchases.value.sort((a, b) => b.price - a.price);
    }
}
function convertAndFormatDate(dateString) {
    
    const dateParts = dateString.split('-');
    const year = parseInt(dateParts[0]);
    const month = parseInt(dateParts[1]) - 1; 
    const day = parseInt(dateParts[2]);


    const date = new Date(year, month, day);

   
    const formattedDate = date.toLocaleDateString('en-US', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit'
    });

    return formattedDate;
}

function showDetails(purchase){

    router.push({name: 'purchaseDetailsManager', params: { purchaseId: purchase.id }});
}

function search() {
    filteredPurchases.value = purchases.value.filter(purchase => 
        (minimumPrice.value == "" || minimumPrice.value == 0 || (purchase.price >= minimumPrice.value)) &&
        (maximumPrice.value == "" || maximumPrice.value == 0 || (purchase.price <= maximumPrice.value)) &&
        (minimumDate.value == "" || minimumDate.value == 0 || (new Date(formatDate(purchase.dateAndTime)) >= new Date(convertAndFormatDate(minimumDate.value)))) &&
        (maximumDate.value == "" || maximumDate.value == 0 || (new Date(formatDate(purchase.dateAndTime)) <= new Date(convertAndFormatDate(maximumDate.value))))
    );

}

function deletePurchase(purchaseId){
    axios.post('http://localhost:8080/ChoccolateAppREST/rest/purchases/deletePurchase', purchaseId, {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('jsonWebToken')}`,
        'Content-Type': 'application/json'
      }
      
    })
    .then(response=>{
        loadPurchases();
    });
}
</script>
<style scoped>

.container {
    font-family: Arial, sans-serif;
    background-color: #dfd1c2;
    border-radius: 8px;
    margin-top: 50px;
}
.card {
    transition: transform 0.3s, box-shadow 0.3s;
}
.purchase:hover .card {
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
    transform: translateY(-5px); 
    cursor: pointer;
}
#searchButton {
    background: rgba(123,63,0,1);
    
}

#searchButton:hover {
    background: rgb(83, 43, 0);
}
</style>