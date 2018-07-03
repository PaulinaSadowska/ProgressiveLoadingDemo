package com.nekodev.paulina.sadowska.progressiveloadingdemo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Picasso.get()
                .load("http://i.imgur.com/DvpvklR.png")
                .into(imageView)
    }
}
