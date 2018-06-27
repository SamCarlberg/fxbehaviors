buildscript {
    repositories {
        mavenCentral()
        jcenter()
        maven {
            setUrl("https://plugins.gradle.org/m2/")
        }
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.1.1")
        classpath("com.jfrog.bintray.gradle:gradle-bintray-plugin:1.7.3")
    }
}

plugins {
    `java-library`
    `maven-publish`
    id ("com.jfrog.bintray") version "1.8.3"
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

// Publishing

val PUBLISHED_CONFIGURATION_NAME = "published"
val publicationName = name

val sourceJar = task<Jar>("sourceJar") {
    description = "Creates a JAR that contains the source code."
    from(java.sourceSets["main"].allSource)
    classifier = "sources"
}

val javadocJar = task<Jar>("javadocJar") {
    dependsOn("javadoc")
    description = "Creates a JAR that contains the javadocs."
    from(java.docsDir)
    classifier = "javadoc"
}

bintray {
    user = properties["bintray.publish.user"].toString()
    key = properties["bintray.publish.key"].toString()
    setPublications(publicationName)
    with(pkg) {
        repo = "maven-artifacts"
        name = "fxbehaviors"
        publish = true
        desc = "A replacement for the JavaFX control behavior API that was made inaccessible in Java 9"
        setLicenses("MIT")
        vcsUrl = "https://github.com/samcarlberg/fxbehaviors.git"
        githubRepo = "https://github.com/samcarlberg/fxbehaviors"
    }
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>(publicationName) {
                groupId = "com.github.samcarlberg"
                artifactId = "fxbehaviors"
                version = project.version as String
                val jar: Jar by tasks
                artifact(jar)
                artifact(sourceJar)
                artifact(javadocJar)
            }
        }
    }
}

configurations.create(PUBLISHED_CONFIGURATION_NAME)

/**
 * Retrieves or configures the [bintray][com.jfrog.bintray.gradle.BintrayExtension] project extension.
 */
fun Project.`bintray`(configure: com.jfrog.bintray.gradle.BintrayExtension.() -> Unit = {}) =
        extensions.getByName<com.jfrog.bintray.gradle.BintrayExtension>("bintray").apply { configure() }

/**
 * Retrieves or configures the [publishing][org.gradle.api.publish.PublishingExtension] project extension.
 */
fun Project.`publishing`(configure: org.gradle.api.publish.PublishingExtension.() -> Unit = {}) =
        extensions.getByName<org.gradle.api.publish.PublishingExtension>("publishing").apply { configure() }
