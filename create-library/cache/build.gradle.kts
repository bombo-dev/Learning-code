plugins {
    id("java")
    kotlin("jvm")
    id("maven-publish")
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

tasks.named<Jar>("jar") {
    enabled = true
    archiveClassifier.set("") // 빈 문자열로 설정

    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) }) // 의존성 포함

    duplicatesStrategy = DuplicatesStrategy.EXCLUDE // 중복 처리
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(11)
}
