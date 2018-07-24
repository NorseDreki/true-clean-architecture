package com.example.clean

import android.app.AlertDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.databinding.BindingAdapter
import android.view.View
import com.example.domain.submitProposal2.common.DialogEvents
import com.example.domain.submitProposal2.common.DialogState

object Binding {

    @JvmStatic
    @BindingAdapter("abc", "def")
    fun bindAbc(view: View, ds: DialogState, plot: DialogEvents) {
        println("111111 bound $ds")
        println("111111 bound $plot")
        println("111111 to view $view")


        val rootView = view.rootView
        println("root view is $rootView")

        val content = rootView.findViewById<View>(android.R.id.content)
        println("content view is $content")

        var dialog: Dialog? = rootView.getTag(R.id.afterTextChangeListener) as? Dialog

        when (ds) {
            is DialogState.Progress -> {
                val d = ProgressDialog.show(view.context, ds.title, ds.message, true, true)
                println("setting tag on $rootView")
                rootView.setTag(R.id.afterTextChangeListener, d)
            }
            is DialogState.Dismissed -> {
                println("should dismiss")
                if (dialog != null) {
                    println("dismissing")
                    dialog.dismiss()
                }
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
