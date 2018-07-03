package com.nekodev.paulina.sadowska.progressiveloadingdemo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.squareup.picasso.Picasso
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val compositeDisposable = CompositeDisposable()
    private val fetcher: ImageFetcher
    private val bitmapApplier = FetchedBitmapApplier()
    private var currentQuality = -1

    init {
        val picasso = Picasso.get()
        picasso.isLoggingEnabled = true
        fetcher = ImageFetcher(picasso)
    }

    companion object {
        private const val IMAGE_URL = "https://picsum.photos"
        private const val BAD_QUALITY = 100
        private const val BEST_QUALITY = 2000
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadImages()
    }

    private fun loadImages() {
        compositeDisposable.add(fetcher.loadProgressively(IMAGE_URL, BAD_QUALITY, BEST_QUALITY)
                .doOnSubscribe { startLoading() }
                .subscribeBy(
                        onNext = { applyImageIfHasBetterQuality(it) },
                        onError = { showError() },
                        onComplete = { showErrorIfShould() }
                ))
    }

    private fun applyImageIfHasBetterQuality(fetchedBitmap: FetchedBitmapWithQuality) {
        stopLoading()
        if (currentQuality < fetchedBitmap.size) {
            currentQuality = fetchedBitmap.size
            bitmapApplier.applyBitmap(imageView, fetchedBitmap.bitmap, fetchedBitmap.loadedFrom)
        }
    }

    private fun startLoading() {
        loader.visibility = View.VISIBLE
    }

    private fun stopLoading() {
        loader.visibility = View.GONE
    }

    private fun showErrorIfShould() {
        if (currentQuality < 0) {
            showError()
        }
    }

    private fun showError() {
        errorText.visibility = View.VISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }
}
