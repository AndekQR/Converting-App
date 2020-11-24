package com.example.convertingapp.ui.audioConverter

import java.io.File

fun File.isAudioFile():Boolean {
    val fileExtension = this.name.substringAfterLast('.')
    if (AudioConverterViewModel.supportedFormats.contains(fileExtension)) return true
    return false
}
