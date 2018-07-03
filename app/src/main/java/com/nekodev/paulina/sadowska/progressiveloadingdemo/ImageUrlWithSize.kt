package com.nekodev.paulina.sadowska.progressiveloadingdemo

/**
 * Created by Paulina Sadowska on 03.07.2018.
 */
class ImageUrlWithSize(url: String, val size: Int) {

    val urlWithSize = "$url/$size/$size"

}