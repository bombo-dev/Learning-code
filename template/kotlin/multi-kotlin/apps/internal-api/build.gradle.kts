dependencies {
    implementation(project(":common-libs:logging"))
    implementation(project(":common-libs:util"))
    implementation(project(":common-libs:monitoring"))
    implementation(project(":libs:application"))
    runtimeOnly(project(":libs:http-client"))
    runtimeOnly(project(":libs:storage:db-core"))

    testImplementation(project(":common-libs:tests"))
    testImplementation("org.springframework:spring-tx")

    // web
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    // data core
    implementation("org.springframework.data:spring-data-commons")
}
