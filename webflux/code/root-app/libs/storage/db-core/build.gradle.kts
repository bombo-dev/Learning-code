dependencies {
    compileOnly(project(":root-app:libs:application"))
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    runtimeOnly("com.h2database:h2")
}