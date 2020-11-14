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

package com.budgetizer.ui.entry

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.budgetizer.R
import com.budgetizer.core.entry.data.model.Entry
import com.budgetizer.core.entry.data.model.EntryType
import com.budgetizer.core.util.toFixed
import com.budgetizer.databinding.ItemEntryBinding

class EntryViewHolder constructor(
    private val binding: ItemEntryBinding
) : RecyclerView.ViewHolder(binding.root) {
    private var entry: Entry? = null

    fun bind(entry: Entry) {
        this.entry = entry
        if (entry.type == EntryType.INCOME) {
            binding.trend.apply {
                rotation = binding.trend.rotation * 1
                imageTintList = ContextCompat.getColorStateList(context, R.color.green)
                binding.amount.setTextColor(ContextCompat.getColor(context, R.color.green))
            }
        } else {
            binding.trend.apply {
                rotation = binding.trend.rotation * -1
                imageTintList = ContextCompat.getColorStateList(context, R.color.red)
                binding.amount.setTextColor(ContextCompat.getColor(context, R.color.red))
            }
        }
        binding.entryType.text = entry.type.name
        binding.name.text = entry.label
        binding.amount.text = entry.amount.toFixed()
    }
}
