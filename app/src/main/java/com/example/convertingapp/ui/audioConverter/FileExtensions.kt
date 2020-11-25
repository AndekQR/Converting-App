package com.example.convertingapp.ui.audioConverter

import com.example.convertingapp.data.filesHelper.AudioFile

fun AudioFile.isAudioFile():Boolean {
    val fileExtension = this.name.substringAfterLast('.')
    if (AudioConverterViewModel.supportedFormats.contains(fileExtension)) return true
    return false
}
