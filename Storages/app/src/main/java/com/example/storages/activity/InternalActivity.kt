package com.example.storages.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.storages.R
import java.io.*
import java.nio.charset.Charset

class InternalActivity : AppCompatActivity() {
    private val isPersistent = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_internal)
        initViews()
        createInternalStorage()
    }

    private fun initViews() {
        val btn_internal_write = findViewById<Button>(R.id.internal_files_write)
        val btn_internal_read = findViewById<Button>(R.id.internal_files_read)
        val internal_files=findViewById<EditText>(R.id.internal_files)
        val internal_files_delete=findViewById<Button>(R.id.internal_files_delete)
        btn_internal_write.setOnClickListener {
            internal_files.isEnabled=true
            val data=internal_files.text.toString()
            writeInternalStorage(data)
        }
        btn_internal_read.setOnClickListener {
            internal_files.isEnabled=false
            val list=readInternalStorage()
            internal_files.setText("")
            for (i in list) {
                internal_files.append("$i ")
            }
        }
        internal_files_delete.setOnClickListener {
            deleteInternalStorage()
            internal_files.text.clear()
        }
    }


    private fun createInternalStorage() {
        val fileName = "internal_storage.txt"
        val file: File = if (isPersistent) {
            File(filesDir, fileName)
        } else {
            File(cacheDir, fileName)
        }
        if (!file.exists()) {
            try {
                file.createNewFile()
                Toast.makeText(this, "File created", Toast.LENGTH_SHORT).show()
            }catch (e:IOException) {
                Toast.makeText(this, "File not created", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "The file has been created", Toast.LENGTH_SHORT).show()
        }
    }
    private fun writeInternalStorage(data:String) {
        val fileName="internal_storage.txt"
        try {
            if (!File(filesDir,fileName).exists() && !File(cacheDir,fileName).exists()) {
                createInternalStorage()
            }
            val fileOutputStream:FileOutputStream=if (isPersistent) {
                FileOutputStream(File(filesDir,fileName))
            }else{
                FileOutputStream(File(cacheDir,fileName))
            }
            fileOutputStream.write(data.toByteArray(charset = Charset.forName("UTF-8")))
            Toast.makeText(this, "written to the file", Toast.LENGTH_SHORT).show()
        }catch (e:IOException) {
            Toast.makeText(this, "Error occurred", Toast.LENGTH_SHORT).show()
        }
    }
    private fun readInternalStorage(): List<String> {
        val fileName = "internal_storage.txt"
        return if (File(filesDir,fileName).exists() || File(cacheDir,fileName).exists()) {

            val fileInputStream: FileInputStream = if (isPersistent) {
                FileInputStream(File(filesDir, fileName))
            } else {
                FileInputStream(File(cacheDir, fileName))
            }
            val reader = fileInputStream.bufferedReader()
            reader.readLines()
        }else{
            emptyList()
        }
    }
    private fun deleteInternalStorage(){
        val fileName = "internal_storage.txt"
        try {
            val fileInputStream:File=if (isPersistent) {
                (File(filesDir,fileName))
            }else{
                (File(cacheDir,fileName))
            }
            fileInputStream.delete()
            Toast.makeText(this, "File deleted", Toast.LENGTH_SHORT).show()
        }catch (e:IOException) {
            Toast.makeText(this, "Error occurred", Toast.LENGTH_SHORT).show()
        }
    }
}