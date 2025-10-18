plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("org.jetbrains.kotlinx.kover") version "0.7.3" // ✅ Plugin de cobertura moderno
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
        // ✅ Habilitar cobertura en debug
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

    // ✅ Tests
    testImplementation(libs.junit)
    testImplementation(kotlin("test"))

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.0")
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")

    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}

/**
 * ✅ Configuración moderna del plugin Kover (v0.7+)
 * Genera reportes XML y HTML + verificación de cobertura mínima.
 */
koverReport {
    filters {
        excludes {
            classes(
                "**/R.class",
                "**/R$*.class",
                "**/BuildConfig.*",
                "**/Manifest*.*",
                "**/*Test*.*",
                "**/*_Factory*",
                "**/*_MembersInjector*",
                "**/*_Provide*",
                "**/data/models/**",
                "**/ui/theme/**",
                "**/*ComposableSingletons*",
                "**/*Kt*lambda*",
                "**/*_Impl*",
                "**/*_HiltModules*"
            )
        }
    }

    defaults {
        xml {
            onCheck = true // genera XML
        }
        html {
            onCheck = true // genera HTML
        }
    }

    verify {
        rule("Minimum coverage") {
            bound {
                minValue = 80 // ✅ cobertura mínima requerida
            }
        }
    }
}
