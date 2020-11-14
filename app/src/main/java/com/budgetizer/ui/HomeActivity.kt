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

package com.budgetizer.ui

import android.content.Context
import android.os.Bundle
import android.text.Annotation
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.SpannedString
import android.text.style.ImageSpan
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewStub
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.budgetizer.R
import com.budgetizer.core.entry.data.model.Entry
import com.budgetizer.core.entry.data.model.EntryType
import com.budgetizer.core.util.Activities
import com.budgetizer.core.util.intentTo
import com.budgetizer.core.util.toFixed
import com.budgetizer.dagger.inject
import com.budgetizer.databinding.ActivityHomeBinding
import com.budgetizer.ui.entry.EntriesAdapter
import javax.inject.Inject

class HomeActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModel: HomeViewModel

    private lateinit var binding: ActivityHomeBinding
    private lateinit var entriesAdapter: EntriesAdapter

    private val noItemsEmptyText by lazy {
        val view = findViewById<ViewStub>(R.id.stub_no_items).inflate() as CardView
        // create the no filters empty text
        val viewText = view.findViewById<TextView>(R.id.no_items)

        val emptyText = getText(R.string.no_items) as SpannedString
        val ssb = SpannableStringBuilder(emptyText)
        val annotations = emptyText.getSpans(0, emptyText.length, Annotation::class.java)

        annotations?.forEach { annotation ->
            if (annotation.key == "src") {
                // image span markup
                val id = annotation.getResId(this@HomeActivity)
                if (id != 0) {
                    ssb.setSpan(
                        ImageSpan(this, id, ImageSpan.ALIGN_BASELINE),
                        emptyText.getSpanStart(annotation),
                        emptyText.getSpanEnd(annotation),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }
            }
        }

        with(viewText) {
            text = ssb
            setOnClickListener {
                addEntry()
            }
            view
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        inject(this)

        entriesAdapter = EntriesAdapter(this)

        setSupportActionBar(binding.toolbar)

        checkEmptyState()

        initViewModelObservers()
        initBindingResources()
    }

    override fun onResume() {
        super.onResume()
        fetchEntries()
        updateHeader()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        setupToolbar()
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        if (!viewModel.hasProfile()) {
            startActivity(intentTo(Activities.Profile))
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_add_entry -> {
                addEntry()
                true
            }
            R.id.menu_profile -> {
                startActivity(intentTo(Activities.Profile))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initViewModelObservers() {
        viewModel.events.observe(this, { _events ->
            val events = _events ?: return@observe

            when (events) {
                is HomeEvents.EntriesUpdate -> {
                    updateEntries(events.entries)
                }
            }
        })
    }

    private fun initBindingResources() {
        binding.contentHome.recyclerView.adapter = entriesAdapter
    }

    private fun setupToolbar() {
        binding.toolbar.inflateMenu(R.menu.home_menu)

        supportActionBar?.apply {
            setDisplayShowTitleEnabled(false)
        }
    }

    private fun fetchEntries() {
        viewModel.getEntries()
    }

    private fun updateEntries(items: List<Entry>) {
        entriesAdapter.items = items

        checkEmptyState()
        updateHeader()
    }

    private fun updateHeader() {
        viewModel.profile?.apply {
            val additional =
                entriesAdapter.items
                    .filter { it.type == EntryType.INCOME }
                    .sumByDouble { it.amount }
            val negation =
                entriesAdapter.items
                    .filter { it.type == EntryType.EXPENSE }
                    .sumByDouble { it.amount }

            val total = (grossMonthlyIncome + additional) - negation

            binding.contentHeader.greeting.text = "Hi, $givenName!"
            binding.contentHeader.header.text = total.toFixed()
        }
    }

    private fun checkEmptyState() {
        if (entriesAdapter.items.isEmpty()) {
            setNoItemsVisibility(View.VISIBLE)
        } else {
            setNoItemsVisibility(View.GONE)
        }
    }

    private fun addEntry() {
        startActivity(intentTo(Activities.Entry))
    }

    private fun setNoItemsVisibility(visibility: Int) {
        noItemsEmptyText.visibility = visibility
    }
}

fun Annotation.getResId(context: Context): Int {
    return context.resources.getIdentifier(value, null, context.packageName)
}
