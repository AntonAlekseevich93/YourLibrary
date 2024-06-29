plugins {
    //trick: for the same plugin versions in all sub-modules
    id("com.android.application").apply(false)
    id("com.android.library").apply(false)
    kotlin("android").apply(false)
    kotlin("multiplatform").apply(false)
    alias(deps.plugins.room).apply(false)
    alias(deps.plugins.ksp).apply(false)
    alias(deps.plugins.compose.compiler).apply(false)
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
