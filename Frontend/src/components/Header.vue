<template>

    <nav class="div_style">
        <label v-if="isLoggedIn">{{loggedInUser.username}}</label>
        <router-link v-else to="/login">Login</router-link> 

        <label> | </label>

        <router-link :to="{name: 'factoryDetails', params: { factoryId: loggedInUser.factoryId }}" v-if="isLoggedIn && loggedInUser.role == 'MANAGER'">My Factory</router-link> 
        <label v-if="isLoggedIn && loggedInUser.role == 'MANAGER'"> | </label>

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
        loggedInUser.value = null;
        isLoggedIn.value = false;
        router.push('/');
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