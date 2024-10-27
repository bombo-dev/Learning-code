plugins {
    id("java")
    kotlin("jvm")
}

group = "com.bombo"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-aop:2.7.5")
    implementation("org.redisson:redisson-spring-boot-starter:3.17.7")
    implementation(kotlin("stdlib-jdk8"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(11)
}
