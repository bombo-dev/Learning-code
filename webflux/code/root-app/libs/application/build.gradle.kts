plugins {
    kotlin("jvm")
}
dependencies {
    implementation("org.springframework:spring-context:6.1.14")
    runtimeOnly(project(":root-app:libs:storage:db-core"))
    runtimeOnly(project(":root-app:libs:client"))
    implementation(kotlin("stdlib-jdk8"))
}
repositories {
    mavenCentral()
}
kotlin {
    jvmToolchain(17)
}
