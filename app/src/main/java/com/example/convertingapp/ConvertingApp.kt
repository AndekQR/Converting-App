package com.example.convertingapp

import android.app.Application
import cafe.adriel.androidaudioconverter.AndroidAudioConverter
import cafe.adriel.androidaudioconverter.callback.ILoadCallback
import com.example.convertingapp.data.myParser.XmlParser
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.bind
import org.kodein.di.generic.factory
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

import kotlin.Exception

class ConvertingApp : Application(), KodeinAware {

    override fun onCreate() {
        super.onCreate()

        AndroidAudioConverter.load(this, object : ILoadCallback {
            override fun onSuccess() {}

            override fun onFailure(error: Exception?) {
                throw Exception("FFmpeg is not supported by device")
            }
        })
    }

    override val kodein by Kodein.lazy {
        bind<XmlParser>() with provider { XmlParser() }
    }
}