package com.nekodev.paulina.sadowska.progressiveloadingdemo

import android.arch.lifecycle.MutableLiveData
import com.nekodev.paulina.sadowska.progressiveloadingdemo.fetcher.ImageFetcher
import com.nekodev.paulina.sadowska.progressiveloadingdemo.fetcher.data.BitmapWithQuality
import com.nekodev.paulina.sadowska.progressiveloadingdemo.fetcher.data.BitmapResult
import com.squareup.picasso.Picasso
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy

class ImageViewModel {

    companion object {
        private const val IMAGE_URL = "https://picsum.photos"
    }

    private val disposable = CompositeDisposable()
    private val fetcher = ImageFetcher(Picasso.get())

    val bitmapResult = MutableLiveData<BitmapResult>()

    fun loadImages(qualities: List<Int>) {
        bitmapResult.value = BitmapResult.loading()
        disposable.add(fetcher.loadProgressively(IMAGE_URL, qualities)
                .subscribeBy(
                        onNext = { applyImageIfHasBetterQuality(it) },
                        onError = { postError() },
                        onComplete = { postErrorIfShould() }
                ))
    }


    private fun applyImageIfHasBetterQuality(bitmap: BitmapWithQuality) {
        if (getCurrentQuality() < bitmap.quality) {
            bitmapResult.value = BitmapResult.success(bitmap)
        }
    }

    private fun getCurrentQuality(): Int {
        return bitmapResult.value?.quality ?: -1
    }

    private fun postErrorIfShould() {
        if (getCurrentQuality() < 0) {
            postError()
        }
    }

    private fun postError() {
        bitmapResult.value = BitmapResult.error()
    }

    fun unSubscribe() {
        disposable.dispose()
    }
}