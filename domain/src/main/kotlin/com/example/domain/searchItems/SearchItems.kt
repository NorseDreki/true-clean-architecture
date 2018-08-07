package com.example.domain.searchItems

import com.example.domain.UiCommand
import com.example.domain.UiResult

class SearchItems {

    sealed class Command : UiCommand {

        data class DATA(val query: String) : Command()
    }

    sealed class Result : UiResult {
        object Dismissed : Result()
    }


}