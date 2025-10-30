plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.protobuf")
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

    // --- DEPENDENCIAS PRINCIPALES (Implementation) ---
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.navigation.compose)
    implementation("androidx.compose.ui:ui-text-google-fonts:1.6.8") // Usando una versión estable de Compose si no está en BOM

    // Retrofit & Networking
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // Coroutines & ViewModel
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")

    // --- DEBUG (Tooling & Manifest) ---
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    // Dejo libs.androidx.compose.ui.tooling.preview ya que viene de libs

    // --- TESTS UNITARIOS (testImplementation) ---
    testImplementation(libs.junit) // Asumiendo que libs.junit es 4.13.2
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
    testImplementation("org.mockito:mockito-core:5.12.0") // Mocks para JVM
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.1") // Coroutines Testing
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.3.1") // Sintaxis más limpia para Mockito en Kotlin
    // testImplementation(kotlin("test")) - Generalmente no es necesario si usas JUnit

    // --- TESTS DE INSTRUMENTACIÓN (androidTestImplementation) ---
    androidTestImplementation(libs.androidx.junit) // androidx.test.ext:junit
    androidTestImplementation(libs.androidx.espresso.core) // androidx.test.espresso:espresso-core
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    // Si realmente necesitas mockito en instrumentación:
    androidTestImplementation("org.mockito:mockito-android:4.8.0")
    implementation("androidx.compose.ui:ui-text-google-fonts:1.9.0")
    implementation("androidx.datastore:datastore-preferences:1.0.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    // Reemplaza X.Y.Z con la última versión
    implementation("com.google.maps.android:maps-compose:4.3.0")
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    implementation("com.google.protobuf:protobuf-kotlin-lite:3.25.1") // Última versión
    implementation("com.google.protobuf:protobuf-javalite:3.25.1")

    implementation("com.google.protobuf:protobuf-kotlin-lite:4.33.0") // Última versión
    implementation("com.google.protobuf:protobuf-javalite:4.33.0")

    // Dependencia de Proto DataStore
    implementation("androidx.datastore:datastore:1.1.7")


}

protobuf {
    protoc {
        // Usa la misma versión de protoc que en las dependencias
        artifact = "com.google.protobuf:protoc:4.33.0"
    }
    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                // Generar código Kotlin (y Java Lite)
                create("kotlin") {
                    option("lite")
                }
                create("java") {
                    option("lite")
                }
            }
        }
    }
}
