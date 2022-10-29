plugins {
    id("java-library")
    id("maven-publish")
}

group = "dev.booky"
version = "1.1.1"

repositories {
    maven("https://papermc.io/repo/repository/maven-public/")
}

dependencies {
    api("io.papermc.paper:paper-api:1.19.2-R0.1-SNAPSHOT")
    api("net.luckperms:api:5.4")
}

tasks.processResources {
    inputs.property("version", version)
    filesMatching("plugin.yml") {
        expand("version" to version)
    }
}

java {
    withSourcesJar()
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

publishing {
    publications.create<MavenPublication>("maven") {
        artifactId = project.name.toLowerCase()
        from(components["java"])
    }
}
