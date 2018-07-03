package com.nekodev.paulina.sadowska.progressiveloadingdemo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.squareup.picasso.Picasso
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val disposable = CompositeDisposable()
    private val fetcher : ImageFetcher

    init{
        val picasso = Picasso.get()
        picasso.isLoggingEnabled = true
        fetcher = ImageFetcher(picasso)
    }

    companion object {
        private const val IMAGE_URL = "https://picsum.photos"
        private const val BAD_QUALITY = 100
        private const val BEST_QUALITY = 3000
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        disposable.add(fetcher.loadImagesFromBuckets(IMAGE_URL, BAD_QUALITY, BEST_QUALITY)
                .subscribeBy(
                        onNext = { FetchedBitmapApplier().applyBitmap(imageView, it.bitmap, it.loadedFrom) },
                        onError = { showError() }
                ))
    }

    private fun showError() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }
}
