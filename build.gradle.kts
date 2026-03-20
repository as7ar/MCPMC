plugins {
    kotlin("jvm") version "2.3.10"
    id("com.gradleup.shadow") version "8.3.0"
    id("io.ktor.plugin") version "3.4.1"
}

group = "kr.astar"
version = "0.1"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/") {
        name = "papermc-repo"
    }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.8-R0.1-SNAPSHOT")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    implementation("io.modelcontextprotocol:kotlin-sdk:0.9.0")

    implementation("io.ktor:ktor-server-core")
    implementation("io.ktor:ktor-server-freemarker")
    implementation("io.ktor:ktor-server-netty")
}

val targetJavaVersion = 21
kotlin {
    jvmToolchain(targetJavaVersion)
}

tasks.build {
    dependsOn("shadowJar")
}

tasks.processResources {
    val props = mapOf("version" to version)
    inputs.properties(props)
    filteringCharset = "UTF-8"
    filesMatching("plugin.yml") {
        expand(props)
    }
}

tasks.shadowJar {
    archiveFileName.set("MCPMC-${version}.jar")
    mergeServiceFiles()
}