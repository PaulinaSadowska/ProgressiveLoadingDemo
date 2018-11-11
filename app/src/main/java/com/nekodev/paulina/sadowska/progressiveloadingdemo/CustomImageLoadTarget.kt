package com.nekodev.paulina.sadowska.progressiveloadingdemo

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import io.reactivex.SingleEmitter

class CustomImageLoadTarget(private val emitter: SingleEmitter<BitmapWithQuality>,
                            private val quality: Int,
                            private val unSubscribe: (Target) -> Unit) : Target {

    init {
        emitter.setCancellable { unSubscribe(this) }
    }

    override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
        //do nothing
    }

    override fun onBitmapLoaded(bitmap: Bitmap, from: Picasso.LoadedFrom) {
        emitter.onSuccess(BitmapWithQuality(bitmap, quality))
        unSubscribe(this)
    }

    override fun onBitmapFailed(e: Exception, errorDrawable: Drawable?) {
        emitter.tryOnError(e)
        unSubscribe(this)
    }
}