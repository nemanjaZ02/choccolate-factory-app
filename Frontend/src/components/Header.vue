<template>
<nav class="nav-bar">
    
    <div class="nav-content">
        <router-link :to="{ name: 'home' }" class="logo-link">
            <img src="../../public/Images/appLogo.jpg" alt="Logo" class="logo-image">
        </router-link>
        <router-link :to="{name: 'myProfileView'}" v-if="isLoggedIn" class="nav-link">{{ loggedInUser.username }}</router-link>
        <router-link v-else to="/login" class="nav-link">Login</router-link> 
 
        <label v-if="isLoggedIn && (loggedInUser.role === 'MANAGER' || loggedInUser.role === 'CUSTOMER' || loggedInUser.role==='ADMIN' || loggedInUser.role==='EMPLOYEE')" class="separator"> | </label>

        <router-link 
            v-if="isLoggedIn && ( loggedInUser.role === 'MANAGER'|| loggedInUser.role ==='EMPLOYEE')"
            :to="{ name: 'factoryDetails', params: { factoryId: loggedInUser.factoryId } }" 
            class="nav-link">
            My Factory
        </router-link> 

        <router-link 
            v-if="isLoggedIn && loggedInUser.role === 'CUSTOMER'" 
            :to="{ name: 'myCartView' }" 
            class="nav-link">
            My Cart
        </router-link>

        <router-link 
            v-if="isLoggedIn && loggedInUser.role === 'ADMIN'" 
            :to="{ name: 'allUsersView' }" 
            class="nav-link">
            Registered Users
        </router-link>

        <router-link 
            v-if="isLoggedIn && loggedInUser.role === 'MANAGER'" 
            :to="{ name: 'managerPurchases' }" 
            class="nav-link">
            My Factory Purchases
        </router-link>

        <router-link 
            v-if="isLoggedIn && loggedInUser.role === 'CUSTOMER'" 
            :to="{ name: 'customerPurchases' }" 
            class="nav-link">
            My Purchases
        </router-link>



        <a href="#" class="nav-link" @click.prevent="logOut" v-if="isLoggedIn">Log out</a>
        <router-link v-else to="/register" class="nav-link">Register</router-link>    
    </div>
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
.logo-image {
    width: 75px; 
    height: auto; 
    margin-right: 10px; 
}
.logo-link {
    margin-right: auto; 
} 
.nav-bar {
    width: 100%;
    padding: 16px 2%;
    background-color: #2C2123; /* Adjusted dark background color */
    color: #EAD9B9; /* Light cream text color */
    text-align: right;
    box-sizing: border-box;
    position: absolute;
    top: 0;
    left: 0;
    z-index: 1000;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2); /* Box shadow */
}

.nav-content {
    display: flex;
    justify-content: flex-end;
    align-items: center;
}

.nav-link {
    margin: 0 10px;
    color: #EAD9B9; /* Light cream link text color */
    text-decoration: none;
    transition: color 0.3s, transform 0.3s;
}

.nav-link:hover {
    color: #D4A058; /* Light brown hover color */
    transform: scale(1.1);
}

.separator {
    margin: 0 5px;
}

.nav-item {
    margin: 0 10px;
}
</style>