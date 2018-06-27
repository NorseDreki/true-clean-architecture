package com.example.domain.submitProposal

import com.example.domain.*
import com.example.domain.models.ItemOpportunity
import com.example.domain.submitProposal.ProposeTerms.*
import io.reactivex.Observable
import io.reactivex.ObservableTransformer

class ProposeTerms : UiComponent<Command, Result, ViewState> {
    override fun process(commands: Observable<Command>): Observable<Result> {
        return commands
                .doOnNext { println("CMD " + it) }
                .compose(processor)
                .doOnNext { println("RES " + it) }
                .cast(Result::class.java)
    }

    override fun render(): Observable<ViewState> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /*data class State(
            val minBid: String = "",
            val maxBid: String = "",
            val calculator: String = ""
    )*/

    val processor =
            ObservableTransformer<Command, Result> { t ->
                t.publish { shared ->

                    Observable.combineLatest(
                            arrayOf(
                                    shared,
                                    shared.ofType(Command.DATA::class.java).map { it.itemOpportunity }
                            )
                    ) {

                        val command = it[0] as Command
                        val itemOpportunity = it[1] as ItemOpportunity

                        when (command) {
                            is Command.DATA -> {
                                Observable.fromArray(
                                        Result.ItemGreetingLoaded("greeting"),
                                        calculate(itemOpportunity),
                                        duration()
                                )

                            }
                            is Command.UpdateBid -> {
                                Observable.just(calculate(itemOpportunity))
                            }
                            is Command.UpdateEngagement -> {
                                Observable.just(duration())
                            }
                        }

                    }

                }
                        .map { Result.Empty }
                /*t.scan(State()) { state, command ->
                    when (command) {
                        is Command.DATA -> {
                            Result.DATALoaded(it.itemOpportunity.itemDetails)
                        }
                        is Command.UpdateBid -> {
                            result(it.coverLetter)
                        }
                    }
                }*/

            }

    fun calculate(itemOpportunity: ItemOpportunity): Result {
        val bid = itemOpportunity.proposal.bid

        return when (bid) {
            0 -> Result.Empty
            else -> when (bid) {
                in 101..999 -> {
                    val fee = bid / 10
                    Result.BidValid(bid, fee)
                }
                else -> Result.BidRangeExceeded(bid, 100, 1000)
            }
        }
    }

    fun duration(): Result {
        return Result.EngagementNotSelected
    }


    sealed class Command : UiCommand {
        data class DATA(val itemOpportunity: ItemOpportunity) : Command(), UiDataCommand

        //from UI
        data class UpdateBid(val bid: Int) : Command()
        data class UpdateEngagement(val engagement: String) : Command()
    }

    sealed class Result : UiResult {

        sealed class Validation : Result() {
            object OK : Validation()
            object Failed : Validation()
        }

        //data class DATALoaded(val itemDetails: ItemDetails) : Result()
/*
        data class CommissionLoaded(val percents: List<String>) : Result()
        data class DurationsLoaded(val percents: List<String>) : Result()
        data class SecretMessageLoaded(val percents: List<String>) : Result()
        data class RecommendedCommisionMessageLoaded(val percents: List<String>) : Result()
*/

        //data class FeeUpdated(val fee: Int) : Result()

        data class ItemGreetingLoaded(val message: String) : Result()

        data class BidValid(val bid: Int, val fee: Int) : Result()
        object Empty : Result()
        data class BidRangeExceeded(val bid: Int, val min: Int, val max: Int) : Result()


        data class EngagementSelected(val engagement: String): Result()
        object EngagementNotSelected: Result()
    }

    data class ViewState(
            val itemDescription: String = "",
            val bid: Int = 0,
            val isRangeError: Boolean = false,
            val fee: Int = 0
    ) : UiState

}

