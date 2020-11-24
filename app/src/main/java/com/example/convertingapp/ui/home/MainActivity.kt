package com.example.convertingapp.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.convertingapp.R
import com.example.convertingapp.ui.audioConverter.AudioConverterActivity
import com.example.convertingapp.ui.xmlToJson.XmlToJsonActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    override fun onStart() {
        super.onStart()

        xmlToJsonCard.setOnClickListener {
            Intent(this, XmlToJsonActivity::class.java).also {
                startActivity(it)
            }

        }

        audioConverterCard.setOnClickListener {
            Intent(this, AudioConverterActivity::class.java).also {
                startActivity(it)
            }
        }
    }
}