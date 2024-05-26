<template>
    <div class="container">
        <h1>FACTORIES</h1>
        <div class="header">
            <form class="search-form">
                <div class="form-group">
                    <label for="search">Naziv Fabrike: </label>
                    <input type="search" pattern=".*\S.*" required>
                </div>
                <div class="form-group">
                    <label for="search">Naizv Cokolade: </label>
                    <input type="search" pattern=".*\S.*" required>
                </div>
                <div class="form-group">
                    <label for="search">Lokacija: </label>
                    <input type="search" pattern=".*\S.*" required>
                </div>
                <div class="form-group">
                    <label for="search">Prosecna Ocena: </label>
                    <input type="search" pattern=".*\S.*" required>
                </div>
            </form>
        </div>
        <div class="factories">
            <div class="factory" v-for="f in factories" v-on:click="showDetails(f)">
                <img :src="f.logo" alt="Logo" class="factory-logo">
                <div class="factory-name">{{ f.name }}</div>
            </div>
        </div>
    </div>
</template>

<script setup>
import axios from 'axios';
import { onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter();
const factories = ref([]);

onMounted(() => {
    loadFactories();
})

function loadFactories() {
    axios.get('http://localhost:8080/ChoccolateAppREST/rest/ChocolateFactoryService/getAll').then(response => {
            factories.value = response.data
        });
}

function showDetails(factory) {
    router.push({name: 'factoryDetails', params: { factoryId: factory.id }});
}
</script>

<style scoped>
.container {
    max-width: 960px;
    padding: 25px;
    font-family: Arial, sans-serif;
    background-color: #dfd1c2;
    border-radius: 8px;
    margin-top: 100px;
}

.header {
    text-align: center;
    margin-bottom: 40px;
}

.header h1 {
    font-size: 2.5em;
    color: #333;
    margin-bottom: 20px;
}

.search-form {
    display: flex;
    justify-content: space-between;
    flex-wrap: wrap;
}

.form-group {
    flex: 0 0 calc(50% - 10px);
    margin-bottom: 20px;
}

.form-group label {
    display: block;
    margin-bottom: 5px;
}

.form-group input {
    width: 100%;
    padding: 10px;
    border: 1px solid #ccc;
    border-radius: 4px;
}

.factories {
    display: flex;
    flex-wrap: wrap;
    justify-content: space-between;
}

.factory {
    width: calc(50% - 10px);
    margin-bottom: 20px;
    cursor: pointer;
    transition: background-color 0.3s ease;
}

.factory:hover {
    background-color: #f9f9f9;
}

.factory-logo {
    width: 100%;
    height: auto;
}

.factory-name {
    font-size: 1.5em;
    padding: 10px 0;
    text-align: center;
}
</style>
