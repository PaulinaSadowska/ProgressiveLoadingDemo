package com.nekodev.paulina.sadowska.progressiveloadingdemo

import android.app.Application
import com.squareup.picasso.Picasso
import io.reactivex.plugins.RxJavaPlugins

class ImageLoadingApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        RxJavaPlugins.setErrorHandler(RxJavaErrorHandler())
        Picasso.get().isLoggingEnabled = true
    }
}
