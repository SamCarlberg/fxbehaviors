import org.gradle.api.tasks.wrapper.Wrapper

subprojects {
    afterEvaluate {
        configure<JavaPluginConvention> {
            sourceCompatibility = JavaVersion.VERSION_1_10
        }
    }
}

task<Wrapper>("wrapper") {
    gradleVersion = "4.8"
}
