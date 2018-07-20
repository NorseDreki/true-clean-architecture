package com.example.domain.submitProposal2.fees


import com.example.domain.UiCommand
import com.example.domain.UiResult
import io.reactivex.ObservableTransformer

sealed class FeesCommands : UiCommand {
    object LoadCalculator : FeesCommands()
    object LoadUpdatedCalculator : FeesCommands()
}

sealed class FeesResult : UiResult {
    data class CalculatorLoaded(val calculator: FeesCalculator) : FeesResult()
}

interface ScriptsService {
    fun getScript(s: String): String
    fun getScriptFromNetworkIgnoreCache(s: String): String

}

interface FeesCalculator {
    fun evaluateFeesScript(script: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}

val feesProc =
        ObservableTransformer<FeesCommands, UiResult> {
            it.map {
                when (it) {
                    FeesCommands.LoadCalculator -> {
                        var service: ScriptsService? = null

                        val script = service!!.getScript("1234")
                        val calc: FeesCalculator? = null

                        calc!!.evaluateFeesScript(script)

                        FeesResult.CalculatorLoaded(calc)
                    }
                    FeesCommands.LoadUpdatedCalculator -> {
                        var service: ScriptsService? = null

                        val script = service!!.getScriptFromNetworkIgnoreCache("1234")
                        val calc: FeesCalculator? = null

                        calc!!.evaluateFeesScript(script)

                        FeesResult.CalculatorLoaded(calc)
                    }
                }
            }
        }
