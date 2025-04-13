pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "AIESLA"
include(":app")
include(":navigation")
include(":applicationScaffold")
include(":routeSignedIn")
include(":routeSignedOut")
include(":routeUnsuccessfulInitialization")
include(":networkConnectivity")
include(":authentication")
include(":dataStore")
include(":roomLocalDatabase")
include(":firebase")
