pluginManagement {
    repositories {
        google() // 👈 keep full google repo (don’t filter Firebase out)
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

rootProject.name = "MyApplication" // ✅ no spaces in project name
include(":app")
