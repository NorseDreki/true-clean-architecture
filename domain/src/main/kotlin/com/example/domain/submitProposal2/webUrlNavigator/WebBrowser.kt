package com.example.domain.submitProposal2.webUrlNavigator

import com.example.domain.UiCommand
import com.example.domain.UiResult
import com.example.domain.UiState
import com.example.domain.framework.ExtraCommandsComponent
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer

class WebBro(
        val navigator: WebUrlNavigator
) : ObservableTransformer<WebBro.Command, WebBro.Result> {

    override fun apply(upstream: Observable<Command>): ObservableSource<Result> {
        return upstream.map {
            navigator.navigateToUrl((it as Command.DATA).url)
            Result.Navigated
        }
    }

    sealed class Command : UiCommand {
        data class DATA(val url: String) : Command()
    }

    sealed class Result : UiResult {
        object Navigated : Result()
    }
}

class WebBrowser(
        val navigator: WebUrlNavigator
) : ExtraCommandsComponent<WebBrowser.Command, WebBrowser.Result, WebBrowser.ViewState>() {

    val pcProcessor =
            ObservableTransformer<Command, Result> {
                it.map {
                    when (it) {
                        is Command.DATA -> Result.DATALoaded("s")
                        Command.Dismiss -> Result.Dismissed
                    }
                }
            }

    val pcReducer =
            ObservableTransformer<Result, ViewState> {
                it.flatMap {
                    when (it) {
                        is Result.DATALoaded -> Observable.just(ViewState(it.itemOpportunity))
                        is Result.Dismissed -> {
                            println("DISMISSED DUMMY")
                            Observable.empty()
                        }
                    }
                }
            }

    override val processor = pcProcessor
    override val reducer = pcReducer


    sealed class Command : UiCommand {
        data class DATA(val url: String) : Command()

        object Dismiss : Command()
    }

    sealed class Result : UiResult {
        data class DATALoaded(val itemOpportunity: String) : Result()

        object Dismissed : Result()
    }

    data class ViewState(
            val title: String
    ) : UiState
}
