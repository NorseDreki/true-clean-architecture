package com.example.domain.searchItems.searchQuery

import com.example.domain.searchItems.searchQuery.SearchQuery.Command
import com.example.domain.searchItems.searchQuery.SearchQuery.Result
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import java.util.concurrent.TimeUnit

class SearchQueryProcessor : ObservableTransformer<Command, Result> {

    override fun apply(upstream: Observable<Command>) =
            upstream
                    .debounce(3, TimeUnit.SECONDS)
                    .map {
                        when (it) {
                            is Command.UpdateSearchQuery -> {
                                Result.SanitizedQuery(it.query.trim())
                            }
                        }
                    }
                    .cast(Result::class.java)!!

/*
    private fun result(coverLetter: String): Result {
        //save cover letter to submit proposal storage?

        val validated = coverLetter.trim()

        val maxLength = 5000
        return when {
            validated.length > maxLength -> Result.LengthExceeded(validated, maxLength)
            validated.isNotEmpty() -> Result.Valid(validated)
            else -> Result.Empty
        }
    }
*/
}
