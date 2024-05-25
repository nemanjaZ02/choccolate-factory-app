<template>

    <div class="div_style">
        <label v-if="isLoggedIn">{{loggedInUser.username}}</label>
        <router-link v-else to="/login">Login</router-link> 

        <label> | </label>

        <router-link :to="{name: 'factoryDetails', params: { factoryId: loggedInUser.factoryId }}" v-if="isLoggedIn && loggedInUser.role == 'MANAGER'">My Factory</router-link> 
        <label v-if="isLoggedIn && loggedInUser.role == 'MANAGER'"> | </label>

        <a v-on:click="logOut()" v-if="isLoggedIn">Log out</a>
        <router-link v-else to="/register">Register</router-link>    
    </div>

</template>

<script setup>
    import axios from 'axios';
    import { onMounted, ref } from 'vue';
    import { useRouter } from 'vue-router';

    const router = useRouter();
    const loggedInUser = ref(null);
    const isLoggedIn = ref(false);

    onMounted(()=>{
        getLoggedInUser();
    })
    
    function getLoggedInUser()
    {
        axios.get("http://localhost:8080/ChoccolateAppREST/rest/getLoggedInUser").then(response=>{
            if(!response.data) {
                isLoggedIn.value = false;
            }
            else {
                loggedInUser.value=response.data;
                isLoggedIn.value = true;
            }   
        })
    }

    function logOut()
    {
        axios.get("http://localhost:8080/ChoccolateAppREST/rest/logOut").then(response=>{
            loggedInUser.value = null;
            isLoggedIn.value = false;
            router.push('/');
        }).catch(error=>{
            console.error(error);
        })
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
        position: fixed;
        top: 0;
        left: 0;
        z-index: 1000;
    }

    a:hover {
        cursor: pointer;
    }
</style>