package com.budgetizer.ui.challenge

import android.app.Activity
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.budgetizer.databinding.ItemChallengeContainerBinding
import com.budgetizer.ui.challenge.items.ChallengeItemsAdapter
import java.util.Locale

class ChallengeViewHolder(
    private val binding: ItemChallengeContainerBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(host: Activity, item: ChallengeHolder) {
        val adapter = ChallengeItemsAdapter(host)

        binding.itemHeader.header.text = item.type.name.capitalize(Locale.getDefault())

        binding.recyclerView.adapter = adapter
        adapter.items = item.items
    }
}