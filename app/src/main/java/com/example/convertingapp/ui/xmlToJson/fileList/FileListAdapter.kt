package com.example.convertingapp.ui.xmlToJson.fileList

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.convertingapp.R
import java.io.File

class FileListAdapter(
    val fileList: List<File>,
    val context: Context,
    private val clickListener: ClickListener
) : RecyclerView.Adapter<FileViewHolder>() {

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


}