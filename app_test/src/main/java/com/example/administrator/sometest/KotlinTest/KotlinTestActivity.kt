package com.example.administrator.sometest.KotlinTest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log

import com.example.administrator.sometest.R

class KotlinTestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kotlin_test)
        var fistK = FirstKotlin(1, 7)
        Log.e("yang", "fistk: " + fistK.add(1, 5))
        Log.e("yang", "fistk: " + fistK.getNum())
        Log.e("yang", "fistk: " + fistK.time)
    }
}
