package com.xg.animateapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var num = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_plus.setOnClickListener {
            num += 1
            img_view.setText(num.toString())
        }

        btn_sub.setOnClickListener {
            num -= 1
            img_view.setText(num.toString())
        }

        btn_clean.setOnClickListener {
            img_view.setText("")
            num = 0
        }
    }
}
