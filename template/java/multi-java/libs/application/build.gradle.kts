dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    runtimeOnly(project(":libs:storage:db-core"))
}
