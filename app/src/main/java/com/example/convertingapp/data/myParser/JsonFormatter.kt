package com.example.convertingapp.data.myParser

import org.json.JSONObject

object JsonFormatter {

    const val INDENT = 2

    fun format(json: String): String {
        return JSONObject(json).toString(INDENT)
    }
}