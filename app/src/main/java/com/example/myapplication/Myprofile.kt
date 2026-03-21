package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri  // <-- KTX extension import

class Myprofile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_myprofile)

        // Portfolio button → MainActivity2
        findViewById<Button>(R.id.details).setOnClickListener {
            startActivity(Intent(this, MainActivity2::class.java))
        }

        // LinkedIn button → open LinkedIn profile
        findViewById<ImageButton>(R.id.linkedinButton).setOnClickListener {
            val intent = Intent(
                Intent.ACTION_VIEW,
                "https://www.linkedin.com/in/aditya-kumar-6634a92a5".toUri()
            )
            startActivity(intent)
        }

        // GitHub button → open GitHub profile
        findViewById<ImageButton>(R.id.githubButton).setOnClickListener {
            val intent = Intent(
                Intent.ACTION_VIEW,
                "https://github.com/adi19git".toUri()
            )
            startActivity(intent)
        }
    }
}
