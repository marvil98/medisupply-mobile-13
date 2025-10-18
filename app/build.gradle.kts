plugins {
    // Plugins ya existentes
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    // 🛠️ 1. Aplicación del Plugin JaCoCo (Ya lo tenías, pero es crucial)
    id("jacoco")
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

        // 🛠️ 2. Habilitar la Cobertura en la Variante 'debug'
        debug {
            // Esta propiedad le dice a Gradle que prepare la recopilación de datos de JaCoCo.
            // Es lo que automáticamente genera la tarea 'createDebugUnitTestCoverageReport'.
            isTestCoverageEnabled = true
        }
    }

    // ... el resto de la configuración ...

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
    // ... tus dependencias existentes ...
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.navigation.compose)

    // Dependencias de pruebas
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    implementation("androidx.compose.ui:ui-text-google-fonts:1.9.0")
}

tasks.withType<JacocoCoverageVerification> {
    // Requiere que los tests se ejecuten antes
    dependsOn("testDebugUnitTest")

    violationRules {
        rule {
            // Regla a nivel de proyecto
            limit {
                // El porcentaje mínimo aceptado para la cobertura de línea (LINE)
                minimum = 0.8.toBigDecimal() // Establecido en 80%
            }
        }
        // Opcional: puedes añadir más reglas para clases, métodos, etc.
    }
}