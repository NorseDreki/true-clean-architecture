package com.example.domain.submitProposal

import com.example.domain.*
import com.example.domain.models.ItemOpportunity
import com.example.domain.submitProposal.ProposeTerms.*
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.subjects.PublishSubject

class ProposeTerms : UiComponent<Command, Result, ViewState> {

    val cmd = PublishSubject.create<Command>()

    fun fromEvent(command: Command) {
        cmd.onNext(command)
    }

    override fun process(commands: Observable<Command>): Observable<Result> {
        return commands
                .doOnNext { println("CMD " + it) }
                .compose(processor)
                .doOnNext { println("RES " + it) }
                .publish { shared ->
                    Observable.merge(
                            shared,
                            shared.compose(paAnsweredProcessor)//.skip(1)
                    ).doOnNext { println("COMB $it") }
                }
                .cast(Result::class.java)
    }

    override fun render(): Observable<ViewState> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

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

                }.flatMap { it }
            }

    fun calculate(itemOpportunity: ItemOpportunity): Result {
        val bid = itemOpportunity.proposal.bid

        return when (bid) {
            0 -> Result.BidEmpty
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

    data class Validation(
            val isBidValid: Boolean = false,
            val isEngagementRequired: Boolean = true,
            val isEngagementSelected: Boolean = false
    )

    val paAnsweredProcessor =
            ObservableTransformer<Result, Result> { t ->
                t
                        .scan(Validation()) { state, result ->
                            when (result) {
                                is Result.EngagementNotRequired -> {
                                    state.copy(isEngagementRequired = false)
                                }
                                is Result.EngagementSelected -> {
                                    state.copy(isEngagementSelected = true)
                                }
                                is Result.BidRangeExceeded -> {
                                    state.copy(isBidValid = false)
                                }
                                is Result.BidEmpty -> {
                                    state.copy(isBidValid = false)
                                }
                                is Result.BidValid ->
                                        state.copy(isBidValid = true)
                                else -> state
                            }
                        }
                        .skip(1)
                        .flatMap {
                            if (it.isBidValid && (!it.isEngagementRequired || it.isEngagementSelected)) {
                                Observable.just(Result.Validation.OK)
                            } else {
                                Observable.just(Result.Validation.Failed)
                            }
                        }
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

        data class ItemGreetingLoaded(val message: String) : Result()
        data class EngagementsLoaded(val message: String) : Result()

        data class BidValid(val bid: Int, val fee: Int) : Result()
        object BidEmpty : Result()
        data class BidRangeExceeded(val bid: Int, val min: Int, val max: Int) : Result()


        data class EngagementSelected(val engagement: String): Result()
        object EngagementNotSelected: Result()

        object EngagementNotRequired: Result()
    }

    data class ViewState(
            val itemDescription: String = "",
            val bid: Int = 0,
            val isRangeError: Boolean = false,
            val fee: Int = 0
    ) : UiState

}

