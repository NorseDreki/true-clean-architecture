package com.example.clean

import android.app.AlertDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.databinding.BindingAdapter
import android.view.View
import com.example.domain.submitProposal.DialogEvents
import com.example.domain.submitProposal.DialogState

object Binding {

    @JvmStatic
    @BindingAdapter("abc", "def")
    fun bindAbc(view: View, ds: DialogState, plot: DialogEvents) {
        println("111111 bound $ds")
        println("111111 bound $plot")

        var dialog: Dialog? = view.getTag(R.id.afterTextChangeListener) as? Dialog

        when (ds) {
            is DialogState.Progress -> {
                val d = ProgressDialog.show(view.context, ds.title, ds.message, true, false)
                view.setTag(R.id.afterTextChangeListener, d)
            }
            is DialogState.Dismiss -> {
                if (dialog != null) dialog.dismiss()
            }
            is DialogState.Alert -> {
                val d = AlertDialog.Builder(view.context)
                        .setCancelable(false)
                        //.setTitle(ds.title)
                        .setMessage(ds.message)
                        .setPositiveButton(ds.positiveText) { dialog, which ->
                            plot.onPositive()
                        }
                        .setNegativeButton(null) { dialog, which ->
                            plot.onNegative()
                        }.show()


                println("none yet")
            }
        }

        //ProgressDialog.show(view.context, "title", "message", true)
    }
}
