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
import com.budgetizer.core.entry.data.model.Entry
import com.budgetizer.core.entry.data.model.EntryType
import com.budgetizer.entry.R
import com.budgetizer.entry.dagger.inject
import com.budgetizer.entry.databinding.ActivityEntryBinding
import java.util.Locale
import javax.inject.Inject

class EntryActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModel: EntryViewModel

    private lateinit var binding: ActivityEntryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEntryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        inject(this)

        initBindingEvents()
        initTypes()
    }

    private fun initBindingEvents() {
        binding.contentNewEntry.addEntry.setOnClickListener {
            val entryType = binding.contentNewEntry.type.editText?.text.toString()

            viewModel.addEntry(
                Entry(
                    type = EntryType.valueOf(entryType.toUpperCase(Locale.getDefault())),
                    label = binding.contentNewEntry.name.editText?.text.toString(),
                    amount = binding.contentNewEntry.amount.editText?.text.toString().toDouble(),
                    tags = emptyList()
                )
            )

            finish()
        }
    }

    private fun initTypes() {
        val types = resources.getStringArray(R.array.entry_types).toList()
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, types)
        (binding.contentNewEntry.type.editText as? AutoCompleteTextView)?.setAdapter(adapter)
    }
}
