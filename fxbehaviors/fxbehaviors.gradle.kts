plugins {
    `java-library`
}

group = "com.github.samcarlberg"
version = "0.1.0"

repositories {
    mavenCentral()
}

dependencies {
    fun junitJupiter(name: String, version: String = "5.0.0") =
            create(group = "org.junit.jupiter", name = name, version = version)

    testCompile(junitJupiter(name = "junit-jupiter-api"))
    testCompile(junitJupiter(name = "junit-jupiter-engine"))
    testCompile(junitJupiter(name = "junit-jupiter-params"))
    testRuntime(create(group = "org.junit.platform", name = "junit-platform-launcher", version = "1.0.0"))
}
