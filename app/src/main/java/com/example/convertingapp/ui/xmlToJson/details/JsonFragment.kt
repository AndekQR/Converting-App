package com.example.convertingapp.ui.xmlToJson.details

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import com.example.convertingapp.R
import com.example.convertingapp.ui.xmlToJson.XmlToJsonViewModel
import kotlinx.android.synthetic.main.fragment_json.*

class JsonFragment : Fragment() {

    private val viewmodel: XmlToJsonViewModel by activityViewModels()

    private lateinit var jsonTextV: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_json, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.jsonTextV = jsonTextView
        jsonTextV.movementMethod = ScrollingMovementMethod()
        this.viewmodel.jsonString.observe(viewLifecycleOwner, {
            this.jsonTextV.text = it
        })
    }


}