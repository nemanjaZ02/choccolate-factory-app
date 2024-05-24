<template>
    <div>
        <div id="divSearchAndHeader">
            <h1 id="FactoriesHeader">FACTORIES</h1>
            <form id="searchForm">
                <label for="search">Naziv Fabrike: </label>
                <input id="search" type="search" pattern=".*\S.*" required>
                <label for="search">Naizv Cokolade: </label>
                <input id="search" type="search" pattern=".*\S.*" required>
                <label for="search">Lokacija: </label>
                <input id="search" type="search" pattern=".*\S.*" required>
                <label for="search">Prosecna Ocena: </label>
                <input id="search" type="search" pattern=".*\S.*" required>
                <span class="caret"></span>
            </form>
        </div>
        <div>
            <table id="Factories">
                    <tr v-for="f in factories" v-on:click="showDetails(f)">
                        <td>
                            <img :src="f.logo" alt="Logo" width="250" height="250">
                        </td>
                        <td id="FactoryName">{{ f.name }}</td>
                    </tr>
            </table>
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
    loadProducts();
})

function loadProducts() {
    axios.get('http://localhost:8080/ChoccolateAppREST/rest/ChocolateFactoryService/getAll').then(response => {
            factories.value = response.data
        });
}

function showDetails(factory) {
    router.push({name: 'factoryDetails', params: { factoryId: factory.id }});
}
</script>

<style>
#FactoriesHeader {
    text-align: center;
    font-size: 100px;
    font-family: fantasy;
}

#Factories { 
    margin: auto;
    border-collapse: collapse;
}

#Factories tr:hover{
    background-color: brown;
}

#FactoryName {
    font-size: 42px;
    font-family: fantasy;
    vertical-align: text-top;
    padding-left: 30px;
}

#searchForm {
    text-align: center;
    margin: 20px;
}

input[type="search"] {
  background-color: #dfd1c2;
}
</style>
