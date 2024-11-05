dependencies {
    implementation(project(":microservice-webflux:libs:application"))
    implementation("org.springframework.boot:spring-boot-starter-web")
}

tasks.getByName<BootJar>("bootJar") {
    mainClass.set("com.bombo.webflux.servera.ServerAApplication")
}
