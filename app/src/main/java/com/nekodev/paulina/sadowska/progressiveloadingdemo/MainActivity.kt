package com.nekodev.paulina.sadowska.progressiveloadingdemo

import android.arch.lifecycle.Observer
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.nekodev.paulina.sadowska.progressiveloadingdemo.fetcher.data.BitmapResult
import com.nekodev.paulina.sadowska.progressiveloadingdemo.fetcher.data.ResponseState
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val viewModel = ImageViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel.bitmapResult.observe(this, Observer<BitmapResult> { it -> process(it) })
        viewModel.loadImages(listOf(3000, 10, 300))
    }

    private fun process(result: BitmapResult?) {
        result?.let {
            when (it.state) {
                ResponseState.LOADING -> {
                    showProgress()
                }
                ResponseState.ERROR -> {
                    hideProgress()
                    showError()
                }
                ResponseState.SUCCESS -> {
                    hideProgress()
                    it.bitmap?.let { bitmap ->
                        showImage(bitmap)
                    }
                }
            }
        }
    }

    private fun showImage(bitmap: Bitmap) {
        imageView.setImageBitmap(bitmap)
    }

    private fun showProgress() {
        loader.visibility = View.VISIBLE
    }

    private fun hideProgress() {
        loader.visibility = View.GONE
    }

    private fun showError() {
        errorText.visibility = View.VISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.unSubscribe()
    }
}
