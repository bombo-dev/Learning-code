import org.springframework.boot.gradle.tasks.bundling.BootJar

dependencies {
    implementation(project(":microservice-webflux:libs:application"))
    implementation("org.springframework.boot:spring-boot-starter-web")
}

tasks.getByName<BootJar>("bootJar") {
    mainClass.set("com.bombo.servera.ServerBApplication")
}
