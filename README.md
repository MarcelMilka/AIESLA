# Introduction
This wiki describes the production-ready "signed out" section. While the app is still in the development, this section includes fully implemented and tested features.

AIESLA (pron. "aisla") is a mnemonic acronym for the Norwegian phrase "Alt I En Språk Læring Applikasjon" (Bokmål), meaning "All-in-One Language Learning App" in English.

The application was created by [Marcel Milka](https://www.linkedin.com/in/marcel-milka/) (me) after testing various learning applications over the past five years.
Initially focused on language learning, the project has evolved into a broader goal of creating the ultimate for creating and managing custom learning resources as well as supporting long-term knowledge retention.
# Features
**Authentication:**
After entering the app for the first time, the user is automatically navigated to the main section (bypassing the sign-in/sign-up processes) to allow immediate testing of core features (with limitations).

The `signedOut` route consists of two sub-routes:

- `signIn`: for logging in and password recovery
- `signUp`: for creating a new account

Both `signIn` and `signUp` are supported by an implementation of `AuthenticationManager` which manages various processes of authentication and conveys information about them to the relevant screen-related `ViewModels` and the `applicationScaffold` via `StateFlow`.
# Architecture
**Model-View-ViewModel (MVVM)**:
Each screen with minimal business logic has its own `ViewModel`, following the MVVM pattern.

**Modularization:** The project is fully modularized, with each module containing a specific type of logic.

- `:applicationScaffold` – Contains the main `Scaffold` from Compose M3, managing navigation between the `signedIn`, `signedOut`, and `unsuccessfulInitialization` routes.
- `:authentication` – Handles authentication-related logic.
- `:dataStore` – Contains all code related to Jetpack DataStore.
- `:firebase` – Contains Firebase-related logic and configurations.
- `:roomLocalDatabase` – Contains code related to local storage using Room.
- `:navigation` – Provides type-safe navigation using sealed classes like `Navigation`, serving as a Single Source of Truth (SSOT) for route definitions.
- `:networkConnectivity` – Detects and manages network connectivity state.
- `:routeSignedIn` – Contains screens, logic, and supporting classes used within the `signedIn` route.
- `:routeSignedOut` – Contains screens, logic, and supporting classes used within the `signedOut` route.
- `:routeUnsuccessfulInitialization` – Displays a fallback screen if initialization fails.
- `:sharedUi` – Hosts reusable UI components, fonts, colors, and string resources.

<!-- Each module contains its own README with documentation. -->
# Technologies
- Kotlin
- Jetpack Compose M3
- Compose Type-safe navigation
- Kotlin Coroutines
- Kotlin Flow
- Firebase Authentication
- Room
- DataStore (leveraging KotlinX Serialization and Json)
- Hilt
