package com.example.convertingapp.ui.audioConverter.homeFragment

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.documentfile.provider.DocumentFile
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cafe.adriel.androidaudioconverter.AndroidAudioConverter
import cafe.adriel.androidaudioconverter.callback.IConvertCallback
import cafe.adriel.androidaudioconverter.model.AudioFormat
import com.example.convertingapp.R
import com.example.convertingapp.ui.audioConverter.AudioConverterViewModel
import com.example.convertingapp.ui.audioConverter.isAudioFile
import com.google.android.material.checkbox.MaterialCheckBox
import kotlinx.android.synthetic.main.fragment_audio_converter_home.*
import java.io.File

class AudioConverterHomeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AudioListAdapter
    private val outputFormatCheckboxes = mutableListOf<MaterialCheckBox>()

    private val viewModel: AudioConverterViewModel by viewModels()

    private val FILE_CHOOSER_REQUEST_CODE = 111
    private val FOLDER_CHOOSER_REQUEST_CODE = 113

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_audio_converter_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.adapter = AudioListAdapter(this.viewModel.audioFilesList)
        this.recyclerView = filesRecyclerView
        this.recyclerView.adapter = this.adapter
        this.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        content.visibility = INVISIBLE
        initCheckboxes()
        chooseFileButton.setOnClickListener {
            openChooser(true)
        }
        chooseFolderButton.setOnClickListener {
            openChooser(false)
        }
        convertButton.setOnClickListener {
            convertFiles()
        }
    }

    private fun convertFiles() {
        this.outputFormatCheckboxes.forEach { checkbox ->
            this.viewModel.audioFilesList.forEach { file ->
                when (checkbox.id) {
                    R.id.checkbox_aac -> convert(file, AudioFormat.AAC)
                    R.id.checkbox_mp3 -> convert(file, AudioFormat.MP3)
                    R.id.checkbox_m4a -> convert(file, AudioFormat.M4A)
                    R.id.checkbox_wma -> convert(file, AudioFormat.WMA)
                    R.id.checkbox_wav -> convert(file, AudioFormat.WAV)
                    R.id.checkbox_flac -> convert(file, AudioFormat.FLAC)
                }
            }
        }
    }

    private fun convert(file: File, format: AudioFormat) {
        //nie konwertujemy pliku do tego samego formatu
        if (file.extension != format.name) {
            AndroidAudioConverter.with(requireContext())
                .setFile(file)
                .setFormat(format)
                .setCallback(fileConvertCallback)
                .convert()
        }
        fileConvertCallback.onSuccess(file)
    }

    private val fileConvertCallback = object : IConvertCallback {
        override fun onSuccess(convertedFile: File?) {
            TODO("Not yet implemented")
        }

        override fun onFailure(error: Exception?) {
            TODO("Not yet implemented")
        }

    }

    private fun initCheckboxes() {
        this.outputFormatCheckboxes.addAll(
            arrayOf(
                checkbox_aac,
                checkbox_mp3,
                checkbox_m4a,
                checkbox_wma,
                checkbox_wav,
                checkbox_flac
            )
        )
    }


    /**
     * true = file chooser
     * false = folder chooser
     */
    private fun openChooser(fileChooser: Boolean = true) {
        val readPermission =
            ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
        if (readPermission != PackageManager.PERMISSION_GRANTED) {
            if (fileChooser) requestPermissions(
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                FILE_CHOOSER_REQUEST_CODE
            )
            else requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), FOLDER_CHOOSER_REQUEST_CODE)
            return
        }
        if (fileChooser) openFileChooser()
        else openFolderChooser()
    }

    private fun openFolderChooser() {
        var selectFileIntent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
        selectFileIntent = Intent.createChooser(selectFileIntent, "Choose folder")
        startActivityForResult(selectFileIntent, FOLDER_CHOOSER_REQUEST_CODE)
    }

    private fun openFileChooser() {
        val mimeTypes = arrayOf<String>("audio/*")
        var selectFileIntent = Intent(Intent.ACTION_GET_CONTENT)
        selectFileIntent.addCategory(Intent.CATEGORY_OPENABLE)
        selectFileIntent.type = "*/*"
        if (mimeTypes.isNotEmpty()) {
            selectFileIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        }
        selectFileIntent = Intent.createChooser(selectFileIntent, "Choose file")
        startActivityForResult(selectFileIntent, FILE_CHOOSER_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            FILE_CHOOSER_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    data?.data?.let { uri ->
                        val fileFromUri = this.viewModel.getFileFromUri(uri, requireContext())
                        fileFromUri?.let {
                            if (fileFromUri.isAudioFile()) {
                                if (content.visibility == INVISIBLE) content.visibility = VISIBLE
                                this.viewModel.addFileCheckDuplicates(it)
                                this.adapter.notifyDataSetChanged()
                            }
                        }
                    }
                }
            }
            FOLDER_CHOOSER_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    data?.data?.let {
                        DocumentFile.fromTreeUri(requireContext(), it)?.listFiles()?.forEach { nullableFile ->
                            nullableFile?.let { file ->
                                val fileFromUri = this.viewModel.getFileFromUri(file.uri, requireContext())
                                fileFromUri?.let { uriFile ->
                                    if (fileFromUri.isAudioFile()) {
                                        if (content.visibility == INVISIBLE) content.visibility = VISIBLE
                                        this.viewModel.addFileCheckDuplicates(uriFile)
                                        this.adapter.notifyDataSetChanged()
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            FILE_CHOOSER_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openFileChooser()
                }
            }
            FOLDER_CHOOSER_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openFolderChooser()
                }
            }
        }
    }

}