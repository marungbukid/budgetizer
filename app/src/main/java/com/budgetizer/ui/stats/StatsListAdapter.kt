/*
 * Copyright 2020 Jan Maru De Guzman.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.budgetizer.ui.stats

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.budgetizer.databinding.ItemStatsViewBinding
import com.budgetizer.entry.EntryHolder

class StatsListAdapter(
    private val host: Activity
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val layoutInflater = LayoutInflater.from(host)

    var items = emptyList<EntryHolder>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return createStatsItemViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as StatsItemViewHolder).bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    private fun createStatsItemViewHolder(parent: ViewGroup): StatsItemViewHolder {
        return StatsItemViewHolder(
            ItemStatsViewBinding.inflate(layoutInflater, parent, false)
        )
    }
}
