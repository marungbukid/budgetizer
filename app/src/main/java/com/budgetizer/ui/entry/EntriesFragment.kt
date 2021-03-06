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

import android.os.Bundle
import android.text.Annotation
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.SpannedString
import android.text.style.ImageSpan
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.budgetizer.R
import com.budgetizer.core.data.entry.model.Entry
import com.budgetizer.core.data.entry.model.EntryType
import com.budgetizer.core.util.Activities
import com.budgetizer.core.util.getResId
import com.budgetizer.core.util.intentTo
import com.budgetizer.core.util.isSameYear
import com.budgetizer.core.util.toFixed
import com.budgetizer.dagger.inject
import com.budgetizer.databinding.FragmentEntriesBinding
import com.budgetizer.entry.EntriesManager
import com.budgetizer.ui.HomeActivity
import com.budgetizer.ui.HomeEvents
import com.budgetizer.ui.HomeViewModel
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.datepicker.MaterialDatePicker
import java.lang.Math.abs
import java.util.Date
import javax.inject.Inject

class EntriesFragment : Fragment() {

    @Inject
    lateinit var viewModel: HomeViewModel

    private var _binding: FragmentEntriesBinding? = null
    private val binding get() = _binding!!

    private lateinit var entriesAdapter: EntriesAdapter

    private var curDate = System.currentTimeMillis()
    private val selectedDate
        get() = Date(curDate).toFixed()
    private var entriesManager = EntriesManager(emptyList())

    private val noItemsEmptyText by lazy {
        val view = requireView().findViewById<ViewStub>(R.id.stub_no_items).inflate() as CardView
        // create the no filters empty text
        val viewText = view.findViewById<TextView>(R.id.no_items)

        val emptyText = getText(R.string.no_items) as SpannedString
        val ssb = SpannableStringBuilder(emptyText)
        val annotations = emptyText.getSpans(0, emptyText.length, Annotation::class.java)

        annotations?.forEach { annotation ->
            if (annotation.key == "src") {
                // image span markup
                val id = annotation.getResId(requireActivity())
                if (id != 0) {
                    ssb.setSpan(
                        ImageSpan(requireActivity(), id, ImageSpan.ALIGN_BASELINE),
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
        inject(this)

        entriesAdapter = EntriesAdapter(requireActivity())

        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEntriesBinding.inflate(inflater, container, false)
        initViewModelObservers()
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initBindingResources()
        checkEmptyState()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as? HomeActivity)?.setSupportActionBar(binding.toolbar)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        if (!viewModel.hasProfile()) {
            startActivity(intentTo(Activities.Profile))
        }
        fetchEntries()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        setupToolbar()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_date -> {
                selectDate()
                true
            }
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
        viewModel.events.observe(viewLifecycleOwner, { _events ->
            val events = _events ?: return@observe

            when (events) {
                is HomeEvents.MonthEntriesUpdate -> {
                    updateBudget(events.entries)
                    updateEntries(events.entries)
                }
            }
        })
    }

    private fun initBindingResources() {
        binding.contentHome.recyclerView.adapter = entriesAdapter
    }

    private fun setupToolbar() {
        binding.toolbar.inflateMenu(R.menu.entry_menu)
        binding.appbar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            binding.collapsingToolbar.title =
                if (abs(verticalOffset) != binding.appbar.totalScrollRange)
                    " " else selectedDate
        })
    }

    private fun fetchEntries() {
        viewModel.getMonthEntriesToDate(Date(curDate))
    }

    private fun updateEntries(items: List<Entry>) {
        entriesManager = EntriesManager(items, Date(curDate))
        entriesAdapter.items = entriesManager.getEntries()

        checkEmptyState()
    }

    private fun updateBudget(items: List<Entry>) {
        viewModel.profile?.apply {
            val additional =
                items
                    .filter { it.type == EntryType.INCOME && it.createdAt.isSameYear(Date()) }
                    .sumByDouble { it.calculatedAmount() }
            val negation =
                items
                    .filter { it.type == EntryType.EXPENSE && it.createdAt.isSameYear(Date()) }
                    .sumByDouble { it.calculatedAmount() }

            val total = (grossMonthlyIncome + additional) - negation
            binding.contentHeader.header.text = total.toFixed()
            binding.contentHeader.greeting.text = "Hi, $givenName!"
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
        val intent = intentTo(Activities.Entry)
        intent.putExtra(Activities.Entry.EXTRA_DATE_ENTRY, curDate)
        startActivity(intent)
    }

    private fun setNoItemsVisibility(visibility: Int) {
        noItemsEmptyText.visibility = visibility
    }

    private fun selectDate() {
        val builder = MaterialDatePicker.Builder.datePicker()
        builder.build().also {
            it.addOnPositiveButtonClickListener { unixTime ->
                curDate = unixTime
                fetchEntries()
            }
            it.show(childFragmentManager, "picker")
        }
    }
}
