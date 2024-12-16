dependencies {
    implementation("org.springframework:spring-context:6.1.14")
    runtimeOnly(project(":root-app:libs:storage:db-core"))
    runtimeOnly(project(":root-app:libs:client"))
}
