package com.example.clean

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.view.View
import com.example.domain.UiState

class DefaultDataBinder : DataBinder {
    override fun bind(view: View, state: UiState) {
        DataBindingUtil.bind<ViewDataBinding>(view).run {
            setVariable(BR.viewModel, state)
            executePendingBindings()
        }
    }
}
