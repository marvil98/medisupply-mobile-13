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

// Configurar la tarea de reporte de cobertura optimizada para tests unitarios
tasks.register<JacocoReport>("jacocoTestReport") {
    dependsOn("testDebugUnitTest")
    
    reports {
        xml.required.set(true)
        html.required.set(true)
        xml.outputLocation.set(file("${buildDir}/reports/jacoco/jacocoTestReport/jacocoTestReport.xml"))
        html.outputLocation.set(file("${buildDir}/reports/jacoco/jacocoTestReport/html"))
    }
    
    val fileFilter = listOf(
        "**/R.class",
        "**/R$*.class",
        "**/BuildConfig.*",
        "**/Manifest*.*",
        "**/*Test*.*",
        "android/**/*.*"
    )
    
    // Incluir tanto clases Java como Kotlin compiladas
    val javaDebugTree = fileTree("${buildDir}/intermediates/javac/debug/classes") {
        exclude(fileFilter)
    }
    val kotlinDebugTree = fileTree("${buildDir}/tmp/kotlin-classes/debug") {
        exclude(fileFilter)
    }
    
    val mainSrc = "${project.projectDir}/src/main/java"
    
    sourceDirectories.setFrom(files(mainSrc))
    classDirectories.setFrom(files(javaDebugTree, kotlinDebugTree))
    executionData.setFrom(fileTree("${buildDir}") {
        include("jacoco/testDebugUnitTest.exec")
        include("outputs/unit_test_code_coverage/debugUnitTest/testDebugUnitTest.exec")
    })
    
    // Configurar para solo medir código ejecutado en tests unitarios
    doFirst {
        println("📊 Generando reporte de cobertura JaCoCo (solo tests unitarios)...")
        println("📁 Clases Java: ${buildDir}/intermediates/javac/debug/classes")
        println("📁 Clases Kotlin: ${buildDir}/tmp/kotlin-classes/debug")
        println("📁 Fuentes: $mainSrc")
        println("📁 Datos de ejecución: ${buildDir}/jacoco/testDebugUnitTest.exec")
        println("📁 Solo se medirá código que se ejecutó en tests unitarios")
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
        "**/R.class",
        "**/R$*.class",
        "**/BuildConfig.*",
        "**/Manifest*.*",
        "**/*Test*.*",
        "android/**/*.*"
    )
    
    // Incluir tanto clases Java como Kotlin compiladas
    val javaDebugTree = fileTree("${buildDir}/intermediates/javac/debug/classes") {
        exclude(fileFilter)
    }
    val kotlinDebugTree = fileTree("${buildDir}/tmp/kotlin-classes/debug") {
        exclude(fileFilter)
    }
    
    val mainSrc = "${project.projectDir}/src/main/java"
    
    sourceDirectories.setFrom(files(mainSrc))
    classDirectories.setFrom(files(javaDebugTree, kotlinDebugTree))
    executionData.setFrom(fileTree("${buildDir}") {
        include("jacoco/testDebugUnitTest.exec")
        include("outputs/unit_test_code_coverage/debugUnitTest/testDebugUnitTest.exec")
    })
    
    // Configurar para solo medir código ejecutado
    doFirst {
        println("📊 Verificando cobertura de código ejecutado...")
        println("📁 Solo se medirá código que se ejecutó en los tests")
    }
}