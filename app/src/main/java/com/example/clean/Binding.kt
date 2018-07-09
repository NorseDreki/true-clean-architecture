package com.example.clean

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

        //ProgressDialog.show(view.context, "title", "message", true)
    }
}
