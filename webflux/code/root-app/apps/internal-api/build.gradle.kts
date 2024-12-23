import org.springframework.boot.gradle.tasks.bundling.BootJar

dependencies {
    implementation(project(":root-app:libs:application"))
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-webflux") // 간편함을 위하여 의존성 라이브러리를 추가
}

tasks.getByName<BootJar>("bootJar") {
    mainClass.set("com.bombo.template.multijava.MultiJavaApplication")
    enabled = true
}
