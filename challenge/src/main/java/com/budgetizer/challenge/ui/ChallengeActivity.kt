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

package com.budgetizer.challenge.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.budgetizer.challenge.R
import com.budgetizer.challenge.dagger.inject
import com.budgetizer.challenge.databinding.ActivityChallengeBinding
import com.budgetizer.core.data.challenge.model.Challenge
import com.budgetizer.core.util.Activities
import com.budgetizer.core.util.intentTo
import javax.inject.Inject

class ChallengeActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModel: ChallengeViewModel

    private lateinit var binding: ActivityChallengeBinding
    private var curChallenge: Challenge? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChallengeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initParcels()
        initViewModelObservers()
        initNavigation()
    }

    private fun initParcels() {
        if (intent.hasExtra(Activities.Challenge.EXTRA_CHALLENGE)) {
            val challenge =
                intent.getParcelableExtra<Challenge>(Activities.Challenge.EXTRA_CHALLENGE)
            if (challenge != null) {
                inject(this, challenge)
            }
            curChallenge = challenge
        }
    }

    private fun initViewModelObservers() {
        viewModel.events.observe(this, { _events ->
            val events = _events ?: return@observe

            if (events is ChallengeEvents.OnBoard) {
                curChallenge = events.challenge
                intentTo(Activities.Challenge.OnBoard).also {
                    it.putExtra(Activities.Challenge.EXTRA_CHALLENGE, events.challenge)
                    startActivityForResult(it, Activities.Challenge.OnBoard.RC_ONBOARD)
                }
            }
        })
    }

    private fun initNavigation() {
        if (curChallenge != null) {
            val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
            val navController = navHostFragment.navController
            navController.navigate(curChallenge!!.navId)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            Activities.Challenge.OnBoard.RC_ONBOARD -> {
                if (resultCode == RESULT_OK) {
                    viewModel.activate()
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    override fun onBackPressed() {
        finish()
    }
}
