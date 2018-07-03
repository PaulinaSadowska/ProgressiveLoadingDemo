package com.nekodev.paulina.sadowska.progressiveloadingdemo

import android.graphics.Bitmap
import com.squareup.picasso.Picasso

/**
 * Created by Paulina Sadowska on 03.07.2018.
 */
data class FetchedBitmapWithQuality(val bitmap: Bitmap,
                                    val loadedFrom: Picasso.LoadedFrom,
                                    val size: Int)