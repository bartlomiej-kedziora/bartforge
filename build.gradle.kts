import org.gradle.api.tasks.testing.Test
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension

plugins {
    id("org.springframework.boot") apply false
    id("io.spring.dependency-management") apply false
    kotlin("jvm") apply false
    kotlin("plugin.spring") apply false
}

subprojects {
    val jdkVersion: String by project

    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "java")

    repositories {
        mavenCentral()
    }

    tasks.withType<Test>().configureEach {
        useJUnitPlatform()
    }

    extensions.configure<KotlinJvmProjectExtension> {
        jvmToolchain(jdkVersion.toInt())
    }

    extensions.configure<JavaPluginExtension> {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(jdkVersion.toInt()))
        }
    }

    dependencies {
        add("testImplementation", kotlin("test"))
    }
}