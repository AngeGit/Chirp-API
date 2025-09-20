plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.kotlin.jpa)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
}

group = "com.angiedev"
version = "0.0.1-SNAPSHOT"
description = "Chirp App API"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
	maven { url = uri("https://repo.spring.io/snapshot") }
}

dependencies {
    implementation(libs.bundles.spring.starters)
    implementation(libs.bundles.kotlin)
    implementation(libs.bundles.jwt)
    implementation(libs.bundles.http)
    implementation(libs.firebase.admin.sdk)
    implementation(libs.jackson.module.kotlin)
	runtimeOnly(libs.postgresql)

    implementation(libs.bundles.spring.test)
	testRuntimeOnly(libs.junit.platform.launcher)
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict", "-Xannotation-default-target=param-property")
	}
}

allOpen {
	annotation("jakarta.persistence.Entity")
	annotation("jakarta.persistence.MappedSuperclass")
	annotation("jakarta.persistence.Embeddable")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
