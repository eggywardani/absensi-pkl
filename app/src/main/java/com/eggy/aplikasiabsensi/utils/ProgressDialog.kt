package com.eggy.aplikasiabsensi.utils

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import com.eggy.aplikasiabsensi.R

object ProgressDialog {
    private var dialogBuilder: AlertDialog? = null
    fun dynamicDialog(context: Context?, title:String, message:String){
        dialogBuilder = context?.let {
            AlertDialog.Builder(it)
                .setTitle(title)
                .setMessage(message)
                .show()
        }
    }

    fun showProgress(context: Context?){
        val dialogView = LayoutInflater.from(context).inflate(R.layout.progressbar_layout, null)
        dialogBuilder = context?.let {
            AlertDialog.Builder(it)
                .setView(dialogView)
                .setCancelable(false)
                .create()
        }

        dialogBuilder?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialogBuilder?.show()

    }

    fun hideProgress(){
        if (dialogBuilder != null){
            dialogBuilder?.dismiss()
        }
    }
}