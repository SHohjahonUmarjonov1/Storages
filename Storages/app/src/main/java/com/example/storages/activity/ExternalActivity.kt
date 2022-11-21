package com.example.storages.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.storages.R
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException

class ExternalActivity : AppCompatActivity() {
    private var readPermission = false
    private var writePermission = false
    var isPersistent = false
    private val permissions = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_external)
        createExternalFiles()
        initViews()
    }


    private fun initViews() {
        val btn_external_write = findViewById<Button>(R.id.external_files_write)
        val btn_external_read = findViewById<Button>(R.id.external_files_read)
        val external_files = findViewById<EditText>(R.id.external_files)
        val external_files_delete = findViewById<Button>(R.id.external_files_delete)
        btn_external_write.setOnClickListener {
            external_files.isEnabled=true
            val data=external_files.text.toString()
            writeExternalFiles(data)
        }
        btn_external_read.setOnClickListener {
            external_files.isEnabled=false
            val list=readExternalFiles()
            external_files.setText("")
            for (i in list) {
                external_files.append("$i ")
            }
        }
        external_files_delete.setOnClickListener {
            deleteExternalFiles()
            external_files.setText("")
        }
        requestPermissions()
    }

    private fun requestPermissions() {
        val hasReadPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
        val hasWritePermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
        val minSdk29=Build.VERSION.SDK_INT>=Build.VERSION_CODES.Q
        readPermission = hasReadPermission
        writePermission = hasWritePermission || minSdk29

        val permissions = mutableListOf<String>()

        if (!readPermission) {
            permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        if (!writePermission) {
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }

        if (permissions.isNotEmpty()) {
            permissionLauncher.launch(permissions.toTypedArray())
        }
    }

    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            readPermission = it[Manifest.permission.READ_EXTERNAL_STORAGE] ?: readPermission
            writePermission = it[Manifest.permission.WRITE_EXTERNAL_STORAGE] ?: writePermission
        }

    private fun createExternalFiles() {
        val fileName = "external_storages"
        val file: File = if (isPersistent) {
            File(getExternalFilesDir(null), fileName)
        } else {
            File(externalCacheDir, fileName)
        }
        try {
            if (!file.exists()) {
                file.createNewFile()
                Toast.makeText(this, "File created", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "File old created", Toast.LENGTH_SHORT).show()
            }
        } catch (e: IOException) {
            Toast.makeText(this, "File not created", Toast.LENGTH_SHORT).show()
        }
    }

    private fun writeExternalFiles(data: String) {
        val fileName = "external_storages"
        val fileOutputStream: FileOutputStream
        if (!File(externalCacheDir, fileName).exists() && !File(getExternalFilesDir(null), fileName).exists()) {
            createExternalFiles()
        }
            try {
                fileOutputStream = if (isPersistent) {
                    FileOutputStream(File(getExternalFilesDir(null), fileName))
                } else {
                    FileOutputStream(File(externalCacheDir, fileName))
                }
                fileOutputStream.write(data.toByteArray(Charsets.UTF_8))
            } catch (e: IOException) {
                Toast.makeText(this, "FileOutputStream error occurred ... ", Toast.LENGTH_SHORT).show()
            }

    }

    private fun readExternalFiles(): List<String> {
        val fileName = "external_storages"
        val fileInputStream:FileInputStream
        if (File(externalCacheDir, fileName).exists() || File(getExternalFilesDir(null), fileName).exists()) {
            return try {
                fileInputStream=if (isPersistent) {
                    FileInputStream(File(getExternalFilesDir(null),fileName))
                } else {
                    FileInputStream(File(externalCacheDir,fileName))
                }
                val reader=fileInputStream.bufferedReader()
                reader.readLines()
            }catch (e:IOException) {
                Toast.makeText(this, "FileInputStream error occurred", Toast.LENGTH_SHORT).show()
                emptyList()
            }
        }
        else {
            return emptyList()
        }
    }

    private fun deleteExternalFiles() {
        val fileName = "external_storages"
        try {
            val file:File=if (isPersistent) {
                File(getExternalFilesDir(null),fileName)
            }else{
                File(externalCacheDir,fileName)
            }
            file.delete()
            Toast.makeText(this, "File deleted", Toast.LENGTH_SHORT).show()
        }catch (e:IOException) {
            Toast.makeText(this, "Error occurred", Toast.LENGTH_SHORT).show()
        }
    }
}