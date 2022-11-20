package com.example.storages.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.storages.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        callInternalActivity()
    }
    private fun callInternalActivity()  {
        val btn_storage = findViewById<Button>(R.id.btn_storages)
        btn_storage.setOnClickListener {
            val intent=Intent(this,InternalActivity::class.java)
            startActivity(intent)
        }
    }
    private fun callExternalActivity()  {
        val btn_storage = findViewById<Button>(R.id.btn_storages)
        btn_storage.setOnClickListener {
            val intent=Intent(this,ExternalActivity::class.java)
            startActivity(intent)
        }
    }
}