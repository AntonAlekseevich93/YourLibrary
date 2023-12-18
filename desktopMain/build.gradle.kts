plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

kotlin {
    jvm()
    sourceSets {
        named("jvmMain") {
            dependencies {
                Dependencies.Kotlin
                implementation(compose.uiTooling)
                implementation(compose.material3)
                implementation(compose.desktop.currentOs)
                api(project(":common:root-compose"))
            }
        }
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            modules("java.sql")
            targetFormats(
                org.jetbrains.compose.desktop.application.dsl.TargetFormat.Dmg,
                org.jetbrains.compose.desktop.application.dsl.TargetFormat.Msi,
                org.jetbrains.compose.desktop.application.dsl.TargetFormat.Deb
            )

//for status bar appearance
//            jvmArgs(
//                "-Dapple.awt.application.appearance=system"
//            )

            packageName = "yourLibrary"
            packageVersion = "1.0.0"
        }
    }
}





