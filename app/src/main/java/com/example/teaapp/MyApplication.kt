package com.example.teaapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Custom [Application] class annotated with [HiltAndroidApp] to enable dependency injection
 * using Hilt throughout the application.
 */

@HiltAndroidApp
class MyApplication : Application()  {
}