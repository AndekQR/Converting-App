package com.example.convertingapp.ui.xmlToJson.details

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.example.convertingapp.R
import com.example.convertingapp.data.filesHelper.FileWriter
import com.example.convertingapp.data.orgJson.JSONException
import com.example.convertingapp.ui.xmlToJson.XmlToJsonViewModel
import com.example.convertingapp.ui.xmlToJson.alert.AlertDialogFragment
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_details_xml_to_json.*
import net.revelc.code.formatter.xml.lib.FormattingPreferences
import net.revelc.code.formatter.xml.lib.XmlDocumentFormatter
import java.io.File
import java.io.IOException

class DetailsXmlToJsonFragment : Fragment() {

    private val viewmodel: XmlToJsonViewModel by activityViewModels()
    private lateinit var viewpager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_details_xml_to_json, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        this.viewpager = pager
        val tabLayout = tab_layout

        val viewpagerAdapter = ViewPagerAdapter(this)
        viewpagerAdapter.addFragment(XmlFragment())
        viewpagerAdapter.addFragment(JsonFragment())
        viewpager.adapter = viewpagerAdapter

        TabLayoutMediator(tabLayout, viewpager) { tab, position ->
            when (position) {
                0 -> tab.text = "XML"
                1 -> tab.text = "JSON"

            }
        }.attach()

        initFabActions()

        val fileToViewDetails = this.viewmodel.fileToViewDetails
        if (fileToViewDetails != null) tryParseFile(fileToViewDetails)
        else {
            AlertDialogFragment("Error","Cannot open the file") { dialog, _ ->
                dialog.dismiss()
            }.show(childFragmentManager, null)
        }

    }

    private fun tryParseFile(file: File) {
        try {
            this.viewmodel.parseXml(file)
        } catch (ex: JSONException) {
            AlertDialogFragment("Error",ex.localizedMessage ?: "XML error") { dialog, _ ->
                dialog.dismiss()
            }.show(childFragmentManager, null)
        }
    }


    private fun initFabActions() {
        saveAsJson_fab.setOnClickListener {
            tryWriteFile()
        }

        format_fab.setOnClickListener {
            val currentItem = viewpager.currentItem
            val currentFragment = (viewpager.adapter as ViewPagerAdapter).createFragment(currentItem)
            if (currentFragment.isVisible && currentFragment.isResumed) {
                val xmlToFormat = this.viewmodel.xmlString.value
                xmlToFormat?.let {
                    val formattingPreferences = FormattingPreferences()
                    formattingPreferences.wellFormedValidation = FormattingPreferences.IGNORE
                    val formattedXml = XmlDocumentFormatter(formattingPreferences).format(it)
                    this.viewmodel.xmlString.value = formattedXml
                }
            }
        }

        parse_fab.setOnClickListener {
            val xmlToParse = this.viewmodel.xmlString.value
            xmlToParse?.let {
                try {
                    this.viewmodel.parseXml(it)
                } catch (ex: JSONException) {
                    AlertDialogFragment("Error",ex.localizedMessage ?: "XML error") { dialog, _ ->
                        dialog.dismiss()
                    }.show(childFragmentManager, null)
                }

            }
        }
    }

    private fun tryWriteFile() {
        val writePermission =
            ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (writePermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                FileWriter.WRITE_PERMISSION_REUEST_CODE
            )
            return
        }
        this.viewmodel.writeFile(childFragmentManager)
    }



    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode) {
            FileWriter.WRITE_PERMISSION_REUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    this.viewmodel.writeFile(childFragmentManager)
                }
            }
        }
    }
}