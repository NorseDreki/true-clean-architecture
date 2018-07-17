package com.example.domain

import io.reactivex.subjects.PublishSubject

data class Depend2(
        val component: DummyComponent,
     //   val results: Observable<DummyComponent.Result>,
        val scopeExit: PublishSubject<Unit>
)