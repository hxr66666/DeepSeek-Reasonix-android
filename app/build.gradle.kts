plugins {
    alias(libs.plugins.android.application)
//    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.reasonix.deepseek_reasonix_android"
    compileSdk = 37

    defaultConfig {
        applicationId = "com.reasonix.deepseek_reasonix_android"
        minSdk = 23
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
    buildFeatures {
        compose = true
    }
}

configurations.all {
    exclude(group = "org.jetbrains", module = "annotations-java5")
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons)
    implementation(libs.okhttp)
    implementation(libs.okhttp.sse)

    // ═══════════════════════════════════════════════
    // Markwon — 原生 Markdown 渲染引擎
    // ═══════════════════════════════════════════════
    implementation(libs.markwon.core)
    implementation(libs.markwon.syntax.highlight)
    implementation(libs.markwon.html)
    implementation(libs.markwon.image)
    implementation(libs.markwon.image.coil)
    implementation(libs.markwon.image.glide)
    implementation(libs.markwon.ext.strikethrough)
    implementation(libs.markwon.ext.tables)
    implementation(libs.markwon.ext.tasklist)
    implementation(libs.markwon.linkify)
    implementation(libs.markwon.inline.parser)
    implementation(libs.markwon.simple.ext)

    // Prism4j — 代码语法高亮
    implementation(libs.prism4j)
    compileOnly(libs.prism4j.bundler)
    annotationProcessor(libs.prism4j.bundler)

    // 图片加载库（Markwon image 插件需要）
    implementation(libs.coil)
    implementation(libs.glide) {
        exclude(group = "com.google.guava", module = "listenablefuture")
    }
    implementation(libs.picasso)

    implementation(libs.gson)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.core.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}