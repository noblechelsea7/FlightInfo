package com.example.flightinfo.ui.utils

import android.app.AlertDialog
import android.content.Context

object DialogUtils {

    fun showErrorDialog(context: Context, title: String, message: String) {
        AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .show()
    }
}