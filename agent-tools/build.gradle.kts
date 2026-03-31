plugins {
    kotlin("jvm")
    kotlin("plugin.spring")
}

val springBootVersion: String by project

dependencies {
    implementation(platform("org.springframework.boot:spring-boot-dependencies:$springBootVersion"))

    implementation(project(":agent-core"))
    implementation("org.springframework:spring-context")
    implementation("org.slf4j:slf4j-api")
}

tasks.test {
    useJUnitPlatform()
}