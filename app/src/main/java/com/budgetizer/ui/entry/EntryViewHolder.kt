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
import com.budgetizer.core.data.entry.model.Entry
import com.budgetizer.core.data.entry.model.EntryType
import com.budgetizer.core.util.toFixed
import com.budgetizer.databinding.ItemEntryBinding

class EntryViewHolder constructor(
    private val binding: ItemEntryBinding,
    private val onItemCLick: (entry: Entry) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    private var entry: Entry? = null

    fun bind(entryItem: EntryItem) {
        this.entry = entryItem.entry
        with(binding.itemEntry) {
            if (entry?.type == EntryType.INCOME) {
                trend.apply {
                    setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.ic_baseline_trending_up_24
                        )
                    )
                    imageTintList = ContextCompat.getColorStateList(context, R.color.green)
                    amount.setTextColor(ContextCompat.getColor(context, R.color.green))
                }
            } else {
                trend.apply {
                    setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.ic_baseline_trending_down_24
                        )
                    )
                    imageTintList = ContextCompat.getColorStateList(context, R.color.red)
                    amount.setTextColor(ContextCompat.getColor(context, R.color.red))
                }
            }
            name.text = entry?.label
            amount.text = entry?.amount?.toFixed()
            date.text = entry?.createdAt?.toFixed()
        }

        binding.itemEntry.root.setOnClickListener {
            onItemCLick.invoke(entryItem.entry)
        }
    }
}
