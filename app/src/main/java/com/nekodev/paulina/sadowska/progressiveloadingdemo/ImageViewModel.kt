package com.nekodev.paulina.sadowska.progressiveloadingdemo

import android.arch.lifecycle.MutableLiveData
import com.nekodev.paulina.sadowska.progressiveloadingdemo.fetcher.ImageFetcher
import com.nekodev.paulina.sadowska.progressiveloadingdemo.fetcher.data.BitmapResult
import com.nekodev.paulina.sadowska.progressiveloadingdemo.fetcher.data.BitmapWithQuality
import com.squareup.picasso.Picasso
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy

class ImageViewModel {

    companion object {
        private const val BASE_IMAGE_URL = "https://picsum.photos"
    }

    private val disposable = CompositeDisposable()
    private val fetcher = ImageFetcher(Picasso.get())

    val bitmapResult = MutableLiveData<BitmapResult>()

    fun loadImages(qualities: List<Int>) {
        bitmapResult.value = BitmapResult.loading()
        disposable.add(fetcher.loadProgressively(BASE_IMAGE_URL, qualities)
                .filter { getCurrentQuality() < it.quality }
                .subscribeBy(
                        onNext = { applyImage(it) },
                        onComplete = { postErrorIfNotSufficientQuality() }
                ))
    }

    private fun getCurrentQuality(): Int {
        return bitmapResult.value?.quality ?: -1
    }

    private fun applyImage(bitmap: BitmapWithQuality) {
        bitmapResult.value = BitmapResult.success(bitmap)
    }
    
    private fun postErrorIfNotSufficientQuality() {
        if (getCurrentQuality() < 0) {
            bitmapResult.value = BitmapResult.error()
        }
    }

    fun unSubscribe() {
        disposable.dispose()
    }
}