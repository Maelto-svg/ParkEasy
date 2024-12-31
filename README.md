# ParkingApp - Android Parking Management System

An Android application for real-time parking spot management using Jetpack Compose UI.

## Features

- Real-time parking spot status monitoring
- Multiple parking lot support
- Color-coded spot status:
  - Green: Available
  - Yellow: Reserved
  - Red: Occupied
- Vehicle registration display
- Session-based authentication

## Technical Stack

- Kotlin
- Jetpack Compose
- Retrofit2
- OkHttp3
- MVVM Architecture
- Coroutines & Flow

## Project Structure

```
app/
├── src/main/java/com/example/parkingapp/
│   ├── MainActivity.kt           # Main UI and navigation
│   ├── ParkingApi.kt            # API interface definitions
│   ├── ParkingViewModel.kt      # Business logic and state management
│   ├── ParkingRepository.kt     # Data operations
│   ├── RetrofitClient.kt        # Network configuration
│   ├── ParkingLot.kt       # Parking lot data model
│   ├── ParkingSpot.kt      # Parking spot data model
│   ├── SpotUpdateRequest.kt # Request model for spot updates
```

## Setup

1. Clone the repository
2. Open in Android Studio
3. Update `BASE_URL` in RetrofitClient.kt
4. Run on emulator or device

## API Endpoints

- `GET /lots`: Retrieve all parking lots
- `GET /{lotId}/spots`: Get spots for specific lot
- `PUT /spots/{id}/reserve`: Reserve/update spot status

## Authentication

The app uses form-based authentication with session management. Default credentials:
- Username: user
- Password: password
