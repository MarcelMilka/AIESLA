# Introduction
This wiki describes the production-ready "signed out" section. While the app is still in the development, this section includes fully implemented and tested features.

AIESLA (pron. "aisla") is a mnemonic acronym for the Norwegian phrase "Alt I En Språk Læring Applikasjon" (Bokmål), meaning "All-in-One Language Learning App" in English.

The application was created by [Marcel Milka](https://www.linkedin.com/in/marcel-milka/) (me) after testing various learning applications over the past five years.
Initially focused on language learning, the project has evolved into a broader goal of creating the ultimate for creating and managing custom learning resources as well as supporting long-term knowledge retention.

# Dictionary
**`AuthenticationManager`**
  - manages which route (signedIn/signedOut) a user should be in,
  - manages states of sign in, sign up, password recovery and verification processes. 

# Features
**Authetnication management:**
After launching the application, the implementation of `AuthenticationManager` checks whether a user is already signed in. If so, the user is automatically navigated to "RouteSignedIn", otherwise only "RouteSignedOut" is accessible.

**State manaement**: The `signInProcess`, `signUpProcess`, and `passwordRecoveryProcess` are managed using `MutableStateFlow` and exposed as `StateFlow` to be observed by the UI layer. This ensures that UI components react to authentication state changes.
States represent the current status of each authentication process (`idle`, `pending`, `successful`, `unsuccessful`).

**Timeout and Error Handling**: Each authentication operation is wrapped with a `withTimeout` block to prevent long-running operations from freezing the UI, ensuring a better user experience with timeouts. `try-catch` block provides additional layer of error handling.

**Management of UI** More complex screens (e.g. `SignInScreen`) leverage custom view models in order to separate tasks and make testing easier.

# Technologies Used
- Kotlin
- Jetpack Compose M3
- Compose Type-safe navigation
- Kotlin Coroutines
- Kotlin Flow
- Firebase Authentication
- Hilt
- MVVM for simpler screens
- Language versions

# Additional Documents
- [Dependency graph](https://www.figma.com/board/YrWj7StykatlUCSJpgm87H/Dependency-graph?node-id=0-1&p=f&t=b0jjgle6fqV9tDji-0)
- [Routing](https://www.figma.com/board/lAi6Z1Zzomn2LWLnEEFm0V/Routing?node-id=1-169&t=kNqAiJkm7bKQiPX5-0)
- [Structure of the project](https://www.figma.com/board/Ef7m9jH43U0VyOSxcjsNFd/Structure-of-the-project?node-id=0-1&p=f&t=MipAn5v7BOv9HfVA-0)

# Screenshots

![AIESLA](https://github.com/user-attachments/assets/a1180c94-e5f6-4515-bc3e-b8a1c1de18da)
