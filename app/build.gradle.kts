plugins {
    // Tus plugins existentes
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("jacoco")
    id("org.sonarqube") version "4.3.0.3225" // Plugin de SonarCloud
}

android {
    namespace = "com.example.medisupplyapp"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.medisupplyapp"
        minSdk = 31
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            // Habilitamos cobertura solo para pruebas unitarias
            enableUnitTestCoverage = true
            enableAndroidTestCoverage = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    // ... tus dependencias ...
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.navigation.compose)
    testImplementation(libs.junit)
    testImplementation("org.mockito:mockito-core:5.1.1")
    testImplementation("org.mockito:mockito-inline:5.1.1")
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    implementation("androidx.compose.ui:ui-text-google-fonts:1.9.0")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    testImplementation("androidx.compose.ui:ui-test-junit4")
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.0.0")
    testImplementation("org.robolectric:robolectric:4.10.3")
}

jacoco {
    toolVersion = "0.8.8"
}

val jacocoExcludes = listOf(
    "**/R.class",
    "**/R$*.class",
    "**/BuildConfig.*",
    "**/Manifest*.*",
    "**/*Test*.*",
    "**/*\$ComposableSingletons*.*",
    "**/*_Factory.*",
    "**/*_MembersInjector.*",
    "**/*_Impl.*",
    "**/*\$Lambda\$*.*",
    "**/*_Delegate.*", // para delegados de Kotlin
    "**/*\$inlined\$*.*", // funciones inlminimumine generadas
    "**/*_ExternalSyntheticLambda.*", // lambdas externas generadas
)

tasks.register<JacocoReport>("jacocoTestReport") {
    dependsOn("testDebugUnitTest")

    reports {
        xml.required.set(true)
        html.required.set(true)
    }

    val javaTree = fileTree("${buildDir}/intermediates/javac/debug/classes") {
        exclude(jacocoExcludes)
    }
    val kotlinTree = fileTree("${buildDir}/tmp/kotlin-classes/debug") {
        exclude(jacocoExcludes)
    }
    val mainSrc = files(
        "${project.projectDir}/src/main/java",
        "${project.projectDir}/src/main/kotlin"
    )

    sourceDirectories.setFrom(mainSrc)
    classDirectories.setFrom(files(javaTree, kotlinTree))
    executionData.setFrom(fileTree(buildDir) {
        include("outputs/unit_test_code_coverage/debugUnitTest/testDebugUnitTest.exec")
    })
}

tasks.register<JacocoCoverageVerification>("jacocoTestCoverageVerification") {
    dependsOn("testDebugUnitTest")

    violationRules {
        rule {
            element = "CLASS"
            excludes = jacocoExcludes
            limit {
                counter = "LINE"
                value = "COVEREDRATIO"
                minimum = BigDecimal("0.0")
            }
        }
    }

    val javaTree = fileTree(layout.buildDirectory.dir("intermediates/javac/debug/classes").get().asFile) {
        exclude(jacocoExcludes)
    }

    val kotlinTree = fileTree(layout.buildDirectory.dir("tmp/kotlin-classes/debug").get().asFile) {
        exclude(jacocoExcludes)
    }

    val mainSrc = files(
        "${project.projectDir}/src/main/java",
        "${project.projectDir}/src/main/kotlin"
    )

    sourceDirectories.setFrom(mainSrc)
    classDirectories.setFrom(files(javaTree, kotlinTree))
    executionData.setFrom(fileTree(layout.buildDirectory.get().asFile) {
        include("outputs/unit_test_code_coverage/debugUnitTest/testDebugUnitTest.exec")
    })
}

sonarqube {
    properties {
        property("sonar.projectKey", "margarita-villafane_medisupply-mobile-13")
        property("sonar.organization", "margarita-villafane")
        property("sonar.host.url", "https://sonarcloud.io")
        property("sonar.login", System.getenv("SONAR_TOKEN"))  // Token en variable de entorno
        property("sonar.coverage.jacoco.xmlReportPaths", "app/build/reports/jacoco/jacocoTestReport/jacocoTestReport.xml")
    }
}
