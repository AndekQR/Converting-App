package com.example.convertingapp.data.myParser

import com.example.convertingapp.data.XmlParser
import com.example.convertingapp.data.myParser.XmlTagType.*
import com.example.convertingapp.data.orgJson.JSONException
import java.io.BufferedReader
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

class XmlParser : XmlParser {

    private var treeNode: TreeNode? = null
    private lateinit var reader: BufferedReader
    private var xmlStringFormatted: String = ""
    private var xmlStringTrimed: String = ""

    private fun prepareData(data: String): String {
        return data
            .trim()
            .replace(System.lineSeparator(), "")
            .replace("\t", "")
            .replace("> +".toRegex(), ">")
    }

    private fun initFile(path: String) {
        this.reader = Files.newBufferedReader(Paths.get(path))
    }

    override fun getXml(): String {
        return xmlStringFormatted
    }

    @Throws(JSONException::class)
    override fun parseXmlString(xml: String) {
        this.saveXmlString(xml)

        XmlValidator.isValid(xml)

        val parse = this.parse(this.xmlStringTrimed)
        this.treeNode = parse

    }

    override fun getJson(): String {
        this.treeNode?.let {
            val jsonCreator = MyJsonCreator(it)
            val json = jsonCreator.getJSON()
            return JsonFormatter.format(json)
        }
        return ""
    }

    fun getTreeNode(): Optional<TreeNode> {
        return Optional.ofNullable(this.treeNode)
    }

    private fun saveXmlString(xml: String) {
        this.xmlStringFormatted = xml
        val xml = this.prepareData(xml)
        this.xmlStringTrimed = xml
    }


    private fun parse(xml: String): TreeNode {
        val detector = TagTypeDetector(xml[0])
        var node: Node?
        val builder = StringBuilder()
        var lastArtName = ""
        val treeNode = TreeNode()

        for (i in 1 until xml.length) {
            detector.checkV2(xml[i])
//            println(xml[i] + ": " + detector.typeOfCurrent() + "(actualType: " + detector.actualTag.get().type + ")")
            when (detector.typeOfCurrent()) {
                PROLOG.type,
                TAG_NAME.type,
                TAG_VALUE.type,
                COMMENT.type,
                ATTRIBUTE_NAME.type,
                ATTRIBUTE_VALUE.type,
                CLOSE_TAG.type,
                INLINE_CLOSE_TAG.type -> builder.append(xml[i])

                UNMACHED.type -> {
                    if (builder.isNotBlank())
                        when (detector.actualTag.get()) {
                            TAG_NAME -> {
                                node = Node(name = builder.toString())
                                treeNode.addChild(node)
                            }
                            TAG_VALUE -> treeNode.getLastNode().value = Optional.of(builder.toString())
                            COMMENT -> {
                                node = Node(name = "_comment")
                                node.value = Optional.of(builder.toString())
                                treeNode.addChild(node)
                                treeNode.goToParent()
                            }
                            ATTRIBUTE_NAME -> lastArtName = builder.toString()
                            ATTRIBUTE_VALUE -> treeNode.getLastNode().atributes[lastArtName] = builder.toString()
                            CLOSE_TAG -> {
                                treeNode.goToParent()
                            }
                            INLINE_CLOSE_TAG -> {
                                node = Node(name = builder.toString())
                                treeNode.addChild(node)
                                treeNode.goToParent()
                            }
                        }
                    builder.clear()
                }

            }
        }
        return treeNode
    }
}

