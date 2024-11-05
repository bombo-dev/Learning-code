plugins {
	id("java")
	kotlin("jvm") version "1.3.50"
	id("org.springframework.boot") version "3.3.5"
	id("io.spring.dependency-management") version "1.1.6"
}

version = "0.0.1-SNAPSHOT"

subprojects {
	apply(plugin = "idea")
}

allprojects {
	apply(plugin = "project-report")
	apply(plugin = "org.jetbrains.kotlin.jvm")
	apply(plugin = "io.spring.dependency-management")

	tasks.withType<Test> {
		useJUnitPlatform()
	}

	repositories {
		mavenCentral()
	}

	dependencies {
		testImplementation("org.junit.jupiter:junit-jupiter-api")
		testImplementation("org.junit.jupiter:junit-jupiter-params")
	}
}
