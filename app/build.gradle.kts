plugins {
    id("chirp.spring-boot-app")
}

group = "com.angiedev"
version = "0.0.1-SNAPSHOT"
description = "Chirp App API"

dependencies {
   implementation(projects.common)
   implementation(projects.chat)
   implementation(projects.notification)
   implementation(projects.user)
}