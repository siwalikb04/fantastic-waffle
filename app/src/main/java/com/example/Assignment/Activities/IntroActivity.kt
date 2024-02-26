package com.example.Assignment.Activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.project155.R

class IntroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)
        val getinBtn = findViewById<Button>(R.id.getInBtn)
        getinBtn.setOnClickListener {
            startActivity(
                Intent(
                    this@IntroActivity,
                    LoginActivity::class.java
                )
            )
        }
    }
}