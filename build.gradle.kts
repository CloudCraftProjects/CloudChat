plugins {
    id("java-library")
    id("maven-publish")

    id("net.minecrell.plugin-yml.bukkit") version "0.6.0"
    id("xyz.jpenilla.run-paper") version "2.3.1"
    id("com.gradleup.shadow") version "8.3.0"
}

group = "dev.booky"
version = "1.1.5-SNAPSHOT"

repositories {
    maven("https://repo.cloudcraftmc.de/public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.1-R0.1-SNAPSHOT")
    compileOnlyApi("net.luckperms:api:5.4")

    implementation("org.bstats:bstats-bukkit:3.1.0")
}

java {
    withSourcesJar()
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
        vendor = JvmVendorSpec.ADOPTIUM
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
        minecraftVersion("1.21.1")

        downloadPlugins {
            // bukkit plugin is not available on modrinth/hangar/github/etc.
            url("https://download.luckperms.net/1556/bukkit/loader/LuckPerms-Bukkit-5.4.141.jar")
        }
    }

    withType<JavaCompile> {
        options.encoding = Charsets.UTF_8.name()
    }

    withType<Jar> {
        // no spigot mappings are used, disable useless remapping step
        manifest.attributes("paperweight-mappings-namespace" to "mojang")
    }

    shadowJar {
        relocate("org.bstats", "${project.group}.cloudchat.bstats")
    }

    assemble {
        dependsOn(shadowJar)
    }
}
