import com.codingfeline.buildkonfig.compiler.FieldSpec
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
    alias(libs.plugins.buildkonfig)
}

kotlin {
    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    )

    android {
        namespace = "com.example.apodpet.core"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()

        compilerOptions {
            jvmTarget = JvmTarget.JVM_11
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.ktor.client.okhttp)
        }
        commonMain.dependencies {
            implementation(libs.koin.core)
            implementation(libs.bundles.ktor)
            implementation(libs.bundles.room)
            implementation(libs.kotlinx.datetime)
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.ios)
        }
    }
}

buildkonfig {
    packageName = "com.example.apodpet.core"

    defaultConfigs {
        buildConfigField(FieldSpec.Type.STRING, "BASE_URL", "api.nasa.gov")
        buildConfigField(FieldSpec.Type.STRING, "NASA_API_KEY", "DEMO_KEY")
    }
    defaultConfigs("dev") {
        buildConfigField(FieldSpec.Type.STRING, "BASE_URL", "api.nasa.gov")
        buildConfigField(FieldSpec.Type.STRING, "NASA_API_KEY", "DEMO_KEY")
    }
    defaultConfigs("stage") {
        buildConfigField(FieldSpec.Type.STRING, "BASE_URL", "api.nasa.gov")
        buildConfigField(FieldSpec.Type.STRING, "NASA_API_KEY", "DEMO_KEY")
    }
}

dependencies {
    add("kspAndroid", libs.room.compiler)
    add("kspIosSimulatorArm64", libs.room.compiler)
    add("kspIosArm64", libs.room.compiler)
}

room {
    schemaDirectory("$projectDir/schemas")
}
