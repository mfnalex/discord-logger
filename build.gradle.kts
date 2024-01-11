plugins {
    kotlin("jvm") version "1.9.21"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "com.jeff-media"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test")

    // https://mvnrepository.com/artifact/org.slf4j/slf4j-simple
    implementation("org.slf4j:slf4j-simple:2.0.11")

    // https://mvnrepository.com/artifact/net.sf.jopt-simple/jopt-simple
    implementation("net.sf.jopt-simple:jopt-simple:5.0.4")

    // https://mvnrepository.com/artifact/net.dv8tion/JDA
    implementation("net.dv8tion:JDA:5.0.0-beta.19")

}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(8)
}

tasks {
    build {
        dependsOn(shadowJar)
    }

    shadowJar {
        archiveClassifier = ""
        archiveVersion = ""
        manifest {
            attributes["Main-Class"] = "com.jeff_media.discordlogger.MainKt"
        }
    }
}