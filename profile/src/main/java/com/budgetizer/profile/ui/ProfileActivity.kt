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
import com.budgetizer.profile.R
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
            if (!isInputsValid()) return@setOnClickListener

            viewModel.saveProfile(
                Profile(
                    givenName = binding.givenName.editText?.text.toString(),
                    familyName = binding.familyName.editText?.text.toString(),
                    grossMonthlyIncome = binding.grossMonthlyIncome.editText?.text.toString()
                        .toDouble()
                )
            )

            finish()
        }
    }

    private fun updateProfile(profile: Profile) {
        binding.greetings.text = getString(R.string.greetings_profile, profile.givenName)
        binding.message.text = getString(R.string.header_update)
        binding.givenName.editText?.setText(profile.givenName)
        binding.familyName.editText?.setText(profile.familyName)
        binding.grossMonthlyIncome.editText?.setText(profile.grossMonthlyIncome.toString())
    }

    private fun isInputsValid(): Boolean {
        if (binding.givenName.editText?.text?.isEmpty() == true) {
            binding.givenName.error = getString(com.budgetizer.core.R.string.error_required_fields)
            return false
        }
        if (binding.familyName.editText?.text?.isEmpty() == true) {
            binding.familyName.error = getString(com.budgetizer.core.R.string.error_required_fields)
            return false
        }
        if (binding.grossMonthlyIncome.editText?.text?.isEmpty() == true) {
            binding.grossMonthlyIncome.error = getString(com.budgetizer.core.R.string.error_required_fields)
            return false
        }

        return true
    }
}
