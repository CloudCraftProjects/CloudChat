plugins {
    id("java-library")
    id("maven-publish")

    id("net.minecrell.plugin-yml.bukkit") version "0.6.0"
    id("xyz.jpenilla.run-paper") version "2.2.0"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "dev.booky"
version = "1.1.3"

repositories {
    maven("https://papermc.io/repo/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.20.2-R0.1-SNAPSHOT")
    compileOnlyApi("net.luckperms:api:5.4")

    implementation("org.bstats:bstats-bukkit:3.0.2")
}

java {
    withSourcesJar()
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
        vendor.set(JvmVendorSpec.ADOPTIUM)
    }
}

publishing {
    publications.create<MavenPublication>("maven") {
        artifactId = project.name.lowercase()
        from(components["java"])
    }
}

bukkit {
    main = "$group.cloudchat.CloudChatMain"
    apiVersion = "1.20"
    authors = listOf("booky10")
    depend = listOf("LuckPerms")
}

tasks {
    runServer {
        minecraftVersion("1.20.2")

        downloadPlugins {
            // bukkit plugin is not available on modrinth/hangar/github/etc.
            url("https://download.luckperms.net/1519/bukkit/loader/LuckPerms-Bukkit-5.4.106.jar")
        }
    }

    shadowJar {
        relocate("org.bstats", "${project.group}.cloudchat.bstats")
    }

    assemble {
        dependsOn(shadowJar)
    }
}
