package com.example.domain

import com.example.domain.submitProposal.ClarifyingQuestions
import com.example.domain.submitProposal.models.JobDetails
import io.reactivex.Observable
import io.reactivex.subscribers.TestSubscriber
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith

@RunWith(JUnitPlatform::class)
class ClarifyingQuestionsSpec : Spek({

    context("some context") {
        val aaa = 1
        val cq = ClarifyingQuestions()

        on("component is told to initialize") {
            val jobDetails = JobDetails()
            val commands =
                    Observable.just<ClarifyingQuestions.Command>(ClarifyingQuestions.Command.INIT(jobDetails))

            it("should populate questions with answers") {

                cq.processCommands(commands)

                /*val sub = TestSubscriber<UiResult>()
                cq.produceResults().subscribe(sub)*/

                cq.produceResults().test().assertValue(ClarifyingQuestions.Result.NoQuestions)
            }
        }
    }
})
