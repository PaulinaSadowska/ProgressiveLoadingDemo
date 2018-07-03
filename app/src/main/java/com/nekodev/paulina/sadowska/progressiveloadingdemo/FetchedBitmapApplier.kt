package com.nekodev.paulina.sadowska.progressiveloadingdemo

import android.graphics.Bitmap
import android.widget.ImageView
import com.squareup.picasso.Picasso

/**
 * Created by Paulina Sadowska on 03.07.2018.
 */
class FetchedBitmapApplier{

    fun applyBitmap(imageView: ImageView, bitmap: Bitmap, loadedFrom: Picasso.LoadedFrom) {
        CustomPicassoDrawable.setBitmap(imageView, imageView.context, bitmap, loadedFrom, shouldFade(imageView))
    }

    private fun shouldFade(imageView: ImageView): Boolean {
        return imageView.drawable == null
    }
}