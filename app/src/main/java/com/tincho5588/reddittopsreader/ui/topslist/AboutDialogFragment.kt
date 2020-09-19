package com.tincho5588.reddittopsreader.ui.topslist

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.tincho5588.reddittopsreader.R

class AboutDialogFragment: DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let { activity ->
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(activity)
            builder.setMessage(R.string.about_message)
                .setNegativeButton(R.string.close
                ) { dialog, _ ->
                    dialog.dismiss()
                }
                .setCancelable(true)

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}