package com.example.learning

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        addActivityButton(NotificationActivity::class.java, "Notification")
    }



    private fun addActivityButton(c: Class<*>, description: String) {
        val button = Button(this)
        button.text = description
        button.setOnClickListener { startActivity(Intent(this@MainActivity, c)) }
        ll_content?.addView(button)
    }
}