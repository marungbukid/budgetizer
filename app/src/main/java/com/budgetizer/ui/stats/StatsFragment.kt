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
import com.budgetizer.core.util.Activities
import com.budgetizer.core.util.getResId
import com.budgetizer.core.util.intentTo
import com.budgetizer.core.util.toFixedMonthOnly
import com.budgetizer.dagger.inject
import com.budgetizer.databinding.FragmentStatsBinding
import com.budgetizer.entry.EntriesManager
import com.budgetizer.ui.HomeActivity
import com.budgetizer.ui.HomeEvents
import com.budgetizer.ui.HomeViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import java.util.Date
import javax.inject.Inject

class StatsFragment : Fragment() {

    @Inject
    lateinit var viewModel: HomeViewModel

    private var _binding: FragmentStatsBinding? = null
    private val binding get() = _binding!!

    private var adapter: StatsListAdapter? = null

    private var selectedDate = Date()

    private val noItemsEmptyText by lazy {
        val view = requireView().findViewById<ViewStub>(R.id.stub_no_items).inflate() as CardView
        // create the no filters empty text
        val viewText = view.findViewById<TextView>(R.id.no_items)

        val emptyText = getText(R.string.no_stat_items) as SpannedString
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
            view
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStatsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        adapter = StatsListAdapter(requireActivity())

        initViewModelObservers()
        initBindingResources()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkEmptyState()
    }

    override fun onResume() {
        super.onResume()

        fetchEntries()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        setupToolbar()
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        if (!viewModel.hasProfile()) {
            startActivity(intentTo(Activities.Profile))
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_date -> {
                selectDate()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun fetchEntries() {

        viewModel.getMonthEntriesToDate(selectedDate)
    }

    private fun initViewModelObservers() {
        viewModel.events.observe(viewLifecycleOwner, { _events ->
            val events = _events ?: return@observe

            when (events) {
                is HomeEvents.EntriesUpdate -> {
                }
                is HomeEvents.MonthEntriesUpdate -> {
                    val manager = EntriesManager(events.entries)

                    adapter?.items = manager.getMonthlyEntries().filter {
                        it.items.size > 1
                    }

                    checkEmptyState()
                }
            }
        })
    }

    private fun initBindingResources() {
        binding.recyclerView.adapter = adapter
    }

    private fun setupToolbar() {
        (requireActivity() as? HomeActivity)?.setSupportActionBar(binding.toolbar)
        updateTitle()
        binding.toolbar.inflateMenu(R.menu.stats_menu)
    }

    private fun updateTitle() {
        (requireActivity() as? HomeActivity)?.supportActionBar?.apply {
            title = Date(System.currentTimeMillis()).toFixedMonthOnly()
        }
    }

    private fun checkEmptyState() {
        if (adapter?.items?.isEmpty() == true) {
            setNoItemsVisibility(View.VISIBLE)
        } else {
            setNoItemsVisibility(View.GONE)
        }
    }

    private fun setNoItemsVisibility(visibility: Int) {
        noItemsEmptyText.visibility = visibility
    }

    private fun selectDate() {
        val builder = MaterialDatePicker.Builder.datePicker()
        builder.build().also {
            it.addOnPositiveButtonClickListener { unixTime ->
                selectedDate = Date(unixTime)
                fetchEntries()
                updateTitle()
            }
            it.show(childFragmentManager, "picker")
        }
    }
}
