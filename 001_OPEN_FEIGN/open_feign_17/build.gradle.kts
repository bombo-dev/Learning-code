plugins {
    java
    id("org.springframework.boot") version "2.3.11.RELEASE"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "com.bombo.demo"
version = "0.0.1-SNAPSHOT"

java.sourceCompatibility = JavaVersion.VERSION_14

repositories {
    mavenCentral()
}

extra["springCloudVersion"] = "Hoxton.SR12"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign:2.2.9.RELEASE")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.springframework.boot:spring-boot-starter-aop")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
