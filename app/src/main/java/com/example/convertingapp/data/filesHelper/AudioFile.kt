package com.example.convertingapp.data.filesHelper

import com.example.convertingapp.ui.audioConverter.ConversionStatus
import java.io.File

class AudioFile(path: String, var conversionStatus: ConversionStatus = ConversionStatus.NONE) : File(path)