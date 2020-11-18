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

package com.budgetizer.entry.ui

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AppCompatActivity
import com.budgetizer.core.R
import com.budgetizer.core.data.entry.model.Entry
import com.budgetizer.core.data.entry.model.EntryRange
import com.budgetizer.core.data.entry.model.EntryType
import com.budgetizer.core.util.Activities
import com.budgetizer.entry.dagger.inject
import com.budgetizer.entry.databinding.ActivityEntryBinding
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class EntryActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModel: EntryViewModel

    private lateinit var binding: ActivityEntryBinding
    private var entryToUpdate: Entry? = null
    private var curDate = System.currentTimeMillis()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEntryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        inject(this)

        initParcels()
        initBindingEvents()
        initTypes()
        initEntryRanges()
    }

    private fun initParcels() {
        if (intent.hasExtra(Activities.Entry.EXTRA_UPDATE_ENTRY)) {
            entryToUpdate = intent.getParcelableExtra(Activities.Entry.EXTRA_UPDATE_ENTRY)
        }

        if (intent.hasExtra(Activities.Entry.EXTRA_DATE_ENTRY)) {
            curDate =
                intent.getLongExtra(Activities.Entry.EXTRA_DATE_ENTRY, System.currentTimeMillis())
        }
    }

    private fun initBindingEvents() {
        if (entryToUpdate != null) {
            binding.greeting.setText(com.budgetizer.entry.R.string.update_entry_title)
            binding.contentNewEntry.addEntry.setText(com.budgetizer.entry.R.string.update_entry)

            binding.contentNewEntry.type.editText?.setText(resources.getStringArray(R.array.entry_types)[entryToUpdate?.type?.ordinal!!])
            binding.contentNewEntry.amount.editText?.setText(entryToUpdate?.amount.toString())
            binding.contentNewEntry.name.editText?.setText(entryToUpdate?.label)
            binding.contentNewEntry.range.editText?.setText(resources.getStringArray(R.array.entry_ranges)[entryToUpdate?.entryRange?.ordinal!!])
        }

        binding.contentNewEntry.addEntry.setOnClickListener {
            if (!isInputsValid()) return@setOnClickListener

            val entryType = binding.contentNewEntry.type.editText?.text.toString()
            val entryRange = binding.contentNewEntry.range.editText?.text.toString()
            val entryRangeIndex =
                resources.getStringArray(R.array.entry_ranges).toList().indexOf(entryRange)

            if (entryToUpdate != null) {
                viewModel.updateEntry(
                    entryToUpdate!!.copy(
                        id = entryToUpdate!!.id,
                        type = EntryType.valueOf(entryType.toUpperCase(Locale.getDefault())),
                        label = binding.contentNewEntry.name.editText?.text.toString(),
                        amount = binding.contentNewEntry.amount.editText?.text.toString()
                            .toDouble(),
                        tags = emptyList(),
                        createdAt = entryToUpdate!!.createdAt,
                        updatedAt = Date(),
                        entryRange = enumValues<EntryRange>()[entryRangeIndex]
                    )
                )
            } else {
                viewModel.addEntry(
                    Entry(
                        type = EntryType.valueOf(entryType.toUpperCase(Locale.getDefault())),
                        label = binding.contentNewEntry.name.editText?.text.toString(),
                        amount = binding.contentNewEntry.amount.editText?.text.toString()
                            .toDouble(),
                        tags = emptyList(),
                        createdAt = Date(curDate),
                        updatedAt = Date(),
                        entryRange = enumValues<EntryRange>()[entryRangeIndex]
                    )
                )
            }

            finish()
        }
    }

    private fun initTypes() {
        val types = resources.getStringArray(R.array.entry_types).toList()
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, types)
        (binding.contentNewEntry.type.editText as? AutoCompleteTextView)?.setAdapter(adapter)
    }

    private fun initEntryRanges() {
        val ranges = resources.getStringArray(R.array.entry_ranges).toList()
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, ranges)
        (binding.contentNewEntry.range.editText as? AutoCompleteTextView)?.setAdapter(adapter)
    }

    private fun isInputsValid(): Boolean {
        if (binding.contentNewEntry.type.editText?.text?.isEmpty() == true) {
            binding.contentNewEntry.type.error = getString(R.string.error_required_fields)
            return false
        }
        if (binding.contentNewEntry.name.editText?.text?.isEmpty() == true) {
            binding.contentNewEntry.name.error = getString(R.string.error_required_fields)
            return false
        }
        if (binding.contentNewEntry.amount.editText?.text?.isEmpty() == true) {
            binding.contentNewEntry.amount.error = getString(R.string.error_required_fields)
            return false
        }
        if (binding.contentNewEntry.range.editText?.text?.isEmpty() == true) {
            binding.contentNewEntry.range.error = getString(R.string.error_required_fields)
            return false
        }

        return true
    }
}
