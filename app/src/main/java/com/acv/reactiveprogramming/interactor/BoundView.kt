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

import com.acv.reactiveprogramming.state.StateHolder
import io.reactivex.functions.BiConsumer

/**
 * Interface representing a screen that can be bound one-way to a [StateHolder]
 */
interface BoundView {
    /**
     * Creates a view-state binding function for the current screen
     * @return binder function
     */
    fun <T: Any> createBinder(): BiConsumer<StateHolder<T>, (T) -> Unit>
}