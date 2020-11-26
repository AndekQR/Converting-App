package com.example.convertingapp.ui.xmlToJson.fileList

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.convertingapp.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_files_list.*
import java.io.File

class FileListAdapter(
    val fileList: MutableList<File>,
    val activity: Activity,
    private val clickListener: ClickListener
) : RecyclerView.Adapter<FileViewHolder>() {

    private var recentlyDeletedFile: File? = null
    private var recentlyDeletedFilePosition: Int? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val fileView = inflater.inflate(R.layout.file_card, parent, false)
        return FileViewHolder(fileView)
    }

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        holder.fileName.text = this.fileList[position].name
        holder.detailsButton.setOnClickListener {
            this.clickListener.detailsButtonAction(this.fileList[position])
        }
        holder.saveAsJsonButton.setOnClickListener {
            this.clickListener.saveAsJsonButtonAction(this.fileList[position])
        }
    }

    override fun getItemCount(): Int = this.fileList.size

    fun deleteFile(position: Int) {
        this.recentlyDeletedFile = this.fileList[position]
        this.recentlyDeletedFilePosition = position
        this.fileList.removeAt(position)
        notifyItemRemoved(position)
        showUndoSnackbar()
    }

    private fun showUndoSnackbar() {
        val view = activity.fileListLayoutRoot
        val snackbar = Snackbar.make(view, "Undo delete", Snackbar.LENGTH_LONG)
        snackbar.setAction("Undo") {
            undoDelete()
        }.show()
    }

    private fun undoDelete() {
        recentlyDeletedFilePosition?.let {
            recentlyDeletedFile?.let { it1 ->
                this.fileList.add(it, it1)
                notifyItemInserted(it)
            }
        }
    }
}