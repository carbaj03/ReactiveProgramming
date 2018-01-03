package com.acv.reactiveprogramming.state

import io.reactivex.Observable


data class PaginationExampleState (
        val elements: StateHolder<List<String>> = createStateHolder(
                Observable.range(0, 30).map { it.toString() }.toList().blockingGet()),
        val pages: StateHolder<Int> = createStateHolder(2)
)
