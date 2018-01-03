/*
 * Copyright (c) pakoito 2016
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.acv.reactiveprogramming.interactor

import com.acv.reactiveprogramming.state.PaginationExampleState
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * Binds the state of this use case to a [com.pacoworks.dereference.architecture.ui.BoundView]
 *
 * @see [com.pacoworks.dereference.architecture.ui.bind]
 */
fun bindPaginationExample(viewInput: PaginationExampleInputView, state: PaginationExampleState) =
        viewInput.createBinder<List<String>>().accept(state.elements, { viewInput.updateElements(it) })

/**
 * Subscribes all use cases in the file
 */
fun PaginationExampleOutputView.subscribePaginationExample(
        state: PaginationExampleState,
        service: PaginationExampleService
) = CompositeDisposable(handleLoading(this, state, service))

fun handleLoading(
        viewOutput: PaginationExampleOutputView,
        state: PaginationExampleState,
        service: PaginationExampleService
): Disposable = doSM(
        /* For the current elements */
        { state.elements },
        /* Wait until end of page is reached */
        { viewOutput.endOfPage().take(1) },
        /* See what the latest page is */
        { _, _ -> state.pages.take(1) },
        /* Request new page */
        { elements, _, page ->
            service(page)
                    .map { elements.plus(it) }
                    /* Update pages inline after a success */
                    .doOnNext { state.pages.accept(page + 1) }
        }
).subscribe(state.elements)

fun <A, B, C, R> doSM(
        zero: () -> Observable<A>,
        one: (A) -> Observable<B>,
        two: (A, B) -> Observable<C>,
        three: (A, B, C) -> Observable<R>
): Observable<R> =
        zero().switchMap { a ->
            one(a).switchMap { b ->
                two(a, b).switchMap { c ->
                    three(a, b, c)
                }
            }
        }