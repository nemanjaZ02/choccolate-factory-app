<template>
    <nav class="div_style">
        <router-link :to="{name: 'myProfileView'}" v-if="isLoggedIn">{{loggedInUser.username}}</router-link>
        <router-link v-else to="/login">Login</router-link> 

        <label> | </label>

        <router-link :to="{name: 'factoryDetails', params: { factoryId: loggedInUser.factoryId }}" v-if="isLoggedIn && loggedInUser.role == 'MANAGER'">My Factory</router-link> 
        <router-link :to="{name: 'myCartView'}" v-if="isLoggedIn && loggedInUser.role == 'CUSTOMER'">My Cart</router-link>
        <label v-if="isLoggedIn && (loggedInUser.role == 'MANAGER' || loggedInUser.role == 'CUSTOMER')"> | </label>

        <router-link :to="{name: 'managerPurchases'}" v-if="isLoggedIn && loggedInUser.role == 'MANAGER'">My Factory Purchases</router-link> 
        
        <label v-if="isLoggedIn && (loggedInUser.role == 'MANAGER')"> | </label>

        <router-link :to="{name: 'customerPurchases'}" v-if="isLoggedIn && loggedInUser.role == 'CUSTOMER'">My Purchases</router-link> 
        
        <label v-if="isLoggedIn && (loggedInUser.role == 'CUSTOMER')"> | </label>

        <a href="#" class="router-link-like" @click.prevent="logOut" v-if="isLoggedIn">Log out</a>
        <router-link v-else to="/register">Register</router-link>    
    </nav>

</template>

<script setup>
    import axios from 'axios';
    import { onMounted, ref } from 'vue';
    import { useRouter } from 'vue-router';

    const router = useRouter();
    const loggedInUser = ref(null);
    const isLoggedIn = ref(false);

    onMounted(()=>{
        loggedInUser.value = JSON.parse(localStorage.getItem('loggedUser'));

        if(loggedInUser.value != null) {
            isLoggedIn.value = true;
        }
        else {
            isLoggedIn.value = false;
        }  
    })
    
    function logOut()
    {
        localStorage.removeItem('loggedUser');
        localStorage.removeItem('jsonWebToken');
        localStorage.removeItem('cart');
        loggedInUser.value = null;
        isLoggedIn.value = false;
        router.push('/').then(() => router.go(0));
    }
</script>

<style scoped>
    .div_style {
        width: 100%;
        padding: 32px 2%;
        background-color: #333;
        color: white;
        text-align: right;
        box-sizing: border-box;
        position: absolute;
        top: 0;
        left: 0;
        z-index: 1000;
    }

    a:hover {
        cursor: pointer;
    }
</style>