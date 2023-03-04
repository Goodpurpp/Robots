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
