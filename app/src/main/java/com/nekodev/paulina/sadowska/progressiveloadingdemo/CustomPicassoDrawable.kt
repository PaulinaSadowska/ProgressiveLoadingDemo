package com.nekodev.paulina.sadowska.progressiveloadingdemo

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.Rect
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.SystemClock
import android.widget.ImageView
import com.squareup.picasso.Picasso

/**
 * Created by Paulina Sadowska on 03.07.2018.
 */
class CustomPicassoDrawable private constructor(context: Context,
                                                bitmap: Bitmap,
                                                placeholder: Drawable?,
                                                loadedFrom: Picasso.LoadedFrom,
                                                noFade: Boolean) : BitmapDrawable(context.resources, bitmap) {

    private var placeholder: Drawable? = null
    private var startTimeMillis: Long = 0
    private var animating: Boolean = false
    private var drawableAlpha = 255

    init {
        val fade = loadedFrom != Picasso.LoadedFrom.MEMORY && !noFade
        if (fade) {
            this.placeholder = placeholder
            this.animating = true
            this.startTimeMillis = SystemClock.uptimeMillis()
        }
    }

    companion object {

        fun setBitmap(target: ImageView, context: Context, bitmap: Bitmap, loadedFrom: Picasso.LoadedFrom, noFade: Boolean) {
            val placeholder = target.drawable
            if (placeholder is AnimationDrawable) {
                placeholder.stop()
            }

            val drawable = CustomPicassoDrawable(context, bitmap, placeholder, loadedFrom, noFade)
            target.setImageDrawable(drawable)
        }
    }

    override fun draw(canvas: Canvas) {
        if (!this.animating) {
            super.draw(canvas)
        } else {
            val normalized = (SystemClock.uptimeMillis() - this.startTimeMillis).toFloat() / 200.0f
            if (normalized >= 1.0f) {
                this.animating = false
                this.placeholder = null
                super.draw(canvas)
            } else {
                this.placeholder?.draw(canvas)
                val partialAlpha = (this.drawableAlpha.toFloat() * normalized).toInt()
                super.setAlpha(partialAlpha)
                super.draw(canvas)
                super.setAlpha(this.drawableAlpha)
            }
        }
    }

    override fun setAlpha(alpha: Int) {
        this.drawableAlpha = alpha
        this.placeholder?.alpha = alpha
        super.setAlpha(alpha)
    }

    override fun setColorFilter(cf: ColorFilter?) {
        this.placeholder?.colorFilter = cf
        super.setColorFilter(cf)
    }

    override fun onBoundsChange(bounds: Rect) {
        this.placeholder?.bounds = bounds
        super.onBoundsChange(bounds)
    }
}