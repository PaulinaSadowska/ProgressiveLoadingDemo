package com.nekodev.paulina.sadowska.progressiveloadingdemo

import com.squareup.picasso.Picasso
import io.reactivex.Observable
import io.reactivex.Single

/**
 * Created by Paulina Sadowska on 03.07.2018.
 */
class ImageFetcher(private val picasso: Picasso) {

    fun loadProgressively(url: String, qualities: List<Int>): Observable<FetchedBitmapWithQuality> {
        return qualities
                .map { quality -> Pair(createUrl(url, quality), quality) }
                .map { loadImageAndIgnoreError(it) }
                .reduce { o1, o2 -> Observable.merge(o1, o2) }
    }

    private fun loadImageAndIgnoreError(urlWithQuality: Pair<String, Int>): Observable<FetchedBitmapWithQuality> {
        val (url, quality) = urlWithQuality
        return Single
                .create(ImageFetcherSingleSubscribe(picasso, url, quality))
                .toObservable()
                .onErrorResumeNext(Observable.empty<FetchedBitmapWithQuality>())
    }

    private fun createUrl(url: String, size: Int): String = "$url/$size/$size?image=0" //?image=0 added so image wont be random
}

