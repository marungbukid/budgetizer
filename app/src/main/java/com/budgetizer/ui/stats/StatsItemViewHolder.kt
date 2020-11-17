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

import android.view.View
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView
import com.budgetizer.R
import com.budgetizer.core.data.entry.model.EntryRange
import com.budgetizer.core.util.toFixed
import com.budgetizer.databinding.ItemStatsViewBinding
import com.budgetizer.entry.EntryHolder
import com.marungbukid.charts.BaseChart
import java.util.Date

class StatsItemViewHolder(
    private val binding: ItemStatsViewBinding
) : RecyclerView.ViewHolder(binding.root), BaseChart.OnScrubListener {
    private val adapter = StatsChartAdapter()

    fun bind(item: EntryHolder) {
        binding.itemEntryRange.range.text =
            binding.root.context.resources.getStringArray(R.array.entry_stat_ranges)[EntryRange.valueOf(
                item.range.name
            ).ordinal]

        binding.lineChart.setAdapter(adapter)
        binding.lineChart.scrubListener = this
        adapter.items = item.items.reversed().map {
            StatsChartEntry(
                index = it.id,
                value = it.amount,
                timestamp = it.createdAt.time,
                label = it.label
            )
        }
    }

    override fun onScrubbed(value: Any?) {
        binding.scrubDetails.children.forEach {
            it.visibility = if (value != null) View.VISIBLE else View.INVISIBLE
        }
        (value as? StatsChartEntry)?.let {
            binding.date.text = Date(it.timestamp).toFixed("MMM dd, yyyy")
            binding.amount.text = it.value.toFixed()
            binding.name.text = it.label
        }
    }
}
