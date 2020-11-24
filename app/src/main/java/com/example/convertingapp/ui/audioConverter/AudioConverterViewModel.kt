package com.example.convertingapp.ui.audioConverter

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.convertingapp.data.filesHelper.RealPathUtil
import java.io.File

class AudioConverterViewModel : ViewModel() {

    var audioFilesList: MutableList<File> = mutableListOf()


    fun addFileCheckDuplicates(file: File) {
        val contains = this.audioFilesList.contains(file)
        if (!contains) {
            this.audioFilesList.add(file)
        }
    }

    fun getFileFromUri(uri: Uri, context: Context): File? {
        val path = RealPathUtil.getRealPath(context, uri)
        path?.let {
            return File(it)
        }
        return null
    }

    companion object {
        val supportedFormats = listOf<String>("aac","mp3","m4a","wma","wav","flac")
    }
}