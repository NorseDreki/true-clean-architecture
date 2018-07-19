package com.example.domain

import com.example.domain.framework.StandaloneComponent
import io.reactivex.subjects.PublishSubject

data class Depend2(
        val component: StandaloneComponent<SampleComponent.Command, SampleComponent.Result, SampleComponent.ViewState>,
     //   val results: Observable<DummyComponent.Result>,
        val scopeExit: PublishSubject<Unit>
)