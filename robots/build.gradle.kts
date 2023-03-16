import java.util.Map

plugins {
    id("java")
}

version = "1.0.0"

java {
    toolchain {
        version = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
}

dependencies {

}

tasks.compileJava {
    options.encoding = "UTF-8"
}

tasks.jar {
    manifest {
        attributes(Map.of("Main-class", "robots.RobotsProgram"))
    }
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    from(sourceSets.main.get().output)
    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
    })
}
