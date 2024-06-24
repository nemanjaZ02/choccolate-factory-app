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

const decodeJWT = (token) => {
    const parts = token.split('.');

    if (parts.length !== 3) {
        throw new Error('Invalid token format');
    }

    const decodeBase64Url = (base64Url) => {
        // Replace URL-safe characters and add padding if necessary
        let base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
        // Add padding if the base64 string length is not a multiple of 4
        while (base64.length % 4) {
            base64 += '=';
        }
        return atob(base64);
    };

    const header = JSON.parse(decodeBase64Url(parts[0]));
    const payload = JSON.parse(decodeBase64Url(parts[1]));
    const signature = parts[2];

    return { header, payload, signature };
};

const login = () => {
    axios.post('http://localhost:8080/ChoccolateAppREST/rest/login', { username: username.value, password: password.value })
        .then(response => {
            let decodedToken = decodeJWT(response.data);
            let user = JSON.stringify(decodedToken.payload.user);
            localStorage.setItem('loggedUser', user);
            localStorage.setItem('jsonWebToken', JSON.stringify(response.data.jwt));
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
