plugins {
         id("java-library")
         id("org.jetbrains.kotlin.jvm") version "1.8.0"
     }

     repositories {
         mavenCentral()
     }

     dependencies {
         implementation("org.jsoup:jsoup:1.14.3")
         implementation("com.lagradost:cloudstream3:3.0.0")
     }