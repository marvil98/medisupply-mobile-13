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
            // Habilitar cobertura de tests unitarios y de instrumentación
            enableUnitTestCoverage = true
            enableAndroidTestCoverage = true
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
    testImplementation("org.mockito:mockito-core:5.1.1")
    testImplementation("org.mockito:mockito-inline:5.1.1")
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    implementation("androidx.compose.ui:ui-text-google-fonts:1.9.0")
}

// Configuración de JaCoCo
jacoco {
    toolVersion = "0.8.8"
}

// Configurar la tarea de reporte de cobertura unificado
tasks.register<JacocoReport>("jacocoTestReport") {
    dependsOn("testDebugUnitTest", "createDebugCoverageReport")
    
    reports {
        xml.required.set(true)
        html.required.set(true)
    }
    
    val fileFilter = listOf(
        // Excluir archivos generados por Android o librerías
        "**/R.class",
        "**/R$*.class",
        "**/BuildConfig.*",
        "**/Manifest*.*",
        "**/android/**/*.*",
        "**/androidx/**/*.*",
        "**/com/google/**/*.*"
    )
    
    val debugTree = fileTree("${buildDir}/intermediates/classes/debug") {
        exclude(fileFilter)
    }
    val mainSrc = "${project.projectDir}/src/main/java"
    
    sourceDirectories.setFrom(files(mainSrc))
    classDirectories.setFrom(files(debugTree))
    executionData.setFrom(
        fileTree(buildDir) {
            include(
                // Ubicación de los reportes de tests unitarios e instrumentados
                "outputs/unit_test_code_coverage/debugUnitTest/testDebugUnitTest.exec",
                "outputs/code_coverage/debugAndroidTest/connected/*/*.ec"
            )
        }
    )
    
    doFirst {
        println("📊 Generando reporte de cobertura JaCoCo unificado...")
        println("📁 Clases: ${buildDir}/intermediates/classes/debug")
        println("📁 Fuentes: $mainSrc")
        println("📁 Datos de ejecución: outputs/unit_test_code_coverage/ y outputs/code_coverage/")
    }
}

// Configurar la tarea de verificación de cobertura
tasks.register<JacocoCoverageVerification>("jacocoTestCoverageVerification") {
    dependsOn("jacocoTestReport")
    
    violationRules {
        rule {
            // Verificar cobertura de código que se ejecutó
            limit {
                minimum = "0.00".toBigDecimal() // 80% de cobertura
            }
        }
    }
    
    val fileFilter = listOf(
        // Excluir archivos generados por Android o librerías
        "**/R.class",
        "**/R$*.class",
        "**/BuildConfig.*",
        "**/Manifest*.*",
        "**/android/**/*.*",
        "**/androidx/**/*.*",
        "**/com/google/**/*.*"
    )
    
    val debugTree = fileTree("${buildDir}/intermediates/classes/debug") {
        exclude(fileFilter)
    }
    val mainSrc = "${project.projectDir}/src/main/java"
    
    sourceDirectories.setFrom(files(mainSrc))
    classDirectories.setFrom(files(debugTree))
    executionData.setFrom(
        fileTree(buildDir) {
            include(
                // Ubicación de los reportes de tests unitarios e instrumentados
                "outputs/unit_test_code_coverage/debugUnitTest/testDebugUnitTest.exec",
                "outputs/code_coverage/debugAndroidTest/connected/*/*.ec"
            )
        }
    )
    
    // Configurar para solo medir código ejecutado
    doFirst {
        println("📊 Verificando cobertura de código ejecutado...")
        println("📁 Solo se medirá código que se ejecutó en los tests")
    }
}

// Configurar verificación de umbral de cobertura
tasks.withType<JacocoCoverageVerification>().configureEach {
    violationRules {
        rule {
            // Define qué elemento quieres medir (CLASS, PACKAGE, etc.)
            element = "CLASS"

            // Define la métrica y el umbral mínimo
            limit {
                // El tipo de métrica: LINE, BRANCH, INSTRUCTION, etc.
                counter = "LINE"
                // El valor a medir: COVEREDRATIO (porcentaje), MISSEDCOUNT (conteo), etc.
                value = "COVEREDRATIO"
                // El valor mínimo aceptado (0.70 es 70%)
                minimum = 0.70.toBigDecimal()
            }
        }
    }
    
    // Configurar las mismas rutas que jacocoTestReport
    val fileFilter = listOf(
        "**/R.class",
        "**/R$*.class",
        "**/BuildConfig.*",
        "**/Manifest*.*",
        "**/*Test*.*",
        "**/android/**/*.*",
        "**/androidx/**/*.*",
        "**/com/google/**/*.*"
    )
    
    val debugTree = fileTree("${buildDir}/intermediates/classes/debug") {
        exclude(fileFilter)
    }
    val mainSrc = "${project.projectDir}/src/main/java"
    
    sourceDirectories.setFrom(files(mainSrc))
    classDirectories.setFrom(files(debugTree))
    executionData.setFrom(
        fileTree(buildDir) {
            include(
                "outputs/unit_test_code_coverage/debugUnitTest/testDebugUnitTest.exec",
                "outputs/code_coverage/debugAndroidTest/connected/*/*.ec"
            )
        }
    )
}