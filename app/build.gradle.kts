

plugins {
    // Apply the application plugin to add support for building a CLI application in Java.
    application
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {
    implementation(project(":mq2tLib"))
    implementation("org.slf4j:slf4j-api:2.0.17")

}

// Apply a specific Java toolchain to ease working on different environments.
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

application {
    // Define the main class for the application.
    mainClass = "ru.maxeltr.mq2tHttpPollableComponent.Main"
}

tasks.jar {
    // jar name = component name
    archiveBaseName.set("mq2tHttpPollableComponent")
}


