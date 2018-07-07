package com.example.clean

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.view.View
import com.example.clean.screens.ScreenEvents
import com.example.domain.UiState

class DefaultDataBinder : DataBinder {
    override fun bind(view: View, state: UiState, events: ScreenEvents) {
        DataBindingUtil.bind<ViewDataBinding>(view).run {
            println(">>>>> data binding state $state")

            setVariable(BR.viewModel, state)
            setVariable(BR.events, events)
            //do not run this on each iteration?
            //executePendingBindings()
        }
    }
}
