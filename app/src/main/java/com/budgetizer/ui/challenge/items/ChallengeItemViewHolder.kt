package com.budgetizer.ui.challenge.items

import androidx.recyclerview.widget.RecyclerView
import com.budgetizer.core.data.challenge.model.Challenge
import com.budgetizer.databinding.ItemChallengeBinding

class ChallengeItemViewHolder(
    private val binding: ItemChallengeBinding,
    private val onClick: (challenge: Challenge) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(challenge: Challenge) {
        binding.label.text = challenge.label
        binding.thumb.setImageResource(challenge.thumbRes)

        binding.container.setOnClickListener {
            onClick.invoke(challenge)
        }
    }
}