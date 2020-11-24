package com.example.convertingapp.ui.xmlToJson.fileList

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import kotlinx.android.synthetic.main.file_card.view.*

class FileViewHolder(val itemView: View) : RecyclerView.ViewHolder(itemView) {

    var fileName: TextView = itemView.fileNameTextView
    var detailsButton: MaterialButton = itemView.detailsButton
    var saveAsJsonButton: MaterialButton = itemView.saveAsJsonButton

}