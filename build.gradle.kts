plugins {
    //trick: for the same plugin versions in all sub-modules
    id("com.android.application").apply(false)
    id("com.android.library").apply(false)
    id("com.google.gms.google-services") version "4.4.0" apply false
    kotlin("android").apply(false)
    kotlin("multiplatform").apply(false)
    alias(deps.plugins.room).apply(false)
    alias(deps.plugins.ksp).apply(false)
    alias(deps.plugins.compose.compiler).apply(false)
}

repositories {
    mavenCentral()
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
