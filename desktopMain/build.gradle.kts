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
                api(project(":common:root-compose"))
                api(project(":common:ui"))
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
            macOS {
                bundleID = "org.jetbrains.compose.examples.deeplinking"
                infoPlist {
                    extraKeysRawXml = macExtraPlistKeys

                }
                packageName = "yourLibrary"
                packageVersion = "1.0.0"
            }
        }
    }
}
val macExtraPlistKeys: String
    get() = """
      <key>NSDocumentsFolderUsageDescription</key>
      <string>This app requires access to the documents folder to save files.</string>
    """





