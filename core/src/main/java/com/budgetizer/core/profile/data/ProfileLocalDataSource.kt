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

package com.budgetizer.core.profile.data

import android.content.SharedPreferences
import androidx.core.content.edit
import com.budgetizer.core.profile.data.model.Profile
import javax.inject.Inject

class ProfileLocalDataSource @Inject constructor(
    private val prefs: SharedPreferences
) {

    var profile: Profile?
        get() {
            val givenName = prefs.getString(KEY_GIVEN_NAME, null)
            val familyName = prefs.getString(KEY_FAMILY_NAME, null)
            val grossMonthlyIncome = prefs.getFloat(KEY_GROSS_MONTHLY_INCOME, 0f).toDouble()

            if (givenName == null || familyName == null) {
                return null
            }
            return Profile(
                givenName = givenName,
                familyName = familyName,
                grossMonthlyIncome = grossMonthlyIncome
            )
        }
        set(value) {
            if (value != null) {
                prefs.edit {
                    putString(KEY_GIVEN_NAME, value.givenName)
                    putString(KEY_FAMILY_NAME, value.familyName)
                    putFloat(KEY_GROSS_MONTHLY_INCOME, value.grossMonthlyIncome.toFloat())
                }
            }
        }

    companion object {
        const val PROFILE_PREFS = "PROFILE_PREFS"
        private const val KEY_GIVEN_NAME = "KEY_GIVEN_NAME"
        private const val KEY_FAMILY_NAME = "KEY_FAMILY_NAME"
        private const val KEY_GROSS_MONTHLY_INCOME = "KEY_GROSS_MONTHLY_INCOME"
    }
}
