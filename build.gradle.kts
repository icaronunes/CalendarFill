// Top-level build file where you can add configuration options common to all sub-projects/modules.


buildscript {
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.10")
        classpath("com.android.tools.build:gradle")
    }
}
plugins {

    id("com.android.application") version "7.2.0-beta02" apply false
    id("com.android.library") version "7.2.0-beta02" apply false
    id("org.jetbrains.kotlin.android") version "1.6.10" apply false
    id("org.jetbrains.kotlin.jvm") version "1.6.10" apply false
}

tasks.register("clean", Delete::class.java) {
    delete(rootProject.buildDir)
}