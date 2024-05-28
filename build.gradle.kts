buildscript {
    dependencies {
        classpath("com.google.gms:google-services:4.4.1")
        // classpath("https://jitpack.io")
    }
    repositories {
        google()
        jcenter()
        maven { url = uri("https://jitpack.io") }

    }

}


// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.1.2" apply false
    id("com.google.gms.google-services") version "4.4.1" apply false
    id("com.google.dagger.hilt.android") version "2.48" apply false
}