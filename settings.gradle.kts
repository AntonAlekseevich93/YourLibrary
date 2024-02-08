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
include(":common:arch")
include(":common:root-ios")
include(":common:root-app")
include(":common:root-app:api")
include(":common:root-app:compose")
include(":common:root-app:data")
include(":common:root-app:domain")
include(":common:root-app:presentation")
include(":common:root-app:di")
include(":common:di:compose")
include(":common:di:ios")
include(":common:theme")
include(":common:models")
include(":common:scopes")
include(":common:ui:drag-and-drop")
include(":common:resources:strings")
include(":common:resources:drawable")
include(":common:utils:url-parser:api")
include(":common:utils:url-parser:data")
include(":common:utils:date")
include(":common:utils:database-utils")
include(":common:utils:json-utils")
include(":common:file-manager")
include(":feature:main-screen:api")
include(":feature:main-screen:presentation")
include(":feature:main-screen:data")
include(":feature:main-screen:compose")
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
include(":feature:book-info:domain")
include(":feature:book-info:compose")
include(":feature:book-creator:api")
include(":feature:book-creator:presentation")
include(":feature:book-creator:data")
include(":feature:book-creator:domain")
include(":feature:book-creator:compose")
include(":feature:settings:api")
include(":feature:settings:presentation")
include(":feature:settings:data")
include(":feature:settings:compose")
include(":feature:authors:api")
include(":feature:authors:presentation")
include(":feature:authors:data")
include(":feature:authors:compose")
include(":feature:authors:domain")