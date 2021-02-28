package com.stacktobasics.pokemoneasycatch

import android.app.Activity
import android.app.AlertDialog

class LoadingDialog(private val activity: Activity) {

    private var alertDialog : AlertDialog

    init {
        val builder = AlertDialog.Builder(activity)

        val inflater = activity.layoutInflater;
        builder.setView(inflater.inflate(R.layout.custom_dialog, null))
        builder.setCancelable(false)

        builder.create()

        alertDialog = builder.create();
    }

    fun showDialog() {
        alertDialog.show()
    }

    fun dismissDialog() {
       alertDialog.dismiss()
    }
}