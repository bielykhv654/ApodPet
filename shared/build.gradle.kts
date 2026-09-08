import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.serialization)
}

kotlin {
    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "Shared"
            isStatic = true
            export(libs.decompose)
            export(libs.lifecycle)
            export(libs.essenty.backHandler)
        }
    }
    
    android {
       namespace = "com.example.apodpet.shared"
       compileSdk = libs.versions.android.compileSdk.get().toInt()
       minSdk = libs.versions.android.minSdk.get().toInt()
    
       compilerOptions {
           jvmTarget = JvmTarget.JVM_11
       }
       androidResources {
           enable = true
       }
       withHostTest {
           isIncludeAndroidResources = true
       }
    }
    
    sourceSets {
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.bundles.mvi)
            implementation(libs.decompose.extensions.compose)
            implementation(libs.koin.core)
            implementation(libs.bundles.coil)
            implementation(project(":core"))
            api(libs.decompose)
            api(libs.lifecycle)
            api(libs.essenty.backHandler)
            implementation(libs.kotlinx.datetime)
            implementation(libs.bundles.paging)
            implementation(libs.mediaplayer)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}
