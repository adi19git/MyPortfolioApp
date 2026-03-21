package com.example.myapplication

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Feedback : AppCompatActivity() {

    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback)

        val nameInput = findViewById<EditText>(R.id.nameInput)
        val emailInput = findViewById<EditText>(R.id.emailInput)
        val feedbackInput = findViewById<EditText>(R.id.feedbackInput)
        val submitBtn = findViewById<Button>(R.id.submitBtn)

        // Initialize Firebase reference
        database = FirebaseDatabase.getInstance().getReference("Feedbacks")

        // Add glow effect on focus
        val highlightColor = ContextCompat.getColor(this, R.color.purple_200)
        listOf(nameInput, emailInput, feedbackInput).forEach { editText ->
            editText.setOnFocusChangeListener { view, hasFocus ->
                val colorFrom =
                    if (hasFocus) ContextCompat.getColor(this, android.R.color.transparent) else highlightColor
                val colorTo =
                    if (hasFocus) highlightColor else ContextCompat.getColor(this, android.R.color.transparent)

                ObjectAnimator.ofObject(
                    view,
                    "backgroundColor",
                    ArgbEvaluator(),
                    colorFrom,
                    colorTo
                ).apply {
                    duration = 400
                    start()
                }
            }
        }

        submitBtn.setOnClickListener {
            val name = nameInput.text.toString().trim()
            val email = emailInput.text.toString().trim()
            val message = feedbackInput.text.toString().trim()

            // Input validation
            if (name.isEmpty() || email.isEmpty() || message.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Validate email format
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Hide keyboard
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)

            // Send feedback to Firebase
            val feedbackId = database.push().key
            if (feedbackId != null) {
                val feedback = FeedbackData(name, email, message)
                database.child(feedbackId).setValue(feedback)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Thanks for your feedback, $name!", Toast.LENGTH_LONG).show()
                        nameInput.text.clear()
                        emailInput.text.clear()
                        feedbackInput.text.clear()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Failed to send feedback", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this, "Unable to generate feedback ID", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

// Data class for Firebase
data class FeedbackData(
    val name: String = "",
    val email: String = "",
    val message: String = ""
)
