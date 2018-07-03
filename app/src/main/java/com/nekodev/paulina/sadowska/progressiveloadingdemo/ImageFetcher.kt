package com.nekodev.paulina.sadowska.progressiveloadingdemo

import com.squareup.picasso.Picasso
import io.reactivex.Observable
import io.reactivex.Single

/**
 * Created by Paulina Sadowska on 03.07.2018.
 */
class ImageFetcher(private val picasso: Picasso) {

    fun loadProgressively(url: String, badQuality: Int, bestQuality: Int): Observable<FetchedBitmapWithQuality> {
        val smallestImage = ImageUrlWithSize(url, badQuality)
        val mediumImage = ImageUrlWithSize(url, bestQuality)

        return loadProgressively(smallestImage, mediumImage)
    }

    private fun loadProgressively(smallImage: ImageUrlWithSize, mediumImage: ImageUrlWithSize): Observable<FetchedBitmapWithQuality> {
        return Observable.merge<FetchedBitmapWithQuality>(
                loadImageAndIgnoreError(smallImage),
                loadImageAndIgnoreError(mediumImage))
    }

    private fun loadImageAndIgnoreError(image: ImageUrlWithSize): Observable<FetchedBitmapWithQuality> {
        return loadImageAndSetBitmap(image).toObservable().onErrorResumeNext(Observable.empty<FetchedBitmapWithQuality>())
    }

    private fun loadImageAndSetBitmap(image: ImageUrlWithSize): Single<FetchedBitmapWithQuality> {
        return Single.create(ImageFetcherSingleSubscribe(picasso, image.urlWithSize, image.size))
    }

    private class ImageUrlWithSize(url: String, val size: Int) {
        val urlWithSize = "$url/$size/$size?image=0" //?image=0 added so image wont be random
    }
}

