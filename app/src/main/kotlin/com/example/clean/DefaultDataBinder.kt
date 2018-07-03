package com.example.clean

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.view.View

class DefaultDataBinder : DataBinder {
    override fun bind(view: View, viewModel: ViewModel) {
        DataBindingUtil.bind<ViewDataBinding>(view).run {
            //setVariable(BR.viewModel, viewModel)
            executePendingBindings()
        }
    }
}