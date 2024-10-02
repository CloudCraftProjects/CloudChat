import me.modmuss50.mpp.PublishModTask

plugins {
    id("java-library")
    id("maven-publish")

    id("net.minecrell.plugin-yml.bukkit") version "0.6.0"
    id("xyz.jpenilla.run-paper") version "2.3.1"
    id("com.gradleup.shadow") version "8.3.0"
    id("me.modmuss50.mod-publish-plugin") version "0.7.4"
}

group = "dev.booky"

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

publishMods {
    val repositoryName = "CloudCraftProjects/CloudChat"
    file = tasks.shadowJar.flatMap { it.archiveFile }.get()
    changelog = "See https://github.com/$repositoryName/releases/tag/v${project.version}"
    type = if (project.version.toString().endsWith("-SNAPSHOT")) BETA else STABLE
    additionalFiles.from(tasks.named<Jar>("sourcesJar").flatMap { it.archiveFile }.get())
    dryRun = !hasProperty("noDryPublish")

    github {
        accessToken = providers.environmentVariable("GITHUB_API_TOKEN")
            .orElse(providers.gradleProperty("ccGithubToken"))

        displayName = "${rootProject.name} v${project.version}"

        repository = repositoryName
        commitish = "master"
        tagName = "v${project.version}"

        if (project != rootProject) {
            parent(rootProject.tasks.named("publishGithub"))
        }
    }
    modrinth {
        accessToken = providers.environmentVariable("MODRINTH_API_TOKEN")
            .orElse(providers.gradleProperty("ccModrinthToken"))

        displayName = "${rootProject.name} v${project.version}"
        modLoaders.add("paper")

        projectId = "JIftXlPn"
        minecraftVersionRange {
            start = "1.20.6"
            end = "latest"
        }

        // even though luckperms doesn't publish paper jars on modrinth, set this as required
        requires("luckperms")
    }
}

tasks.withType<PublishModTask> {
    dependsOn(tasks.shadowJar)
    dependsOn(tasks.named<Jar>("sourcesJar"))
}
