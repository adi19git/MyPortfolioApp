package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btn = findViewById<Button>(R.id.knowme)

        // Load button animation
        val animation = AnimationUtils.loadAnimation(this, R.anim.button_entry)
        btn.startAnimation(animation)

        // OnClick event
        btn.setOnClickListener {
            btn.isEnabled = false
            val intent = Intent(this, Myprofile::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            Toast.makeText(this, "Welcome to MyPortfolio", Toast.LENGTH_LONG).show()
        }

        // ✅ Get Firebase token for push notifications
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("FCM", "Fetching FCM registration token failed", task.exception)
                return@addOnCompleteListener
            }

            val token = task.result
            Log.d("FCM", "Device Token: $token")
        }

        // ✅ Subscribe this device to "all" topic
        FirebaseMessaging.getInstance().subscribeToTopic("all")
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("FCM", "✅ Subscribed to FCM topic: all")
                } else {
                    Log.w("FCM", "❌ Subscription failed", task.exception)
                }
            }
    }
}
