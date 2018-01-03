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

import io.reactivex.Observable

typealias PaginationExampleService = (Int) -> Observable<List<String>>
typealias GetItemsService = () -> Observable<List<String>>

fun requestMore(page: Int): Observable<List<String>> =
        Observable.range(0, 10)
                .map { ((10 * (page + 1)) + it).toString() }
                .toList().toObservable()

fun getItems(): Observable<List<String>> =
        Observable.range(0, 30).map { it.toString() }.toList().toObservable()