package com.example.convertingapp.data

interface XmlParser {
    fun parseXmlString(xml: String)
    fun getJson(): String
    fun getXml(): String
}