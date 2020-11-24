package com.example.convertingapp.ui.xmlToJson.fileList

import java.io.File

interface ClickListener {
    fun detailsButtonAction(file: File)
    fun saveAsJsonButtonAction(file: File)
}