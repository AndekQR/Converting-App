package com.example.convertingapp.ui.xmlToJson.details

import android.os.Bundle
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.text.method.ScrollingMovementMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.convertingapp.R
import com.example.convertingapp.ui.xmlToJson.XmlToJsonViewModel
import kotlinx.android.synthetic.main.fragment_xml.*

class XmlFragment : Fragment() {

    private val viewmodel: XmlToJsonViewModel by activityViewModels()
    private lateinit var xmlEditText: EditText
    private var initialized = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_xml, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.xmlEditText = xmlTextView
        xmlEditText.movementMethod = ScrollingMovementMethod()
        this.viewmodel.xmlString.observe(viewLifecycleOwner, {
            if(it != this.xmlEditText.text.toString()) this.xmlEditText.setText(it)
            initialized = true
        })
//        xmlEditText.addTextChangedListener(textChangeListener)

        this.xmlEditText.doOnTextChanged { text, start, before, count ->
            viewmodel.xmlString.postValue(text.toString())
            if (initialized) viewmodel.jsonString.value = ""
        }
    }

//    private val textChangeListener = object: TextWatcher {
//        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//        }
//
//        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//        }
//
//        override fun afterTextChanged(s: Editable?) {
//            viewmodel.xmlString.value = xmlEditText.text.toString()
////            if (initialized) viewmodel.jsonString.value = ""
//        }
//
//    }
}