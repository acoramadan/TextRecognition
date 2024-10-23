package com.muflidevs.textrecognition

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.muflidevs.textrecognition.CameraActivity.Companion.CAMERAX_RESULT
import com.muflidevs.textrecognition.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var btnCamera: Button
    private lateinit var btnGaleri: Button
    private lateinit var btnCameraX: Button
    private lateinit var btnAnalyze: Button

    private var currentImageUri: Uri? = null
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        isGranted: Boolean ->
        if(isGranted) {
            showToast("Permission granted")
        }
        else {
            showToast("Permission not granted")
        }
    }
    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        btnCamera = binding.camera
        btnGaleri = binding.galeri
        btnAnalyze = binding.analyze
        btnCameraX = binding.camerax

        btnCamera.setOnClickListener{ startCamera() }
        btnGaleri.setOnClickListener{ startGallery() }
        btnCameraX.setOnClickListener{ startCameraX()}
        btnAnalyze.setOnClickListener{
            currentImageUri?.let {
            analyzeImage(it)
            } ?: run {
                showToast("image kosong atau tidak ada!")
            }
        }


    }
    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun startCamera() {
        currentImageUri = getImageUri(this)
        launcherIntentCamera.launch(currentImageUri!!)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        }else {
            showToast("Gagal Mengambil photo!")
        }
    }

    private fun startCameraX() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERAX_RESULT) {
            currentImageUri = it.data?.getStringExtra(CameraActivity.EXTRA_CAMERAX_IMAGE)?.
            toUri()
            showImage()
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.previewImageView.setImageURI(it)
        }
    }

    private fun analyzeImage(uri: Uri) {
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra(ResultActivity.EXTRA_IMAGE_URI, currentImageUri.toString())
        startActivity(intent)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}