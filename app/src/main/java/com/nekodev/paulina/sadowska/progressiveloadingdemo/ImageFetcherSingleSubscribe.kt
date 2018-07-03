package com.nekodev.paulina.sadowska.progressiveloadingdemo

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import io.reactivex.SingleEmitter
import io.reactivex.SingleOnSubscribe
import java.lang.Exception

/**
 * Created by Paulina Sadowska on 03.07.2018.
 */
class ImageFetcherSingleSubscribe(private val picasso: Picasso, private val url: String, private val quality: Int) : SingleOnSubscribe<FetchedBitmapWithQuality> {

    private val runningTargets = mutableListOf<Target>()

    override fun subscribe(emitter: SingleEmitter<FetchedBitmapWithQuality>) {
        val target = object : Target {
            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                //do nothing
            }

            override fun onBitmapLoaded(bitmap: Bitmap, from: Picasso.LoadedFrom) {
                emitter.onSuccess(FetchedBitmapWithQuality(bitmap, from, quality))
                removeTargetAndCancelRequest(this)
            }

            override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                emitter.tryOnError(ImageNotFetchedException(e?.message ?: "image not fetched"))
                removeTargetAndCancelRequest(this)
            }
        }
        runningTargets.add(target)
        emitter.setCancellable { removeTargetAndCancelRequest(target) }
        picasso.load(url).into(target)
    }

    private fun removeTargetAndCancelRequest(target: Target) {
        picasso.cancelRequest(target)
        runningTargets.remove(target)
    }
}