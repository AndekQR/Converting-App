package com.example.convertingapp.ui.audioConverter

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.convertingapp.data.filesHelper.AudioFile
import com.example.convertingapp.data.filesHelper.RealPathUtil
import java.io.File


class AudioConverterViewModel : ViewModel() {

    var audioFilesList: MutableList<AudioFile> = mutableListOf()
    var convertedFile: AudioFile? = null


    fun addFileCheckDuplicates(file: AudioFile) {
        val contains = this.audioFilesList.contains(file)
        if (!contains) {
            this.audioFilesList.add(file)
        }
    }

    fun getFileFromUri(uri: Uri, context: Context): AudioFile? {
        val path = RealPathUtil.getRealPath(context, uri)
        path?.let {
            return AudioFile(it)
        }
        return null
    }

    fun changeAudioFileStatus(file: File, status: ConversionStatus) {
        val find = this.audioFilesList.find { it.nameWithoutExtension == file.nameWithoutExtension }
        find?.let{
            it.conversionStatus = status
        }
    }

    companion object {
        val supportedFormats = listOf<String>("aac","mp3","m4a","wma","wav","flac")
    }
}