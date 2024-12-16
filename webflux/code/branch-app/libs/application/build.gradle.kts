dependencies {
    implementation("org.springframework:spring-context:6.1.14")
    runtimeOnly(project(":branch-app:libs:storage:db-core"))
    implementation(project(":branch-app:libs:client"))
}
