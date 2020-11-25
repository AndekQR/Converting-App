package com.example.convertingapp.ui.audioConverter.homeFragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.convertingapp.R
import com.example.convertingapp.data.filesHelper.AudioFile
import com.example.convertingapp.ui.audioConverter.ConversionStatus

class AudioListAdapter(val audioFileList: List<AudioFile>) : RecyclerView.Adapter<AudioFileViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AudioFileViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val fileView = inflater.inflate(R.layout.audio_file_item, parent, false)
        return AudioFileViewHolder(fileView)
    }

    override fun onBindViewHolder(holder: AudioFileViewHolder, position: Int) {
        val file = this.audioFileList[position]
        holder.audioFileTextView.text = getlastSegmentsOfPath(file = audioFileList[position])
        when(file.conversionStatus) {
            ConversionStatus.IN_PROGRESS -> {
                holder.progressbar.visibility = View.VISIBLE
                holder.imageView.visibility = View.GONE
            }
            ConversionStatus.DONE -> {
                holder.progressbar.visibility = View.GONE
                holder.imageView.visibility = View.VISIBLE
                holder.imageView.setImageResource(R.drawable.baseline_done_black_24dp)
            }
            ConversionStatus.ERROR -> {
                holder.progressbar.visibility = View.GONE
                holder.imageView.visibility = View.VISIBLE
                holder.imageView.setImageResource(R.drawable.baseline_error_outline_black_24dp)
            }
            ConversionStatus.NONE -> {
                holder.progressbar.visibility = View.GONE
                holder.imageView.visibility = View.GONE
            }
        }
    }

    private fun getlastSegmentsOfPath(segments: Int = 2, file: AudioFile): String {
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