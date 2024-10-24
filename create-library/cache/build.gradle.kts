plugins {
    id("java")
}

group = "com.bombo"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val SPRING_BOOT_VERISON = "2.7.5"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-aop:$SPRING_BOOT_VERISON")
    implementation("org.redisson:redisson-spring-boot-starter:$SPRING_BOOT_VERISON")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}
