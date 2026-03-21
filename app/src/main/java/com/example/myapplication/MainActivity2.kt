package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main2)  // XML with only one button

        val viewResumeButton = findViewById<Button>(R.id.viewResumeButton)
        viewResumeButton.setOnClickListener {
            startActivity(Intent(this, Resume::class.java))
        }
    }
}

