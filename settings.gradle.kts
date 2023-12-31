pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}


rootProject.name = "YourLibrary"
include(":androidApp")
include(":desktopMain")
include(":common:core")
include(":common:root-ios")
include(":common:root-compose")
include(":common:di:compose")
include(":common:di:ios")
include(":common:theme")
include(":common:models")
include(":common:ui:drag-and-drop")
include(":common:resources:strings")
include(":common:resources:drawable")
include(":feature:shelf:api")
include(":feature:shelf:presentation")
include(":feature:shelf:data")
include(":feature:shelf:compose")
include(":feature:search:api")
include(":feature:search:presentation")
include(":feature:search:data")
include(":feature:search:compose")
include(":feature:book:api")
include(":feature:book:presentation")
include(":feature:book:data")
include(":feature:book:compose")
