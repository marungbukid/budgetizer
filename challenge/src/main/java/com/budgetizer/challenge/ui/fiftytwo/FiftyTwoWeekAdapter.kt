package com.budgetizer.challenge.ui.fiftytwo

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.budgetizer.challenge.databinding.ItemFiftytwoWeekBinding
import com.budgetizer.core.data.entry.model.Entry
import com.budgetizer.core.data.entry.model.EntryType

class FiftyTwoWeekAdapter(
    host: Activity
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val layoutInflater = LayoutInflater.from(host)

    var showSavings = false
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var items: List<Entry> = emptyList()
        get() = field.filter {
            showSavings ==
                (showSavings && (it.type != EntryType.SAVINGS))
        }
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var onItemClicked: ((entry: Entry) -> Unit)? = null

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return createFiftyTwoWkViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as FiftyTwoWeekViewHolder).bind(items[position], position == 0)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemId(position: Int): Long {
        return items[position].id
    }

    private fun createFiftyTwoWkViewHolder(parent: ViewGroup): FiftyTwoWeekViewHolder {
        return FiftyTwoWeekViewHolder(
            ItemFiftytwoWeekBinding.inflate(
                layoutInflater,
                parent,
                false
            )
        ) {
            onItemClicked?.invoke(it)
        }
    }
}