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
        maven("https://repository.map.naver.com/archive/maven")
        flatDir { // 여기
            dirs("libs")
        }
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        flatDir { // 여기
            dirs("libs")
        }
        mavenCentral()
    }
}

rootProject.name = "fitmily_android"
include(":app")
include(":fitmily_wearos")
include(":mylibrary")
include(":fitmily_common")
