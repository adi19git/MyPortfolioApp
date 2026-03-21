package com.example.myapplication

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class Resume : AppCompatActivity() {

    private lateinit var resumeBitmap: Bitmap
    private var savedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resume)

        val resumeImageView = findViewById<ImageView>(R.id.resumeImage)
        val downloadBtn = findViewById<Button>(R.id.downloadBtn)
        val shareBtn = findViewById<Button>(R.id.shareBtn)
        val feedbackBtn = findViewById<Button>(R.id.feedbackBtn)

        // Load resume image
        resumeBitmap = BitmapFactory.decodeResource(resources, R.drawable.resume)
        resumeImageView.setImageBitmap(resumeBitmap)

        downloadBtn.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch { saveImageSafely() }
        }

        shareBtn.setOnClickListener {
            savedImageUri?.let { shareResume(it) }
                ?: Toast.makeText(this, "Download the resume first.", Toast.LENGTH_SHORT).show()
        }

        feedbackBtn.setOnClickListener {
            startActivity(Intent(this, Feedback::class.java))
        }
    }

    private suspend fun saveImageSafely() {
        val filename = "resume_${System.currentTimeMillis()}.jpeg"
        val file = File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), filename)
        try {
            FileOutputStream(file).use { stream ->
                resumeBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            }
            savedImageUri = FileProvider.getUriForFile(
                this,
                "${applicationContext.packageName}.provider",
                file
            )
            withContext(Dispatchers.Main) {
                Toast.makeText(this@Resume, "Saved to: ${file.absolutePath}", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(this@Resume, "Failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun shareResume(uri: Uri) {
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "image/jpeg"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        startActivity(Intent.createChooser(shareIntent, "Share Resume via"))
    }
}
