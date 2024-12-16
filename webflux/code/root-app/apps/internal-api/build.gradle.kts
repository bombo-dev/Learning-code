import org.springframework.boot.gradle.tasks.bundling.BootJar

dependencies {
    implementation(project(":root-app:libs:application"))
    implementation("org.springframework.boot:spring-boot-starter-web")
}

tasks.getByName<BootJar>("bootJar") {
    mainClass.set("com.bombo.template.multijava.MultiJavaApplication")
    enabled = true
}
