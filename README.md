# AI Study OS – Android App

A production-grade Android application for smart studying powered by AI.

## Tech Stack
- Kotlin + MVVM + Clean Architecture + Repository Pattern
- Hilt (DI), Retrofit + OkHttp (Networking), Room (Local DB)
- DataStore (Session), Coroutines + Flow, Coil (Images)
- Navigation Component, WorkManager, Material Design 3

## Setup Instructions

### Prerequisites
- Android Studio Hedgehog or later
- JDK 17
- Android SDK 34

### Steps
1. Clone or unzip this project
2. Open in Android Studio
3. In `app/build.gradle.kts` update `BASE_URL` in debug buildType to your backend URL
4. Download Inter font files and place in `app/src/main/res/font/`:
   - `inter_regular.ttf`
   - `inter_bold.ttf`
5. Add the dots indicator library to build.gradle (or remove the DotsIndicator from onboarding):
   `implementation("com.tbuonomo:dotsindicator:5.0")`
6. Add MPAndroidChart to repositories: `maven { url 'https://jitpack.io' }`
7. Sync Gradle and Run

### Backend Connection
Configure Spring Boot REST API base URL in `app/build.gradle.kts`:
- Release: `https://api.yourdomain.com/`

### API Endpoints Expected
See `data/remote/api/` for all Retrofit service interfaces.

### Architecture Overview
```
presentation/ → domain/ → data/
     ↓              ↓          ↓
  ViewModel    UseCase    Repository
  Fragment     DomainModel   RemoteSource
  Adapter      Interface     LocalSource (Room)
                             SessionManager (DataStore)
```

### Key Features Implemented
- Auth (Login/Register/ForgotPassword)
- Onboarding flow
- Subject CRUD
- Material Upload (PDF/Audio)
- AI Chat with material
- AI Notes generation and display
- Quiz generation, play, and results
- Analytics dashboard
- Revision plan with task completion
- Profile & logout
- Offline-first with Room caching
- Dark mode ready
- MVVM + Clean Architecture throughout

### Package Structure
```
com.aistudyos.app/
├── AiStudyApplication.kt
├── core/common/        (Result, UiState, extensions, constants)
├── data/remote/        (Retrofit APIs, DTOs, interceptors, mappers)
├── data/local/         (Room DB, DataStore session)
├── data/repository/    (Repository implementations)
├── data/worker/        (WorkManager worker factory)
├── domain/model/       (Clean domain models)
├── domain/repository/  (Repository interfaces)
├── domain/usecase/     (Business logic use cases)
├── presentation/       (Fragments, ViewModels, Adapters)
├── di/                 (Hilt modules)
└── navigation/         (Nav graph docs)
```
