package com.example.convertingapp.data.myParser

import com.example.convertingapp.data.orgJson.JSONException
import com.example.convertingapp.data.orgJson.XML

object XmlValidator {

//    /**
//     * Jeżeli jest wyjątek to jest błąd w XML
//     */
    @Throws(JSONException::class)
    fun isValid(xml: String) {
        XML.toJSONObject(xml)
    }

}