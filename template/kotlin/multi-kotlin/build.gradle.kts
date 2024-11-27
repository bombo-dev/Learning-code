import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    id("org.springframework.boot") version Versions.springBootVersion apply false
    id("io.spring.dependency-management") version Versions.springDependencyManagementPluginVersion apply false
    id("com.coditory.integration-test") version Versions.integrationTestPlugin apply false
    kotlin("plugin.serialization") version Versions.kotlinVersion apply false
    kotlin("jvm") version Versions.kotlinVersion apply false
    kotlin("kapt") version Versions.kotlinVersion apply false
    kotlin("plugin.spring") version Versions.kotlinVersion apply false
    kotlin("plugin.jpa") version Versions.kotlinVersion apply false
}


allprojects {
    group = "com.bombo.template"
    version = "0.0.1-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jetbrains.kotlin.kapt")
    apply(plugin = "org.jetbrains.kotlin.plugin.spring")
    apply(plugin = "com.coditory.integration-test")
    apply(plugin = "org.jetbrains.kotlin.plugin.serialization")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")

    configure<DependencyManagementExtension> {
        dependencies {
            dependency("io.mockk:mockk:${Versions.mockk}")
        }
    }

    dependencies {
        val implementation by configurations
        val testImplementation by configurations
        val testRuntimeOnly by configurations
        val integrationImplementation by configurations
        val integrationRuntimeOnly by configurations

        implementation("org.jetbrains.kotlin:kotlin-reflect")
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

        testImplementation("org.junit.jupiter:junit-jupiter-api")
        testImplementation("org.junit.jupiter:junit-jupiter-params")
        testImplementation("org.assertj:assertj-core")
        testImplementation("org.mockito:mockito-core")
        testImplementation("org.mockito:mockito-junit-jupiter")
        testImplementation("io.mockk:mockk")
        testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")

        integrationImplementation("org.junit.jupiter:junit-jupiter-api")
        integrationImplementation("org.junit.jupiter:junit-jupiter-params")
        integrationImplementation("org.assertj:assertj-core")
        integrationRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = Versions.javaVersion
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    tasks.getByName<BootJar>("bootJar") {
        enabled = false
    }

    tasks.getByName<Jar>("jar") {
        enabled = true
    }
}
