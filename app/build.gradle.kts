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
    // JUnit 4
    testImplementation("junit:junit:4.13.2")

    // Mocking framework (e.g., Mockito-Kotlin)
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.0.0")

    // Robolectric (para resolver recursos Android en pruebas JVM)
    testImplementation("org.robolectric:robolectric:4.10.3")
}

// Versión de la herramienta JaCoCo
jacoco {
    toolVersion = "0.8.8"
}

// Tarea para CREAR el reporte Jacoco HTML/XML
tasks.register<JacocoReport>("jacocoTestReport") {
    dependsOn("testDebugUnitTest")

    reports {
        xml.required.set(true)
        html.required.set(true)
    }

    val fileFilter = listOf("**/R.class", "**/R$*.class", "**/BuildConfig.*", "**/Manifest*.*", "**/*Test*.*")
    val javaTree = fileTree("${buildDir}/intermediates/javac/debug/classes") { exclude(fileFilter) }
    val kotlinTree = fileTree("${buildDir}/tmp/kotlin-classes/debug") { exclude(fileFilter) }
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

// Tarea para VERIFICAR el umbral mínimo de cobertura
tasks.register<JacocoCoverageVerification>("jacocoTestCoverageVerification") {
    dependsOn("testDebugUnitTest")

    violationRules {
        rule {
            element = "CLASS"
            excludes = listOf(
                "**/R.class",
                "**/R$*.class",
                "**/BuildConfig.*",
                "**/Manifest*.*",
                "**/*Test*.*",
                "**/*\$ComposableSingletons*.*",
                "**/*_Factory.*",
                "**/*_MembersInjector.*",
                "**/*_Impl.*"
            )
            limit {
                counter = "LINE"
                value = "COVEREDRATIO"
                minimum = 0.70.toBigDecimal()
            }
        }
    }

    val fileFilter = listOf(
        "**/R.class",
        "**/R$*.class",
        "**/BuildConfig.*",
        "**/Manifest*.*",
        "**/*Test*.*"
    )

    val javaTree = fileTree("${buildDir}/intermediates/javac/debug/classes") { exclude(fileFilter) }
    val kotlinTree = fileTree("${buildDir}/tmp/kotlin-classes/debug") { exclude(fileFilter) }
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

// Configuración para SonarQube/SonarCloud
sonarqube {
    properties {
        property("sonar.projectKey", "margarita-villafane_medisupply-mobile-13")
        property("sonar.organization", "margarita-villafane")
        property("sonar.host.url", "https://sonarcloud.io")
        property("sonar.login", System.getenv("SONAR_TOKEN"))  // Asumiendo que usas token en variable de entorno
        property("sonar.coverage.jacoco.xmlReportPaths", "app/build/reports/jacoco/jacocoTestReport/jacocoTestReport.xml")
    }
}
