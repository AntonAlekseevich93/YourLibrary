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
include(":common:utils:url-parser:api")
include(":common:utils:url-parser:data")
include(":common:utils:date")
include(":common:utils:database-utils")
include(":common:utils:json-utils")
include(":common:file-manager")
include(":feature:shelf:api")
include(":feature:shelf:presentation")
include(":feature:shelf:data")
include(":feature:shelf:compose")
include(":feature:search:api")
include(":feature:search:presentation")
include(":feature:search:data")
include(":feature:search:compose")
include(":feature:book-info:api")
include(":feature:book-info:presentation")
include(":feature:book-info:data")
include(":feature:book-info:compose")
include(":feature:book-creator:api")
include(":feature:book-creator:presentation")
include(":feature:book-creator:data")
include(":feature:book-creator:compose")
include(":feature:settings:api")
include(":feature:settings:presentation")
include(":feature:settings:data")
include(":feature:settings:compose")