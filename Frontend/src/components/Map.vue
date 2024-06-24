<template>
  <div>
    <div ref="map" class="map"></div>
    <div class="coordinates">
      <p v-if="selectedCoordinates">
        Selected coordinates: {{ selectedCoordinates }}
      </p>
      <p v-else>
        Click on the map to select factory location.
      </p>
    </div>
  </div>
</template>

<script>
import 'ol/ol.css';
import Map from 'ol/Map';
import View from 'ol/View';
import TileLayer from 'ol/layer/Tile';
import OSM from 'ol/source/OSM';
import axios from 'axios';
import { transform } from 'ol/proj';
import Feature from 'ol/Feature';
import Point from 'ol/geom/Point';
import { Vector as VectorLayer } from 'ol/layer';
import { Vector as VectorSource } from 'ol/source';
import Style from 'ol/style/Style';
import Icon from 'ol/style/Icon';
import pinImage from '../../public/Images/marker.png'; // Adjust the path as per your project structure

export default {
  name: 'MapComponent',
  data() {
    return {
      map: null,
      selectedCoordinates: null,
      addressDetails: null,
      apiKey: '31e961871f4a4635aa81b3f35f43971a',
      vectorLayer: null,
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

      // Create a vector layer for markers
      this.vectorLayer = new VectorLayer({
        source: new VectorSource()
      });
      this.map.addLayer(this.vectorLayer);

      this.map.on('click', this.onMapClick);
    },
    async onMapClick(event) {
      const clickedCoordinate = event.coordinate;

      // Clear existing markers
      this.vectorLayer.getSource().clear();

      // Create a marker feature
      const marker = new Feature({
        geometry: new Point(clickedCoordinate)
      });

      // Define a custom icon style for the marker (pin icon)
      const markerStyle = new Style({
        image: new Icon({
          anchor: [0.5, 1],
          anchorXUnits: 'fraction',
          anchorYUnits: 'fraction',
          src: pinImage, // Use the imported pin image
          scale: 0.05 // Adjust the scale of the marker image
        })
      });

      // Set the custom style to the marker
      marker.setStyle(markerStyle);

      // Add the marker to the vector layer
      this.vectorLayer.getSource().addFeature(marker);

      // Transform clicked coordinate to EPSG:4326 (WGS 84)
      const coordinates4326 = transform(clickedCoordinate, 'EPSG:3857', 'EPSG:4326');
      const lon = coordinates4326[0];
      const lat = coordinates4326[1];

      try {
        const response = await axios.get(`https://api.geoapify.com/v1/geocode/reverse?lat=${lat}&lon=${lon}&apiKey=${this.apiKey}`);

        if (response.data.features.length > 0) {
          const feature = response.data.features[0];
          const { city, country, street, housenumber, postcode } = feature.properties;
          this.addressDetails = {
            city,
            country,
            street,
            housenumber,
            postcode
          };

          alert(`Location saved: ${city}, ${country}, ${street}, ${housenumber}, ${postcode}`);
          this.$emit('address-selected', this.addressDetails);

        } else {
          console.error('No results found for the coordinates.');
          alert('No results found for the coordinates.');
        }
      } catch (error) {
        console.error('Error fetching address details:', error);
        alert('Error fetching address details.');
      }

      this.selectedCoordinates = clickedCoordinate;
      this.$emit('location-selected', clickedCoordinate);
    },
    saveFactoryLocation() {
      // Example function to simulate saving the location to a database
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
</script>

<style scoped>
.map {
  height: 400px;
}
.coordinates {
  margin-top: 10px;
}
</style>
