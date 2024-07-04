plugins {
    id("java-library")
    id("maven-publish")

    id("net.minecrell.plugin-yml.bukkit") version "0.6.0"
    id("xyz.jpenilla.run-paper") version "2.3.0"
    id("io.github.goooler.shadow") version "8.1.8"
}

group = "dev.booky"
version = "1.1.3"

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.20.6-R0.1-SNAPSHOT")
    compileOnlyApi("net.luckperms:api:5.4")

    implementation("org.bstats:bstats-bukkit:3.0.2")
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
        minecraftVersion("1.20.6")

        downloadPlugins {
            // bukkit plugin is not available on modrinth/hangar/github/etc.
            url("https://download.luckperms.net/1549/bukkit/loader/LuckPerms-Bukkit-5.4.134.jar")
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
