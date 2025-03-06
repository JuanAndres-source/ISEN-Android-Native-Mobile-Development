/**
 * Archivo de configuración Gradle para aplicación Android
 * Proyecto: ISEN Smart Companion
 * Versión: 1.0
 */

plugins {
    // Plugin principal para aplicaciones Android
    alias(libs.plugins.android.application)
    // Plugin para soporte de Kotlin en Android
    alias(libs.plugins.kotlin.android)
    // Plugin para Jetpack Compose con Kotlin
    alias(libs.plugins.kotlin.compose)
    // Kotlin Annotation Processing Tool - necesario para Room y otras bibliotecas
    id("kotlin-kapt")
}

/**
 * Configuración específica de Android
 */
android {
    // Espacio de nombres único para la aplicación
    namespace = "fr.isen.aliagafuentesjuanandres.isensmartcompanion"
    // SDK de compilación - Android 15 (API 35)
    compileSdk = 35

    // Configuración predeterminada de la aplicación
    defaultConfig {
        // ID de la aplicación en Google Play
        applicationId = "fr.isen.aliagafuentesjuanandres.isensmartcompanion"
        // SDK mínimo requerido - Android 7.1 (API 25)
        minSdk = 25
        // SDK objetivo - Android 15 (API 35)
        targetSdk = 35
        // Código de versión para actualizaciones
        versionCode = 1
        // Nombre de versión visible para usuarios
        versionName = "1.0"

        // Configuración para pruebas instrumentadas
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    // Configuración de tipos de construcción
    buildTypes {
        // Configuración para versión de producción
        release {
            // Desactivada la minificación de código
            isMinifyEnabled = false
            // Archivos ProGuard para optimización
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        // Aquí se podría añadir configuración para debug si fuera necesario
    }

    // Configuración de compatibilidad Java
    compileOptions {
        // Usar características de Java 11
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    // Opciones específicas de Kotlin
    kotlinOptions {
        // Target JVM para bytecode generado
        jvmTarget = "11"
    }

    // Características de construcción habilitadas
    buildFeatures {
        // Habilitar soporte para Jetpack Compose
        compose = true
    }
}

/**
 * Dependencias del proyecto
 * Organizadas por categorías para mejor mantenimiento
 */
dependencies {
    // ================= DEPENDENCIAS PRINCIPALES =================

    // Bibliotecas fundamentales de AndroidX
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // ================= JETPACK COMPOSE =================

    // Componentes base de Compose
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)

    // Material Design 3 para Compose
    implementation(libs.androidx.material3)

    // Iconos extendidos de Material Design
    implementation("androidx.compose.material:material-icons-extended:1.5.0")

    // ================= NAVEGACIÓN =================

    // Sistema de navegación para Compose
    implementation("androidx.navigation:navigation-compose:2.8.7")

    // ================= PRUEBAS UNITARIAS E INSTRUMENTADAS =================

    // Biblioteca JUnit para pruebas unitarias
    testImplementation(libs.junit)

    // Pruebas instrumentadas de AndroidX
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)

    // Herramientas de depuración para Compose
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // ================= NETWORKING Y SERIALIZACIÓN =================

    // Gson para serialización/deserialización JSON
    implementation("com.google.code.gson:gson:2.10.1")

    // Retrofit para peticiones HTTP
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // OkHttp para comunicación HTTP avanzada
    implementation("com.squareup.okhttp3:okhttp:4.10.0")

    // ================= INTELIGENCIA ARTIFICIAL =================

    // Cliente Gemini AI de Google
    implementation("com.google.ai.client.generativeai:generativeai:0.4.0")

    // ================= CORRUTINAS =================

    // Soporte de corrutinas para operaciones asíncronas
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // ================= PERSISTENCIA DE DATOS =================

    // Room para base de datos SQLite
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")

    // Preferencias compartidas
    implementation("androidx.preference:preference-ktx:1.1.1")

    // ================= TAREAS EN SEGUNDO PLANO =================

    // WorkManager para gestión de tareas en segundo plano
    implementation("androidx.work:work-runtime-ktx:2.7.1")

    // ================= NOTIFICACIONES =================

    // Soporte para sistema de notificaciones
    implementation("androidx.core:core-ktx:1.8.0")

    // ================= COMPONENTES DE INTERFAZ ADICIONALES =================

    // Componentes Material 3 (versión alpha para características avanzadas)
    implementation("androidx.compose.material3:material3:1.2.0-alpha10")
}