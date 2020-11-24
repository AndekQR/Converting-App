package com.example.convertingapp.ui.xmlToJson

import android.content.Context
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.convertingapp.data.XmlParser
import com.example.convertingapp.data.builtInParser.BuiltInXmlParser
import com.example.convertingapp.data.filesHelper.FileWriter
import com.example.convertingapp.data.filesHelper.RealPathUtil
import com.example.convertingapp.data.orgJson.JSONException
import com.example.convertingapp.ui.xmlToJson.alert.AlertDialogFragment
import java.io.File
import java.io.IOException
import java.io.OutputStreamWriter

class XmlToJsonViewModel : ViewModel() {

    val xmlFileList = mutableListOf<File>()
    private lateinit var parser: XmlParser
    var fileToViewDetails: File? = null

    val xmlString: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val jsonString: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }


    fun isXmlFile(file: DocumentFile): Boolean {
        val substringAfterLast = file.name?.substringAfterLast(".")
        if (substringAfterLast != null && substringAfterLast == "xml") {
            return true
        }
        return false
    }

    fun addFileCheckDuplicates(file: File) {
        val contains = this.xmlFileList.contains(file)
        if (!contains) {
            this.xmlFileList.add(file)
        }
    }

    fun getFileFromUri(uri: Uri, context: Context): File? {
        val path = RealPathUtil.getRealPath(context, uri)
        path?.let {
            return File(it)
        }
        return null
    }

    @Throws(JSONException::class)
    fun parseXml(xml: String) {
        if (this.checkForNamespaces(xml)) {
            this.parser = BuiltInXmlParser()
            this.parser.parseXmlString(xml)
        } else {
            this.parser = com.example.convertingapp.data.myParser.XmlParser()
            this.parser.parseXmlString(xml)
        }
        this.xmlString.value = this.getXml()
        this.jsonString.value = this.getJson()
    }

     @Throws(JSONException::class)
    fun parseXml(file: File) {
        val xmlString = file.readText()
        this.parseXml(xmlString)
    }

    private fun getJson(): String {
        if (this::parser.isInitialized) {
            return this.parser.getJson()
        }
        return ""
    }

    private fun getXml(): String {
        if (this::parser.isInitialized) {
            return this.parser.getXml()
        }
        return ""
    }

    /**
     * @return true jeżeli w tekście pliku xml występują przestrzenie nazw
     */
    private fun checkForNamespaces(xmlString: String): Boolean {
        val nameSpaceRegex = Regex("<[a-zA-Z]+:[a-zA-Z]+")
        return nameSpaceRegex.containsMatchIn(xmlString)
    }

    /**
     * przed wywołaniem trzeba sprawdzić uprawnienia
     */
    fun writeFile(fm: FragmentManager, file: File? = null, alertsOn: Boolean = true) {
        if(file != null) {
            this.parseXml(file)
            write(file, this.jsonString.value ?: "", fm, alertsOn)
            return
        } else {
            this.fileToViewDetails?.let {
                this.jsonString.value?.let { jsonString ->
                    write(it, jsonString, fm, alertsOn)
                    return
                }
            }
        }
        if (alertsOn) {
            AlertDialogFragment("Error","Something wrong with the file") { dialog, _ ->
                dialog.dismiss()
            }.show(fm, null)
        }
    }

    private fun write(file: File, json: String,fm: FragmentManager, alertsOn: Boolean) {
        try{
            FileWriter().writeJson(file, json)
        }catch (ex: IOException) {
            if (alertsOn) {
                AlertDialogFragment("Error",ex.localizedMessage ?: "Something wrong with the file") { dialog, _ ->
                    dialog.dismiss()
                }.show(fm, null)
            }
            return
        }
        if (alertsOn) {
            AlertDialogFragment("Success","File saved") { dialog, _ ->
                dialog.dismiss()
            }.show(fm, null)
        }
        return
    }


}