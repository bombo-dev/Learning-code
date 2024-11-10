tasks.getByName("bootJar") {
    enabled = true
}

tasks.getByName("jar") {
    enabled = false
}

dependencies {
    implementation(project(":libs:application"))
    implementation("org.springframework.boot:spring-boot-starter-web")
}
