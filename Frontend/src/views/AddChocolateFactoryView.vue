<script setup>
import { ref } from 'vue';
import AddChocolateFactory from '../components/AddChocolateFactory.vue';
import Header from '../components/Header.vue';
import Map from '../components/Map.vue';

const selectedCoordinates = ref([0,0]);
const selectedAdress = ref({city:"", country:"", street:"", streetNum:"",housenumber:"",postcode:0});

const handleLocationSelected = (clickedCoordinate) => {
  selectedCoordinates.value = clickedCoordinate;
};
const handleAdressSelected = (addressDetails) => {
    selectedAdress.value = addressDetails;
};

</script>

<template>
    <main>
        <Header/>
        <Map @location-selected="handleLocationSelected" @adress-selected="handleAdressSelected" />
        <AddChocolateFactory :coordinates="selectedCoordinates" :adress="selectedAdress" />
    </main>
</template>