package com.muflidevs.textrecognition

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.muflidevs.textrecognition.databinding.ActivityDetailBinding

class ResultActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imageUri = Uri.parse(intent.getStringExtra(EXTRA_IMAGE_URI))
        imageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.imgresult.setImageURI(it)
        }
    }
    companion object {
        const val EXTRA_IMAGE_URI = "extra_image_uri"
        const val EXTRA_RESULT = "extra_result"
    }
}