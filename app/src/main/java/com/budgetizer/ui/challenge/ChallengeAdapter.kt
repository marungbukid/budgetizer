package com.budgetizer.ui.challenge

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.budgetizer.databinding.ItemChallengeContainerBinding

class ChallengeAdapter(
    private val host: Activity
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val layoutInflater = LayoutInflater.from(host)

    var items = emptyList<ChallengeHolder>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return createChallengeContainerViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ChallengeViewHolder).bind(host, items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    private fun createChallengeContainerViewHolder(parent: ViewGroup): ChallengeViewHolder {
        return ChallengeViewHolder(
            ItemChallengeContainerBinding.inflate(
                layoutInflater,
                parent,
                false
            )
        )
    }
}