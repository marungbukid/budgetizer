package com.budgetizer.challenge.ui.onboard

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import com.budgetizer.challenge.R
import com.budgetizer.challenge.databinding.ActivityOnboardBinding
import com.budgetizer.core.data.challenge.model.Challenge
import com.budgetizer.core.util.Activities
import com.google.android.material.appbar.AppBarLayout
import kotlin.math.abs

class OnBoardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initParcels()

        setSupportActionBar(binding.toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        setupToolbar()
        return true
    }

    private fun initParcels() {
        if (intent.hasExtra(Activities.Challenge.EXTRA_CHALLENGE)) {
            intent.getParcelableExtra<Challenge>(Activities.Challenge.EXTRA_CHALLENGE)?.let {
                updateLayout(it)
            }
        }
    }

    private fun updateLayout(challenge: Challenge) {
        binding.title.text = challenge.label
        binding.done.setOnClickListener {
            setResult(RESULT_OK)
            finish()
        }

        when (challenge.id) {
            1L -> {
                binding.explanation.setText(R.string.explanation_52_week)
            }
        }
    }

    private fun setupToolbar() {
        binding.appbar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            binding.collapsingToolbar.title =
                if (abs(verticalOffset) != appBarLayout.totalScrollRange)
                    " " else binding.title.text.toString()
        })
    }
}