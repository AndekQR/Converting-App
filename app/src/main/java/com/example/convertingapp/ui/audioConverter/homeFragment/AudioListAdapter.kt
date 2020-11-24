package com.example.convertingapp.ui.audioConverter.homeFragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.convertingapp.R
import java.io.File

class AudioListAdapter(val audioFileList: List<File>) : RecyclerView.Adapter<AudioFileViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AudioFileViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val fileView = inflater.inflate(R.layout.audio_file_item, parent, false)
        return AudioFileViewHolder(fileView)
    }

    override fun onBindViewHolder(holder: AudioFileViewHolder, position: Int) {
        holder.audioFileTextView.text = getlastSegmentsOfPath(file = audioFileList[position])
    }

    private fun getlastSegmentsOfPath(segments: Int = 2, file: File): String {
        val pathSegments = file.absolutePath.split('/')
        val result: StringBuilder = StringBuilder()
        val startIndex = pathSegments.size - segments
        if (startIndex <= 0) {
            pathSegments.forEach {
                result.append("/")
                result.append(it)
            }
        } else {
            for(i in startIndex until pathSegments.size) {
                result.append("/")
                result.append(pathSegments[i])
            }
        }
        return result.toString().trim()
    }

    override fun getItemCount() = this.audioFileList.size
}