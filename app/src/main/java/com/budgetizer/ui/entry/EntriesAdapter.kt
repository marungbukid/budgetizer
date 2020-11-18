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

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.budgetizer.R
import com.budgetizer.core.data.entry.model.EntryRange
import com.budgetizer.core.util.Activities
import com.budgetizer.core.util.intentTo
import com.budgetizer.databinding.ItemEntryBinding
import com.budgetizer.databinding.ItemHeaderBinding
import com.budgetizer.ui.view.ItemHeaderViewHolder

class EntriesAdapter(
    private val host: Activity
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val layoutInflater = LayoutInflater.from(host)

    var items = emptyList<EntryListItem>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_RANGE) {
            createEntryRangeViewHolder(parent)
        } else {
            createEntryViewHolder(parent)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            VIEW_TYPE_RANGE -> {
                val range = items[position] as EntryRangeItem
                val rangeValue =
                    host.resources.getStringArray(R.array.entry_ranges)[EntryRange.valueOf(
                        range.range.name
                    ).ordinal]

                (holder as ItemHeaderViewHolder)
                    .bind(rangeValue)
            }
            VIEW_TYPE_ENTRY -> {
                (holder as EntryViewHolder)
                    .bind(items[position] as EntryItem)
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemId(position: Int): Long {
        return items[position].hashCode().toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return items[position].type()
    }

    private fun createEntryViewHolder(
        parent: ViewGroup
    ): EntryViewHolder {
        return EntryViewHolder(ItemEntryBinding.inflate(layoutInflater, parent, false)) {
            val intent = intentTo(Activities.Entry)
            intent.putExtra(Activities.Entry.EXTRA_UPDATE_ENTRY, it)

            host.startActivity(intent)
        }
    }

    private fun createEntryRangeViewHolder(
        parent: ViewGroup
    ): ItemHeaderViewHolder {
        return ItemHeaderViewHolder(ItemHeaderBinding.inflate(layoutInflater, parent, false))
    }

    companion object {
        const val VIEW_TYPE_ENTRY = 0
        const val VIEW_TYPE_RANGE = 1
    }
}
