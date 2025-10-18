plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("jacoco") // ✅ Plugin de JaCoCo aplicado
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
        // 👇 CORRECCIÓN CLAVE: Habilitar la cobertura de pruebas unitarias para 'debug'
        debug {
            enableUnitTestCoverage = true
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

    testOptions {
        animationsDisabled = true
        unitTests.isIncludeAndroidResources = true
    }
}

configurations.all {
    resolutionStrategy {
        // 🔧 Evita conflicto entre espresso-core 3.5.0 vs 3.5.1
        force("androidx.test.espresso:espresso-core:3.5.0")
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.navigation.compose)
    implementation("androidx.compose.ui:ui-text-google-fonts:1.9.0")

    testImplementation(libs.junit)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.0")
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    testImplementation(kotlin("test"))
}

// -----------------------------------------------------------------------------

/**
 * Tarea personalizada de JaCoCo para generar el reporte de cobertura.
 */
tasks.register<JacocoReport>("jacocoTestReport") {
    // Asegura que las pruebas unitarias se ejecuten antes de generar el reporte
    dependsOn("testDebugUnitTest")

    reports {
        xml.required.set(true) // ✅ Necesario para que GitHub Action lea el porcentaje
        html.required.set(true)
        html.outputLocation.set(layout.buildDirectory.dir("reports/jacoco/jacocoTestReport/html"))
    }

    // 👇 MEJORA: Lista de exclusiones expandida para Android/Compose/Kotlin
    val coverageExclusions = listOf(
        // Clases generadas por Android/Kotlin
        "**/R.class",
        "**/R\$*.class",
        "**/BuildConfig.*",
        "**/Manifest*.*",
        "**/*Test*.*",
        "android/**/*.*",
        // Clases de Composable y ViewBinding
        "**/*_Factory*",
        "**/*_MembersInjector*",
        "**/*_Provide*",
        "**/data/models/**", // Si son solo data classes
        "**/ui/theme/**", // Archivos de tema
        "**/*ComposableSingletons*", // Clases singleton de Compose
        "**/*Kt\$lambda\$*", // Lambdas generadas por el compilador Kotlin
        "**/*_Impl*", // Clases de Room/otros ORMs
        "**/*_HiltModules*", // Módulos de Hilt si usas Hilt
    )

    // 👇 CORRECCIÓN: Directorios de clases para instrumentación. Se simplifica el acceso.
    classDirectories.setFrom(
        fileTree("${layout.buildDirectory.get()}/tmp/kotlin-classes/debug") {
            exclude(coverageExclusions)
        }
    )

    // Directorios de código fuente (para el reporte HTML)
    sourceDirectories.setFrom(
        files(
            "${project.projectDir}/src/main/java",
            "${project.projectDir}/src/main/kotlin"
        )
    )

    // 👇 CORRECCIÓN: Ubicación del archivo de ejecución (.exec) de las pruebas unitarias.
    // Esta ruta suele ser la más confiable para versiones recientes de Gradle/Android.
    executionData.setFrom(
        fileTree(project.buildDir) {
            include("outputs/unit_test_code_coverage/debugUnitTest/testDebugUnitTest.exec")
        }
    )
}

// Tarea extra para mantener el nombre de tu Action
tasks.register<JacocoReport>("createDebugCoverageReport") {
    dependsOn("jacocoTestReport")
}