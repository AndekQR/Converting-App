package com.example.convertingapp.ui.xmlToJson.fileList

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.documentfile.provider.DocumentFile
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.convertingapp.R
import com.example.convertingapp.data.filesHelper.FileWriter
import com.example.convertingapp.ui.xmlToJson.XmlToJsonViewModel
import com.example.convertingapp.ui.xmlToJson.alert.AlertDialogFragment
import kotlinx.android.synthetic.main.fragment_files_list.*
import java.io.File


class FilesListFragment : Fragment(), ClickListener {

    private lateinit var adapter: FileListAdapter
    private val viewModel: XmlToJsonViewModel by activityViewModels()

    private val FILE_CHOOSER_REQUEST_CODE = 111
    private val FOLDER_CHOOSER_REQUEST_CODE = 113

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_files_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.adapter = FileListAdapter(this.viewModel.xmlFileList, requireContext(), this)
        filesRecyclerView.adapter = this.adapter
        filesRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        chooseFileButton.setOnClickListener {
            openChooser(true)
        }
        chooseFolderButton.setOnClickListener {
            openChooser(false)
        }

        saveAllJsonButton.setOnClickListener {
            this.viewModel.xmlFileList.forEach {file ->
                this.tryWriteFile(file, false)
            }
            AlertDialogFragment("Success", "Files saved") {dialog, _ ->
                dialog.dismiss()
            }
        }
    }


    override fun detailsButtonAction(file: File) {
        this.viewModel.fileToViewDetails = file
        view?.findNavController()?.navigate(R.id.DetailsXmlToJsonFragment)
    }

    override fun saveAsJsonButtonAction(file: File) {
        this.tryWriteFile(file)
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
        val mimeTypes = arrayOf<String>("text/xml")
        var selectFileIntent = Intent(Intent.ACTION_GET_CONTENT)
        selectFileIntent.addCategory(Intent.CATEGORY_OPENABLE)
        selectFileIntent.type = if (mimeTypes.size == 1) mimeTypes[0] else "*/*"
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
                            this.viewModel.addFileCheckDuplicates(it)
                            this.adapter.notifyDataSetChanged()
                        }
                    }
                }
            }
            FOLDER_CHOOSER_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    data?.data?.let {
                        DocumentFile.fromTreeUri(requireContext(), it)?.listFiles()?.forEach { nullableFile ->
                            nullableFile?.let { file ->
                                if (this.viewModel.isXmlFile(file)) {
                                    val fileFromUri = this.viewModel.getFileFromUri(file.uri, requireContext())
                                    fileFromUri?.let { uriFile ->
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

    private fun tryWriteFile(file: File, alertsOn: Boolean = true) {
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
        this.viewModel.writeFile(childFragmentManager, file, alertsOn)
    }



}