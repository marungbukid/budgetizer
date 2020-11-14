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

package com.budgetizer.profile.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.budgetizer.core.profile.data.model.Profile
import com.budgetizer.profile.dagger.inject
import com.budgetizer.profile.databinding.ActivityProfileBinding
import javax.inject.Inject

class ProfileActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModel: ProfileViewModel

    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        inject(this)

        initViewModelObservers()
        initBindingEvents()
    }

    private fun initViewModelObservers() {
        viewModel.state.observe(this, { _state ->
            val state = _state ?: return@observe

            when (state) {
                is ProfileEvents.OnProfileFetched -> updateProfile(state.profile)
            }
        })
    }

    private fun initBindingEvents() {
        binding.save.setOnClickListener {
            viewModel.saveProfile(
                Profile(
                    givenName = binding.givenName.text.toString(),
                    familyName = binding.familyName.text.toString(),
                    grossMonthlyIncome = binding.grossMonthlyIncome.text.toString().toDouble()
                )
            )

            finish()
        }
    }

    private fun updateProfile(profile: Profile) {
        binding.givenName.setText(profile.givenName)
        binding.familyName.setText(profile.familyName)
        binding.grossMonthlyIncome.setText(profile.grossMonthlyIncome.toString())
    }
}
