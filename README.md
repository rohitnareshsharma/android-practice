# Goal

Make a single screen application to list all the trending git hub repositories
fetched from a public api

http://rohitsharma.org/practice-debug.apk

# Apk Installation Command (Correct download location if its different for you)
adb install -r -t ~/Downloads/practice-debug.apk

# Launcher Activity

TrendingReposActivity.java

# Build Details

Directly import this as an android project in android studio. It should do the needful.
In case of issues. Try cleaning build and .idea related files.

# Test Cases

Run Instrumentation and Unit Test in the project via standard ways.
We have respectively 4,5 test cases for both unit and ui category.

#Architectural Components Used

1. MVVM Design Style
2. Dagger for Dependency Injection
3. Data Binding for UI data linking.
4. Espresso, RoboElectric, JUnit, MockWebServer for Testing
5. EasyVolley for Networking


# 2 Hour Cache Requirement

There is nothing specifically done for this requirement.
As network lib of ours do this internally for us based on the
cache-header of response. It should never be a client side driven
mechanism. Although if we still want to do this client side. We
can do preference based work. But it's not done in this assignment.

# EasyVolley for Networking

EasyVolley is my own customised version of Volley. In which i have
coupled it with GSON to do the TypeCasting for us internally.
It uses Volley 1.1.0 which has HurlStack using device HttpUrlConnection class.
Which i suppose is based on okhttp engine only. So should work fine.

Its earlier version had okhttp3 client directly used in it.
But latest version of okhttp-urlconnection has deprecated the OkClientFactory Class.
Because of its conflict with latest mock-webserver dependency.
I made it use the default HurlStack for now. Till i create BaseHttpStack Based
network client for my lib which then can leverage all the benefits of latest okhttp3 clients.

Molecular level customisation is possible using this. Check the Repo Link

https://github.com/rohitnareshsharma/easyvolley


# All Dependencies of this project

    // Standard android
    implementation 'androidx.appcompat:appcompat:1.1.0'
    implementation 'androidx.lifecycle:lifecycle-extensions:2.2.0-rc02'
    implementation "androidx.recyclerview:recyclerview:1.0.0"
    implementation 'androidx.constraintlayout:constraintlayout:1.1.3'

    // For control over item selection of both touch and mouse driven selection
    implementation "androidx.recyclerview:recyclerview-selection:1.0.0"

    // Added Glide for Image Operations
    implementation 'com.github.bumptech.glide:glide:4.10.0'
    annotationProcessor 'com.github.bumptech.glide:compiler:4.10.0'

    // For Networking
    implementation 'com.spandexship:easyvolley:0.5.2'

    // For Shimmer Animation
    implementation 'com.facebook.shimmer:shimmer:0.5.0'

    // Dependency injection
    api 'com.google.dagger:dagger:2.25.2'
    annotationProcessor 'com.google.dagger:dagger-compiler:2.25.2'

    // Unit Tests
    testImplementation 'junit:junit:4.12'
    testImplementation 'com.squareup.okhttp3:mockwebserver:4.2.1'
    testImplementation 'androidx.test:core:1.2.0'
    testImplementation 'org.mockito:mockito-core:2.19.0'
    testImplementation 'androidx.arch.core:core-testing:2.1.0'
    testImplementation "org.robolectric:robolectric:4.3.1"
    testImplementation 'org.apache.httpcomponents:httpcore:4.4.12'
    testImplementation 'com.google.code.gson:gson:2.8.6'


    // Instrumentation
    androidTestImplementation 'com.squareup.okhttp3:mockwebserver:4.2.1'
    androidTestImplementation 'androidx.test:runner:1.2.0'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.2.0'
    androidTestImplementation 'androidx.arch.core:core-testing:2.1.0'
    androidTestImplementation 'com.android.support.test.espresso:espresso-contrib:3.0.2'
    androidTestImplementation 'androidx.test:rules:1.3.0-alpha02'


# Default Project Settings

applicationId "com.practice"
minSdkVersion 19
targetSdkVersion 28
versionCode 1
versionName "1.0"








