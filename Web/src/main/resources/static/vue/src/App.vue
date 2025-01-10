// App.vue
<template>
  <div>
    <header class="header">
      <div class="container">
        <h1 class="logo">ParkEasy - Parking Interface</h1>
      </div>
    </header>

    <section class="parking-interface">
      <div class="container">
        <h2>Parking Management Dashboard</h2>
        <p>Welcome to the parking management system.</p>
        <select v-model="selectedLotId" @change="loadSpotsForLot">
          <option v-for="lot in lots" :key="lot.id" :value="lot.id">
            {{ lot.name }}
          </option>
        </select>

        <div class="parking-layout">
          <div v-for="(column, colIndex) in parkingColumns" 
               :key="colIndex" 
               class="parking-column">
            <div v-for="spot in column" 
                 :key="spot.id" 
                 class="parking-slot"
                 :class="{
                   'available': !spot.isOccupied && !spot.isReserved,
                   'occupied': spot.isOccupied,
                   'reserved': spot.isReserved
                 }"
                 @click="spot.isOccupied ? null : reserveSpot(spot)">
              {{ spot.spotNumber }}
              <span>{{ getSpotStatus(spot) }}</span>
            </div>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import axios from 'axios'

// Configuration d'Axios avec l'authentification de base
const api = axios.create({
  headers: {
    'Content-Type': 'application/json'
  },
  auth: {
    username: 'user',
    password: 'password'
  }
})

const lots = ref([])
const selectedLotId = ref(null)
const spots = ref([])
const spotsPerColumn = 6

const parkingColumns = computed(() => {
  const columns = []
  const spotsArray = [...spots.value]
  
  while (spotsArray.length > 0) {
    columns.push(spotsArray.splice(0, spotsPerColumn))
  }
  
  return columns
})

const getSpotStatus = (spot) => {
  if (spot.isOccupied) return 'Occupied'
  if (spot.isReserved) return 'Reserved'
  return 'Available'
}

const loadLots = async () => {
  try {
    const response = await api.get('/api/parking/lots')
    lots.value = response.data
    if (lots.value.length > 0) {
      selectedLotId.value = lots.value[0].id
      await loadSpotsForLot()
    }
  } catch (error) {
    console.error('Error loading parking lots:', error)
  }
}

const loadSpotsForLot = async () => {
  if (!selectedLotId.value) return
  
  try {
    const response = await api.get(`/api/parking/${selectedLotId.value}/spots`)
    spots.value = response.data.map(spot => ({
      ...spot,
      isReserved: false
    }))
  } catch (error) {
    console.error('Error loading parking spots:', error)
  }
}

const reserveSpot = async (spot) => {
  try {
    const response = await api.put(`/api/parking/spots/${spot.id}/reserve`)
    if (response.status === 200) {
      spot.isReserved = true
    }
  } catch (error) {
    alert(`Error: ${error.response?.data || 'Failed to reserve spot'}`)
  }
}

onMounted(loadLots)
</script>

<style scoped>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: Arial, sans-serif;
  background-color: #f0f0f0;
  color: #333;
  line-height: 1.6;
}

.container {
  max-width: 1100px;
  margin: 0 auto;
  padding: 20px;
}

.header {
  background: #003366;
  color: #fff;
  padding: 20px 0;
  display: flex;
  justify-content: center;
  align-items: center;
}

.logo {
  font-size: 24px;
  font-weight: bold;
  text-align: center;
}

.parking-interface {
  text-align: center;
  margin-top: 20px;
}

.parking-layout {
  display: flex;
  justify-content: space-around;
  gap: 20px;
  margin-top: 20px;
}

.parking-column {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 10px;
}

.parking-slot {
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
  width: 80px;
  height: 80px;
  border-radius: 5px;
  font-weight: bold;
  color: #fff;
  font-size: 14px;
  cursor: pointer;
}

.parking-slot.available {
  background-color: #4CAF50;
}

.parking-slot.reserved {
  background-color: #FFC107;
}

.parking-slot.occupied {
  background-color: #F44336;
  cursor: not-allowed;
}

select {
  padding: 10px;
  margin: 20px 0;
  width: 200px;
  border-radius: 5px;
  border: 1px solid #ccc;
}
</style>