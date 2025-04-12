# ⚽ TeaApp

**TeaApp** is a modern Android application built with **Jetpack Compose** that displays football competitions by area.It features clean architecture, offline caching, real-time network tracking, and test coverage.

---
## 🛠️ Tech Stack

- **Language:** Kotlin
- **UI Toolkit:** Jetpack Compose
- **Architecture:** MVI (Model–View–Intent) + Clean Architecture
- **Dependency Injection:** Hilt
- **Networking:** Retrofit, OkHttp
- **Local Storage:** Room (SQLite)
- **Asynchronous Programming:** Kotlin Coroutines & Flow
- **Image Loading:** Coil (with SVG support)
- **Testing:** JUnit, Coroutine Test, Hilt Test, Truth


---
## 📂 Project Structure

- `data/`
  - `local/` – Room database, entities, and DAO
  - `remote/` – Retrofit API interfaces and network models
  - `repository/` – Repository implementations
  - `util/` – Utility classes (e.g., AuthInterceptor, NetworkChangeReceiver)

- `domain/` – Repository interfaces and data models

- `presentation/`
  - `home/` – Home screen UI and ViewModel (MVI pattern)
  - `details/` – Competition details screen
  - `navigation/` – Navigation components (NavHost, routes)

- `di/` – Hilt dependency injection modules

- `MainActivity.kt` – Entry point of the app with Compose setup
- `MyApplication.kt` – Hilt-enabled Application class
---
## 🚀 Features

- ✅ View football competitions by area
- ✅ Offline experience with Room DB
- ✅ Real-time internet connection updates
- ✅ Supports mobile & tablet via adaptive layout
- ✅ Integrated unit & instrumentation tests
---

## 🔐 API Token Setup
To use the Football API, set up your token securely.

1. Add the token in `gradle.properties`:
```properties
API_TOKEN=your_api_key_here




