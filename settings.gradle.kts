pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
<<<<<<< HEAD
        maven {
            setUrl("https://jitpack.io")
        }
=======
        maven {url = uri("https://jitpack.io") }
>>>>>>> 5c6d431 (fix error)
    }
}
rootProject.name = "CalendarFill"
include(":library")
include(":data")
