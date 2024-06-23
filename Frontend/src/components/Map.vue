<template>
    <div>
      <div ref="map" class="map"></div>
      <div class="coordinates">
        <p v-if="selectedCoordinates">
          Selected coordinates: {{ selectedCoordinates }}
          <button @click="saveFactoryLocation">Save Location</button>
        </p>
        <p v-else>
        Click on the map to select factory location.
      </p>
      
      </div>
    </div>
  </template>
  
  <script >
  import 'ol/ol.css';
  import Map from 'ol/Map';
  import View from 'ol/View';
  import TileLayer from 'ol/layer/Tile';
  import OSM from 'ol/source/OSM';
  import axios from 'axios';
  import { transform } from 'ol/proj';
  
  export default {
    name: 'MapComponent',
    data() {
      return {
        map: null,
        selectedCoordinates: null,
        addressDetails: null,
        apiKey: '31e961871f4a4635aa81b3f35f43971a'
      };
    },
    mounted() {
      this.initializeMap();
    },
    methods: {
      initializeMap() {
        this.map = new Map({
          target: this.$refs.map,
          layers: [
            new TileLayer({
              source: new OSM()
            })
          ],
          view: new View({
            center: [0, 0],
            zoom: 5
          })
        });
  
     
        this.map.on('click', this.onMapClick);
      },
      async onMapClick(event) {
        const clickedCoordinate = event.coordinate;
        
       this.selectedCoordinates = event.coordinate
      
        this.$emit('location-selected', clickedCoordinate);
       
        try {
         
          const coordinates4326 = transform3857To4326(clickedCoordinate);
          const lon = coordinates4326[0];
          const lat = coordinates4326[1];
      
        const response = await axios.get(`https://api.geoapify.com/v1/geocode/reverse?lat=${lat}&lon=${lon}&apiKey=31e961871f4a4635aa81b3f35f43971a`);

        if (response.data.features.length > 0) {
          const feature = response.data.features[0];
          const { city, country, street, housenumber,postcode } = feature.properties;
          console.log('City:', city);
          console.log('Country:', country);
          console.log('Street:', street);
          console.log('HouseNumber:', housenumber);
          console.log('Postcode:', postcode);
    
          this.addressDetails = {
            city,
            country,
            street,
            housenumber,
            postcode
        };
          console.log('Address details:', this.addressDetails);
          alert(` location saved : ${city}, ${country}, ${street}, ${housenumber},${postcode}`);
          this.$emit('adress-selected', this.addressDetails);

        } else {
          console.error('No results found for the coordinates.');
          alert(` No results found for the coordinates.`);
        }
      } catch (error) {
        console.error('Error fetching address details:', error);
        alert(`Error fetching address details:`);
      }
    
        // Optionally, you can show a marker at the clicked location
        // For example, using a vector layer and a feature with a point geometry
        // Uncomment the following lines if you want to show a marker
        /*
        const marker = new Feature({
          geometry: new Point(clickedCoordinate)
        });
        const vectorSource = new VectorSource({
          features: [marker]
        });
        const vectorLayer = new VectorLayer({
          source: vectorSource
        });
        this.map.addLayer(vectorLayer);
        */
  
        // You can also center the map on the clicked location if needed
        // this.map.getView().setCenter(clickedCoordinate);
      },
      
      saveFactoryLocation() {
        // Example function to simulate saving the location to a database
        // Replace this with your actual logic to save the coordinates
        alert(`Factory location saved at coordinates: ${this.selectedCoordinates}`);
        // Here you could call an API to save the coordinates to your backend
        // Example: axios.post('/api/save-location', { coordinates: this.selectedCoordinates });
      }
    },
    beforeDestroy() {
      if (this.map) {
        this.map.un('click', this.onMapClick);
      }
    }
  };
  function transform3857To4326(coordinates3857) {
  return transform(coordinates3857, 'EPSG:3857', 'EPSG:4326');
}
  </script>
  
  <style scoped>
  .map {
    height: 400px;
  }
  .coordinates {
    margin-top: 10px;
  }
  </style>