package com.example.convertingapp.ui.xmlToJson.alert

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import java.lang.IllegalStateException
import kotlin.jvm.Throws

class AlertDialogFragment(private val title: String,private val message: String, private val okButtonAction: DialogInterface.OnClickListener) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setTitle(title)
                setMessage(message)
                setPositiveButton("Ok", okButtonAction)
            }
            return builder.create()
        } ?: throw IllegalStateException("activity cannot be null")
    }

}