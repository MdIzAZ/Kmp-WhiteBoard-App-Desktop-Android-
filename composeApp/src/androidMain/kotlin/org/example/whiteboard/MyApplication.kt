package org.example.whiteboard

import android.app.Application
import org.example.whiteboard.di.initKoin
import org.example.whiteboard.presentation.whiteboard.component.initClipboardHelper
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidLogger()
            androidContext(this@MyApplication)
        }
        initClipboardHelper(this)
    }
}