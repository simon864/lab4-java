plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("com.google.code.gson:gson:2.10") // Для работы с JSON
    implementation("org.projectlombok:lombok:1.18.30") // Для Lombok
    annotationProcessor("org.projectlombok:lombok:1.18.24") // Для Lombok
}

tasks.test {
    useJUnitPlatform()
}