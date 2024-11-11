dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation(project(":libs:client"))
    runtimeOnly(project(":libs:storage:db-core"))
}
