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
import android.view.View
import android.view.ViewStub
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.budgetizer.R
import com.budgetizer.databinding.ActivityHomeBinding
import com.budgetizer.ui.entry.EntriesAdapter

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var entriesAdapter: EntriesAdapter

    private val noItemsEmptyText by lazy {
        val view = findViewById<ViewStub>(R.id.stub_no_items).inflate() as TextView
        // create the no filters empty text

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

        with(view) {
            text = ssb
            setOnClickListener {
                TODO()
            }
            view
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        entriesAdapter = EntriesAdapter(this)

        setSupportActionBar(binding.toolbar)

        checkEmptyState()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        setupToolbar()
        return true
    }

    private fun setupToolbar() {
        binding.toolbar.inflateMenu(R.menu.home_menu)

        supportActionBar?.apply {
            setDisplayShowTitleEnabled(false)
        }
    }

    private fun checkEmptyState() {
        if (entriesAdapter.items.isEmpty()) {
            setNoItemsVisibility(View.VISIBLE)
        } else {
            setNoItemsVisibility(View.GONE)
        }
    }

    private fun setNoItemsVisibility(visibility: Int) {
        noItemsEmptyText.visibility = visibility
    }
}

fun Annotation.getResId(context: Context): Int {
    return context.resources.getIdentifier(value, null, context.packageName)
}
