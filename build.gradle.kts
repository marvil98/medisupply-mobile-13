// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
}

tasks.register<JacocoReport>("jacocoTestReport") {
    dependsOn("testDebugUnitTest")

    reports {
        xml.required.set(true)
        html.required.set(true)
    }

    val fileFilter = listOf("**/R.class", "**/BuildConfig.*", "**/Manifest*.*", "**/*Test*.*")
    val debugTree = fileTree("${buildDir}/intermediates/javac/debug") {
        exclude(fileFilter)
    }
    val kotlinDebugTree = fileTree("${buildDir}/tmp/kotlin-classes/debug") {
        exclude(fileFilter)
    }

    sourceDirectories.setFrom(files(
        "${project.projectDir}/src/main/java",
        "${project.projectDir}/src/main/kotlin"
    ))
    classDirectories.setFrom(files(debugTree, kotlinDebugTree))
    executionData.setFrom(fileTree(buildDir).include(
        "jacoco/testDebugUnitTest.exec"
    ))
}
