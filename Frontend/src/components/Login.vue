<template>
   
    <h1>Login</h1>
    <form @submit.prevent="login">
      <table>
        <tr>
            <td>Username:</td>
            <td> <input type="text" id="username" v-model="username" required> </td>
        </tr>
        <tr>
            <td>Password:</td>
            <td><input type="password" id="password" v-model="password" required></td>
        </tr>
      
        <tr>
            <button type="submit">Login</button>
        </tr>
        <br>
      </table>
      <p id="success">{{ success }}</p>
      <p id="error">{{ errorMessage }}</p>
    </form>
    
</template>

<script setup>
import axios from 'axios';
import { ref } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter();

const username = ref('');
const password = ref('');

const errorMessage = ref('');
const success = ref('');

const login = () => {
    axios.post('http://localhost:8080/ChoccolateAppREST/rest/login', { username: username.value, password: password.value })
        .then(response => {
            localStorage.setItem('loggedUser', JSON.stringify(response.data));
            success.value = 'Korisnik je uspesno prijavljen.';
            errorMessage.value = '';
            router.push('/');
        })
        .catch(error => {
            success.value = '';
            errorMessage.value = 'Pogresno korisnicko ime ili lozinka.';
            console.error(error);
        });
}

</script>
