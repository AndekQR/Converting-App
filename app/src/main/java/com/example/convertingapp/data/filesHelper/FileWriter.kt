package com.example.convertingapp.data.filesHelper

import java.io.File
import java.io.FileWriter
import java.io.IOException
import kotlin.jvm.Throws

class FileWriter() {

    @Throws(IOException::class)
    fun writeJson(xmlFile: File, json: String) {
        val path = this.getFolderPath(xmlFile)
        val name = getJsonFileName(xmlFile)
        val fileToWrite = File(path, name)

        FileWriter(fileToWrite).use {
            it.append(json)
            it.flush()
        }
    }

    private fun getFolderPath(file: File): String {
        return file.absolutePath.substringBeforeLast('/')+"/"
    }

    private fun getJsonFileName(file: File): String {
        val extension = "json"
        val baseName = file.name.substringBeforeLast('.')
        return "$baseName.$extension"
    }


    companion object {
        const val WRITE_PERMISSION_REUEST_CODE = 115
    }
}