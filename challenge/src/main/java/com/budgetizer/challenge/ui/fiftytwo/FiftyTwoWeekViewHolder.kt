package com.budgetizer.challenge.ui.fiftytwo

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.budgetizer.challenge.databinding.ItemFiftytwoWeekBinding
import com.budgetizer.core.data.entry.model.Entry
import com.budgetizer.core.data.entry.model.EntryType
import com.budgetizer.core.util.toFixed
import java.util.Calendar

class FiftyTwoWeekViewHolder(
    private val binding: ItemFiftytwoWeekBinding,
    private val onClick: (entry: Entry) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(entry: Entry, isCheckable: Boolean = false) {
        val calendar = Calendar.getInstance()
        calendar.time = entry.createdAt
        calendar.add(Calendar.WEEK_OF_MONTH, 1)
        calendar.add(Calendar.DAY_OF_MONTH, -1)
        binding.amount.text = entry.amount.toFixed()
        binding.date.text = "${entry.createdAt.toFixed()} - ${calendar.time.toFixed()}"

        if (isCheckable) {
            binding.root.setOnClickListener {
                onClick.invoke(entry)
            }
        }

        if (entry.type == EntryType.SAVINGS) {
            binding.root.setCardBackgroundColor(
                ContextCompat.getColor(binding.root.context, com.budgetizer.core.R.color.primaryColor)
            )
        } else {
            binding.root.setCardBackgroundColor(
                ContextCompat.getColor(
                    binding.root.context,
                    com.budgetizer.core.R.color.color_surface
                )
            )
        }
    }
}