package com.example.convertingapp.data.builtInParser

import com.example.convertingapp.data.XmlParser
import com.example.convertingapp.data.orgJson.XML
import org.json.JSONException

import kotlin.jvm.Throws

class BuiltInXmlParser : XmlParser {

    private lateinit var xmlString: String
    private lateinit var jsonString: String

    @Throws(JSONException::class)
    override fun parseXmlString(xml: String) {
        this.xmlString = xml
        val jsonObject = XML.toJSONObject(xml)
        this.jsonString = jsonObject.toString(INDENT)
    }

    override fun getJson(): String {
        return this.jsonString
    }

    override fun getXml(): String {
        return this.xmlString
    }

    companion object {
        const val INDENT: Int = 2
    }
}