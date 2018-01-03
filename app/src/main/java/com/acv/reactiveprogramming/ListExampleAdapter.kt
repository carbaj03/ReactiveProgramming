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
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

abstract class ViewHolder<in M>(view: View) : RecyclerView.ViewHolder(view) {
    abstract fun bind(model: M)
}

class ListExampleVH(view: View) : ViewHolder<String>(view) {
    var tvName: TextView = view.findViewById(R.id.tvName)

    override fun bind(model: String) {
        tvName.text = model
    }
}

class ListExampleAdapter(f: (String, String) -> Boolean) : BaseRecyclerAdapter<String, ListExampleVH>(f) {

    private lateinit var selected: MutableSet<String>

    override fun getItemViewType(position: Int) =
            R.layout.item_list

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListExampleVH =
            ListExampleVH(parent.inflate(viewType))

    override fun onBindViewHolder(holder: ListExampleVH, position: Int, element: String) {
        holder.bind(element)
    }

    fun swapSelected(newSelected: MutableSet<String>) {
        val diffResult = DiffUtil.calculateDiff(createSelectDiffCallback(newSelected), false)
        selected.clear()
        selected.addAll(newSelected)
        diffResult.dispatchUpdatesTo(this)
    }

    @NonNull
    private fun createSelectDiffCallback(newSelected: MutableSet<String>): DiffUtil.Callback {
        return object : DiffUtil.Callback() {
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                val element = getPosition(oldItemPosition)
                val newElement = getPosition(newItemPosition)
                return element == newElement && selected.contains(newElement) == newSelected.contains(newElement)
            }

            override fun getOldListSize(): Int = itemCount

            override fun getNewListSize(): Int = itemCount

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                val element = getPosition(oldItemPosition)
                val newElement = getPosition(newItemPosition)
                return element == newElement
            }
        }
    }
}

