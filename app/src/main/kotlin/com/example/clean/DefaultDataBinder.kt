package com.example.clean

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.view.View
import com.example.domain.UiState

class DefaultDataBinder : DataBinder {
    override fun bind(view: View, state: UiState, events: ClarifyingQuestionsEvents) {
        DataBindingUtil.bind<ViewDataBinding>(view).run {
            setVariable(BR.viewModel, state)
            setVariable(BR.events, events)
            //do not run this on each iteration?
            //executePendingBindings()
        }
    }
}
