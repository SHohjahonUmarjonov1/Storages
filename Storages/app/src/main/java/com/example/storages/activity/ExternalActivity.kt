package com.example.storages.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.storages.R

class ExternalActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_external)
        initViews()
    }
    private fun initViews() {
        val btn_external_write = findViewById<Button>(R.id.external_files_write)
        val btn_external_read = findViewById<Button>(R.id.external_files_read)
        val external_files=findViewById<EditText>(R.id.external_files)
        val external_files_delete=findViewById<Button>(R.id.external_files_delete)
    }
}