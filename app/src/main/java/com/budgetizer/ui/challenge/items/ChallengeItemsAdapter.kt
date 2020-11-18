package com.budgetizer.ui.challenge.items

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.budgetizer.core.data.challenge.model.Challenge
import com.budgetizer.core.util.Activities
import com.budgetizer.core.util.intentTo
import com.budgetizer.databinding.ItemChallengeBinding

class ChallengeItemsAdapter(
    private val host: Activity
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val layoutInflater = LayoutInflater.from(host)

    var items = emptyList<Challenge>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return createChallengeItemViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ChallengeItemViewHolder).bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    private fun createChallengeItemViewHolder(parent: ViewGroup): ChallengeItemViewHolder {
        return ChallengeItemViewHolder(
            ItemChallengeBinding.inflate(
                layoutInflater,
                parent,
                false
            )
        ) { challenge ->
            intentTo(Activities.Challenge).also {
                it.putExtra(Activities.Challenge.EXTRA_CHALLENGE, challenge)
                host.startActivity(it)
            }
        }
    }
}