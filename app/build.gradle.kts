plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
    id("com.google.dagger.hilt.android")

}

android {
    namespace = "com.app.financialmanagement"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.app.financialmanagement"
        minSdk = 24
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures{
        viewBinding =true
        dataBinding=true
    }
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.firebase:firebase-auth:22.3.1")
    implementation("com.google.firebase:firebase-database:20.3.1")
    implementation("com.google.firebase:firebase-messaging:23.4.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation(platform("com.google.firebase:firebase-bom:32.7.3"))
    implementation("com.google.firebase:firebase-analytics")
    implementation ("com.github.blackfizz:eazegraph:1.2.5l@aar")
    implementation ("com.nineoldandroids:library:2.4.0")

    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    //worker
    implementation ("androidx.work:work-runtime:2.8.1")

    //bubble navigation bar
    implementation ("io.ak1:bubbletabbar:1.0.8")


    //navigation component
    implementation ("androidx.navigation:navigation-fragment:2.3.5")
    implementation ("androidx.navigation:navigation-ui:2.3.5")

    //circle image
    implementation ("de.hdodenhof:circleimageview:3.1.0")

    //sweetalert dialog
    implementation ("com.github.f0ris.sweetalert:library:1.6.2")

    //glide
    implementation ("com.github.bumptech.glide:glide:4.15.1")

    //sdp
    implementation ("com.intuit.sdp:sdp-android:1.1.0")

    //MVVM
    implementation ("androidx.lifecycle:lifecycle-viewmodel:2.3.1")
    implementation ("androidx.lifecycle:lifecycle-livedata:2.3.1")
    //dagger hilt
    implementation ("com.google.dagger:hilt-android:2.44")
    annotationProcessor ("com.google.dagger:hilt-compiler:2.44")
    //graph
    implementation ("com.halfhp.fig:figlib:1.0.11")
    implementation ("com.androidplot:androidplot-core:1.5.7")
    //worker
    implementation ("androidx.work:work-runtime:2.8.1")
}