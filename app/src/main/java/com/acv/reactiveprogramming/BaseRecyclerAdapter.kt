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

package com.acv.reactiveprogramming

import android.support.annotation.NonNull
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable


/**
 * Abstract base class implementation of {@link RecyclerView.Adapter} containing support for reactive operations, like
 * swapping the elements, or observing single and long taps.
 *
 * @param <T> type of elements held
 * @param <U> type of the {@link RecyclerView.ViewHolder} attached to this instance
 */
abstract class BaseRecyclerAdapter<T, U : RecyclerView.ViewHolder>(val hasSameId: (T, T) -> Boolean) : RecyclerView.Adapter<U>() {
    private var values: MutableList<T> = mutableListOf()

    private val clicksRelay: PublishRelay<Pair<Int, T>> = PublishRelay.create()

    private val longClicksRelay: PublishRelay<Pair<Int, T>> = PublishRelay.create()

    fun swap(newValues: List<T>) {
        val diffResult = DiffUtil.calculateDiff(createElementsDiffCallback(newValues), true)
        values.clear()
        values.addAll(newValues)
        diffResult.dispatchUpdatesTo(this)
    }

    @NonNull
    private fun createElementsDiffCallback(newValues: List<T>): DiffUtil.Callback {
        return object : DiffUtil.Callback() {
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                    hasSameId(values[oldItemPosition], newValues[newItemPosition])

            override fun getOldListSize(): Int = values.size

            override fun getNewListSize(): Int = newValues.size

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                    values[oldItemPosition] == newValues[newItemPosition]
        }
    }

    override fun onBindViewHolder(holder: U, position: Int) {
        val element = values[position]
        RxView.clicks(holder.itemView).map({ Pair(holder.adapterPosition, element) }).subscribe(clicksRelay)
        RxView.longClicks(holder.itemView).map({ Pair(holder.adapterPosition, element) }).subscribe(longClicksRelay)
        onBindViewHolder(holder, position, element)
    }

    override fun getItemCount(): Int = values.size

    protected abstract fun onBindViewHolder(holder: U, position: Int, element: T)

    protected fun getPosition(position: Int) = values[position]

    fun getClicks(): Observable<Pair<Int, T>> = clicksRelay.hide()

    fun getLongClicks(): Observable<Pair<Int, T>> = longClicksRelay.hide()

}
