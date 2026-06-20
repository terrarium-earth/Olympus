import com.teamresourceful.publishing.GitHubPom
import com.teamresourceful.publishing.javaPublishing
import com.teamresourceful.utils.Platform
import com.teamresourceful.utils.getPlatform
import groovy.json.StringEscapeUtils

plugins {
    java
    id("maven-publish")
    alias(libs.plugins.resourceful.gradle)
    alias(libs.plugins.resourceful.minecraft) apply false
}

subprojects {
    apply(plugin = "maven-publish")

    val platform = getPlatform()

    when (platform) {
        Platform.COMMON -> apply(plugin = "com.teamresourceful.plugins.minecraft-platform-common")
        Platform.FABRIC -> apply(plugin = "com.teamresourceful.plugins.minecraft-platform-fabric")
        Platform.NEOFORGE -> apply(plugin = "com.teamresourceful.plugins.minecraft-platform-neoforge")
    }

    if (platform != Platform.COMMON) {
        tasks.withType<JavaCompile> {
            val serviceArgs = listOf(
                "-Xplugin:ServicePlugin",
                "--service-plugin-platform=$platform",
            )

            options.encoding = "UTF-8"
            options.compilerArgs.add(serviceArgs.joinToString(separator = " "))
        }
    }

    dependencies {
        if (platform != Platform.COMMON) {
            annotationProcessor(rootProject.libs.service.plugin)
        }

        implementation(
            group = "com.teamresourceful.resourcefullib",
            name = "resourcefullib-${platform.id}-${rootProject.libs.versions.minecraft.get()}",
            version = rootProject.libs.versions.resourceful.lib.get()
        )
    }

    javaPublishing {
        artifactId = "${rootProject.name}-${platform.name}-${rootProject.libs.versions.minecraft.get()}".lowercase()

        pom = GitHubPom(
            "Olympus",
            "A UI library for Minecraft mods",
            "MIT",
            "https://github.com/terrarium-earth/Olympus"
        )

        repo = "https://maven.teamresourceful.com/repository/terrarium/"
    }
}


resourcefulGradle {
    templates {
        register("discord") {

            val changelog: String = file("changelog.md").readText(Charsets.UTF_8)

            source = file("templates/embed.json.template")
            injectedValues = mapOf(
                "version" to version,
                "minecraft" to libs.versions.minecraft.get(),
                "neoforge" to libs.versions.neoforge.get(),
                "fabric" to libs.versions.fabric.api.get(),
                "changelog" to StringEscapeUtils.escapeJava(changelog),
            )
        }
    }
}