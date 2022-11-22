package com.example.storages.activity

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.activity.result.registerForActivityResult
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.storages.R
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

class PermissionsActivity : AppCompatActivity() {
    var isPersistent = false
    var isInternal = true
    var cameraPermission = false
    var locationPermission = false

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permissions)
        initViews()
    }

    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            cameraPermission = it ?: cameraPermission
        }
    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            locationPermission =
                it[Manifest.permission.ACCESS_COARSE_LOCATION] ?: locationPermission
            cameraPermission = it[Manifest.permission.CAMERA] ?: cameraPermission
        }
    private val takePhoto =
        registerForActivityResult(ActivityResultContracts.TakePicturePreview()) {
            val isSavedSuccess =
                when {
                    isInternal -> it?.let { it1 ->
                        savePhotoInternalStorage(
                            UUID.randomUUID().toString(),
                            it1
                        )
                    } ?: return@registerForActivityResult
                    else -> false
                }
            if (isSavedSuccess) {
                Toast.makeText(this, "Success saved", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Saved Error occurred ...", Toast.LENGTH_SHORT).show()
            }
        }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun initViews() {
        val camera = findViewById<Button>(R.id.camera)
        val location = findViewById<Button>(R.id.location)
        camera.setOnClickListener {
            if (cameraPermission) {
                takePhoto.launch()
            } else {
                Toast.makeText(this, "not allowed for camera", Toast.LENGTH_SHORT).show()
                cameraLauncher.launch(Manifest.permission.CAMERA)
                if (cameraPermission) {
                    takePhoto.launch()
                }
            }
        }
        location.setOnClickListener {
            if (locationPermission) {
                Toast.makeText(this, "Success location", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "not allowed for location", Toast.LENGTH_SHORT).show()
            }
        }
        requestPermissions()
    }

    private fun requestPermissions() {
        cameraPermission = ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
        locationPermission = ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val permissions = mutableListOf<String>()
        if (!cameraPermission) {
            permissions.add(Manifest.permission.CAMERA)
        }
        if (!locationPermission) {
            permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        }
        if (permissions.isNotEmpty()) {
            permissionLauncher.launch(permissions.toTypedArray())
        }
    }

    private fun savePhotoInternalStorage(fileName: String, bitmap: Bitmap): Boolean {
        return try {
            val fileOutputStream: FileOutputStream = if (isPersistent) {
                FileOutputStream(File(filesDir, "$fileName.jpg"))
            } else {
                FileOutputStream(File(cacheDir, "$fileName.jpg"))
            }
            true
        } catch (e: IOException) {
            Toast.makeText(this, "FileOutputStream error occurred ...", Toast.LENGTH_SHORT).show()
            false
        }
    }
}