<template>
<label style="color: red">{{ errorMessage }}</label>
<div v-if="loggedInUser" class="user-info">
    <div class="content">
        <h1>User Information</h1>
        <div v-if="dataLoaded" class="user-details">
            <p><strong>Username:</strong>
                <input v-if="usernameEditable" type="text" v-model="loggedInUser.username" :size="loggedInUser.username.length"></input>
                <label v-else>{{ loggedInUser.username }}</label>
                <button v-if="!usernameEditable" class="btn" v-on:click="changeUsernameEditable(false)">
                    <svg xmlns="http://www.w3.org/2000/svg" width="21" height="21" fill="currentColor" class="bi bi-pen" viewBox="0 0 16 16">
                        <path d="m13.498.795.149-.149a1.207 1.207 0 1 1 1.707 1.708l-.149.148a1.5 1.5 0 0 1-.059 2.059L4.854 14.854a.5.5 0 0 1-.233.131l-4 1a.5.5 0 0 1-.606-.606l1-4a.5.5 0 0 1 .131-.232l9.642-9.642a.5.5 0 0 0-.642.056L6.854 4.854a.5.5 0 1 1-.708-.708L9.44.854A1.5 1.5 0 0 1 11.5.796a1.5 1.5 0 0 1 1.998-.001m-.644.766a.5.5 0 0 0-.707 0L1.95 11.756l-.764 3.057 3.057-.764L14.44 3.854a.5.5 0 0 0 0-.708z"/>
                    </svg>
                </button>
                <button class="btn" v-on:click="updateUser()" v-if="usernameEditable">
                    <svg xmlns="http://www.w3.org/2000/svg" width="32" height="32" fill="currentColor" class="bi bi-check" viewBox="0 0 16 16">
                        <path d="M10.97 4.97a.75.75 0 0 1 1.07 1.05l-3.99 4.99a.75.75 0 0 1-1.08.02L4.324 8.384a.75.75 0 1 1 1.06-1.06l2.094 2.093 3.473-4.425z"/>
                    </svg>
                </button>
                <button class="btn" v-on:click="changeUsernameEditable(true)" v-if="usernameEditable">
                    <svg xmlns="http://www.w3.org/2000/svg" width="32" height="32" fill="currentColor" class="bi bi-x" viewBox="0 0 16 16">
                        <path d="M4.646 4.646a.5.5 0 0 1 .708 0L8 7.293l2.646-2.647a.5.5 0 0 1 .708.708L8.707 8l2.647 2.646a.5.5 0 0 1-.708.708L8 8.707l-2.646 2.647a.5.5 0 0 1-.708-.708L7.293 8 4.646 5.354a.5.5 0 0 1 0-.708"/>
                    </svg>
                </button>
            </p>

            <p><strong>Name:</strong>
                <input v-if="nameEditable" type="text" v-model="loggedInUser.name" :size="loggedInUser.name.length"></input>
                <label v-else>{{ loggedInUser.name }}</label>
                <button v-if="!nameEditable" class="btn" v-on:click="changeNameEditable(false)">
                    <svg xmlns="http://www.w3.org/2000/svg" width="21" height="21" fill="currentColor" class="bi bi-pen" viewBox="0 0 16 16">
                        <path d="m13.498.795.149-.149a1.207 1.207 0 1 1 1.707 1.708l-.149.148a1.5 1.5 0 0 1-.059 2.059L4.854 14.854a.5.5 0 0 1-.233.131l-4 1a.5.5 0 0 1-.606-.606l1-4a.5.5 0 0 1 .131-.232l9.642-9.642a.5.5 0 0 0-.642.056L6.854 4.854a.5.5 0 1 1-.708-.708L9.44.854A1.5 1.5 0 0 1 11.5.796a1.5 1.5 0 0 1 1.998-.001m-.644.766a.5.5 0 0 0-.707 0L1.95 11.756l-.764 3.057 3.057-.764L14.44 3.854a.5.5 0 0 0 0-.708z"/>
                    </svg>
                </button>
                <button class="btn" v-on:click="updateUser()" v-if="nameEditable">
                    <svg xmlns="http://www.w3.org/2000/svg" width="32" height="32" fill="currentColor" class="bi bi-check" viewBox="0 0 16 16">
                        <path d="M10.97 4.97a.75.75 0 0 1 1.07 1.05l-3.99 4.99a.75.75 0 0 1-1.08.02L4.324 8.384a.75.75 0 1 1 1.06-1.06l2.094 2.093 3.473-4.425z"/>
                    </svg>
                </button>
                <button class="btn" v-on:click="changeNameEditable(true)" v-if="nameEditable">
                    <svg xmlns="http://www.w3.org/2000/svg" width="32" height="32" fill="currentColor" class="bi bi-x" viewBox="0 0 16 16">
                        <path d="M4.646 4.646a.5.5 0 0 1 .708 0L8 7.293l2.646-2.647a.5.5 0 0 1 .708.708L8.707 8l2.647 2.646a.5.5 0 0 1-.708.708L8 8.707l-2.646 2.647a.5.5 0 0 1-.708-.708L7.293 8 4.646 5.354a.5.5 0 0 1 0-.708"/>
                    </svg>
                </button>
            </p>

            <p><strong>Surname:</strong>
                <input v-if="surnameEditable" type="text" v-model="loggedInUser.surname" :size="loggedInUser.surname.length"></input>
                <label v-else>{{ loggedInUser.surname }}</label>
                <button v-if="!surnameEditable" class="btn" v-on:click="changeSurnameEditable(false)">
                    <svg xmlns="http://www.w3.org/2000/svg" width="21" height="21" fill="currentColor" class="bi bi-pen" viewBox="0 0 16 16">
                        <path d="m13.498.795.149-.149a1.207 1.207 0 1 1 1.707 1.708l-.149.148a1.5 1.5 0 0 1-.059 2.059L4.854 14.854a.5.5 0 0 1-.233.131l-4 1a.5.5 0 0 1-.606-.606l1-4a.5.5 0 0 1 .131-.232l9.642-9.642a.5.5 0 0 0-.642.056L6.854 4.854a.5.5 0 1 1-.708-.708L9.44.854A1.5 1.5 0 0 1 11.5.796a1.5 1.5 0 0 1 1.998-.001m-.644.766a.5.5 0 0 0-.707 0L1.95 11.756l-.764 3.057 3.057-.764L14.44 3.854a.5.5 0 0 0 0-.708z"/>
                    </svg>
                </button>
                <button class="btn" v-on:click="updateUser()" v-if="surnameEditable">
                    <svg xmlns="http://www.w3.org/2000/svg" width="32" height="32" fill="currentColor" class="bi bi-check" viewBox="0 0 16 16">
                        <path d="M10.97 4.97a.75.75 0 0 1 1.07 1.05l-3.99 4.99a.75.75 0 0 1-1.08.02L4.324 8.384a.75.75 0 1 1 1.06-1.06l2.094 2.093 3.473-4.425z"/>
                    </svg>
                </button>
                <button class="btn" v-on:click="changeSurnameEditable(true)" v-if="surnameEditable">
                    <svg xmlns="http://www.w3.org/2000/svg" width="32" height="32" fill="currentColor" class="bi bi-x" viewBox="0 0 16 16">
                        <path d="M4.646 4.646a.5.5 0 0 1 .708 0L8 7.293l2.646-2.647a.5.5 0 0 1 .708.708L8.707 8l2.647 2.646a.5.5 0 0 1-.708.708L8 8.707l-2.646 2.647a.5.5 0 0 1-.708-.708L7.293 8 4.646 5.354a.5.5 0 0 1 0-.708"/>
                    </svg>
                </button>
            </p>

            <p><strong>Gender:</strong>
                <select v-if="genderEditable" v-model="loggedInUser.gender">
                        <option>MALE</option>
                        <option>FEMALE</option>
                </select>
                <label v-else>{{ loggedInUser.gender }}</label>
                <button v-if="!genderEditable" class="btn" v-on:click="changeGenderEditable(false)">
                    <svg xmlns="http://www.w3.org/2000/svg" width="21" height="21" fill="currentColor" class="bi bi-pen" viewBox="0 0 16 16">
                        <path d="m13.498.795.149-.149a1.207 1.207 0 1 1 1.707 1.708l-.149.148a1.5 1.5 0 0 1-.059 2.059L4.854 14.854a.5.5 0 0 1-.233.131l-4 1a.5.5 0 0 1-.606-.606l1-4a.5.5 0 0 1 .131-.232l9.642-9.642a.5.5 0 0 0-.642.056L6.854 4.854a.5.5 0 1 1-.708-.708L9.44.854A1.5 1.5 0 0 1 11.5.796a1.5 1.5 0 0 1 1.998-.001m-.644.766a.5.5 0 0 0-.707 0L1.95 11.756l-.764 3.057 3.057-.764L14.44 3.854a.5.5 0 0 0 0-.708z"/>
                    </svg>
                </button>
                <button class="btn" v-on:click="updateUser()" v-if="genderEditable">
                    <svg xmlns="http://www.w3.org/2000/svg" width="32" height="32" fill="currentColor" class="bi bi-check" viewBox="0 0 16 16">
                        <path d="M10.97 4.97a.75.75 0 0 1 1.07 1.05l-3.99 4.99a.75.75 0 0 1-1.08.02L4.324 8.384a.75.75 0 1 1 1.06-1.06l2.094 2.093 3.473-4.425z"/>
                    </svg>
                </button>
                <button class="btn" v-on:click="changeGenderEditable(true)" v-if="genderEditable">
                    <svg xmlns="http://www.w3.org/2000/svg" width="32" height="32" fill="currentColor" class="bi bi-x" viewBox="0 0 16 16">
                        <path d="M4.646 4.646a.5.5 0 0 1 .708 0L8 7.293l2.646-2.647a.5.5 0 0 1 .708.708L8.707 8l2.647 2.646a.5.5 0 0 1-.708.708L8 8.707l-2.646 2.647a.5.5 0 0 1-.708-.708L7.293 8 4.646 5.354a.5.5 0 0 1 0-.708"/>
                    </svg>
                </button>
            </p>

            <p><strong>Birthday:</strong>
                <input v-if="dateEditable" type="date" v-model="loggedInUser.birthday"></input>
                <label v-else>{{ convertToTimestamp(loggedInUser.birthday,true) }}</label>
                <button v-if="!dateEditable" class="btn" v-on:click="changeDateEditable(false)">
                    <svg xmlns="http://www.w3.org/2000/svg" width="21" height="21" fill="currentColor" class="bi bi-pen" viewBox="0 0 16 16">
                        <path d="m13.498.795.149-.149a1.207 1.207 0 1 1 1.707 1.708l-.149.148a1.5 1.5 0 0 1-.059 2.059L4.854 14.854a.5.5 0 0 1-.233.131l-4 1a.5.5 0 0 1-.606-.606l1-4a.5.5 0 0 1 .131-.232l9.642-9.642a.5.5 0 0 0-.642.056L6.854 4.854a.5.5 0 1 1-.708-.708L9.44.854A1.5 1.5 0 0 1 11.5.796a1.5 1.5 0 0 1 1.998-.001m-.644.766a.5.5 0 0 0-.707 0L1.95 11.756l-.764 3.057 3.057-.764L14.44 3.854a.5.5 0 0 0 0-.708z"/>
                    </svg>
                </button>
                <button class="btn" v-on:click="updateUser()" v-if="dateEditable">
                    <svg xmlns="http://www.w3.org/2000/svg" width="32" height="32" fill="currentColor" class="bi bi-check" viewBox="0 0 16 16">
                        <path d="M10.97 4.97a.75.75 0 0 1 1.07 1.05l-3.99 4.99a.75.75 0 0 1-1.08.02L4.324 8.384a.75.75 0 1 1 1.06-1.06l2.094 2.093 3.473-4.425z"/>
                    </svg>
                </button>
                <button class="btn" v-on:click="changeDateEditable(true)" v-if="dateEditable">
                    <svg xmlns="http://www.w3.org/2000/svg" width="32" height="32" fill="currentColor" class="bi bi-x" viewBox="0 0 16 16">
                        <path d="M4.646 4.646a.5.5 0 0 1 .708 0L8 7.293l2.646-2.647a.5.5 0 0 1 .708.708L8.707 8l2.647 2.646a.5.5 0 0 1-.708.708L8 8.707l-2.646 2.647a.5.5 0 0 1-.708-.708L7.293 8 4.646 5.354a.5.5 0 0 1 0-.708"/>
                    </svg>
                </button>
            </p>

            <p><strong>Role:</strong>{{ loggedInUser.role }} 
            </p>       
        </div>
    </div>
</div>
<div v-else>
    <h1 style="color: red;">Not logged in</h1>
</div>
</template>
  
<script setup>
import axios from 'axios';
import { onMounted, ref } from 'vue';
import { format } from 'date-fns';
import {useRouter} from 'vue-router';

const router = useRouter();
const errorMessage = ref('');

const loggedInUser = ref(null);
const loggedInUserConst = ref(null);
const dataLoaded = ref(false);

const usernameEditable = ref(false);
const nameEditable = ref(false);
const surnameEditable = ref(false);
const genderEditable = ref(false);
const dateEditable = ref(false);

onMounted(()=>{
    loggedInUser.value = JSON.parse(localStorage.getItem('loggedUser'));
    loggedInUserConst.value = JSON.parse(localStorage.getItem('loggedUser'));
    dataLoaded.value = true;    
})  

function changeNameEditable(xPressed) {
    errorMessage.value = '';
    nameEditable.value = !nameEditable.value;

    if(xPressed){
        loggedInUser.value.name = loggedInUserConst.value.name;
    }
}

function changeUsernameEditable(xPressed) {
    errorMessage.value = '';
    usernameEditable.value = !usernameEditable.value;

    if(xPressed){
        loggedInUser.value.username = loggedInUserConst.value.username;
    }
}

function changeSurnameEditable(xPressed) {
    errorMessage.value = '';
    surnameEditable.value = !surnameEditable.value;

    if(xPressed){
        loggedInUser.value.surname = loggedInUserConst.value.surname;
    }
}

function changeGenderEditable(xPressed) {
    errorMessage.value = '';
    genderEditable.value = !genderEditable.value;

    if(xPressed){
        loggedInUser.value.gender = loggedInUserConst.value.gender;
    }
}

function changeDateEditable(xPressed) {
    errorMessage.value = '';
    dateEditable.value = !dateEditable.value;

    if(xPressed){
        loggedInUser.value.birthday = loggedInUserConst.value.birthday;
    }
}

function updateUser() {
    if(loggedInUser.value.username === '' || loggedInUser.value.name === '' || loggedInUser.value.surname === '' || loggedInUser.value.gender == '' || loggedInUser.value.birthday == '') {
        errorMessage.value = 'You can not edit set field to nothing!';
    }
    else
    {
        errorMessage.value = '';
        axios.post(`http://localhost:8080/ChoccolateAppREST/rest/updateUser`, { id: loggedInUser.value.id, username: loggedInUser.value.username, password: '', name: loggedInUser.value.name, surname: loggedInUser.value.surname, gender: loggedInUser.value.gender, birthday: convertToTimestamp(loggedInUser.value.birthday, false), role: loggedInUser.value.role}, { 
         headers: {
            'Authorization': `Bearer ${localStorage.getItem('jsonWebToken')}`
        }
        })
        .then(response=>{
            if(response.data)
            {
                localStorage.removeItem('loggedUser');
                localStorage.setItem('loggedUser', JSON.stringify(response.data));
                router.go(0);   

                nameEditable.value = false; 
                usernameEditable.value = false;
                surnameEditable.value = false;
                genderEditable.value = false;
                dateEditable.value = false;
            }   
        })
    }
}

function convertToTimestamp(dateString, forShow) {
    if (!(typeof dateString === 'number') && !forShow) {
        const parts = dateString.split(' ');

        if (parts.length !== 6) {
            return dateString;
        }

        const [dayOfWeek, month, day, time, timezone, year] = parts;
        
        const dateWithoutTimezone = `${dayOfWeek} ${month} ${day} ${time} ${year}`;
        
        const parsedDate = new Date(dateWithoutTimezone);
        
        if (isNaN(parsedDate.getTime())) {
            throw new Error('Invalid date string');
        }

        return parsedDate;
    } else if(!(typeof dateString === 'number') && forShow) {
        const parts = dateString.split(' ');

        if (parts.length !== 6) {
            return dateString;
        }

        const [dayOfWeek, month, day, time, timezone, year] = parts;
        
        const dateWithoutTimezone = `${dayOfWeek} ${month} ${day} ${time} ${year}`;
        
        const parsedDate = new Date(dateWithoutTimezone);
        
        if (isNaN(parsedDate.getTime())) {
            throw new Error('Invalid date string');
        }

        return format(parsedDate, 'dd.MM.yyyy');
    } 
    else if(forShow)
    {
        return format(new Date(dateString), 'dd.MM.yyyy');
    }
    else
    {
        return dateString;
    }
}

</script>
  
<style scoped>
.user-info {
font-family: Arial, sans-serif;
background: #dfd1c2;
color: #333;
padding: 20px;
border-radius: 10px; 
}

.navbar {
display: flex;
justify-content: space-between;
background-color: #8B4513; 
padding: 10px 20px;
}

.navbar-brand {
font-size: 1.5em;
color: white;
}

.navbar-menu {
display: flex;
gap: 15px;
}

.navbar-item {
color: white;
text-decoration: none;
}

.navbar-item:hover {
text-decoration: underline;
}

.content {
margin-top: 20px;
}

.user-details {
background: white;
padding: 15px;
border-radius: 8px;
box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.user-details p {
margin: 10px 0;
}
</style>
  