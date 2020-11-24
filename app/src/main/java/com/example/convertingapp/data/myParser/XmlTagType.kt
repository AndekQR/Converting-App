package com.example.convertingapp.data.myParser

enum class XmlTagType(val type: String) {
    PROLOG("PROLOG"),
    TAG_NAME("TAG_NAME"),
    TAG_VALUE("TAG_VALUE"),
    COMMENT("COMMENT"),
    ATTRIBUTE_NAME("ATTRIBUTE_NAME"),
    ATTRIBUTE_VALUE("ATTRIBUTE_VALUE"),
    UNMACHED("UNMACHED"),
    CLOSE_TAG("CLOSE_TAG"),
    INLINE_CLOSE_TAG("INLINE_CLOSE_TAG"),
}