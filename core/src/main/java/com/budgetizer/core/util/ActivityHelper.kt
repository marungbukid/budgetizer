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

@file:JvmName("ActivityHelper")

package com.budgetizer.core.util

import android.content.Intent

private const val PACKAGE_NAME = "com.budgetizer"

fun intentTo(addressableActivity: AddressableActivity): Intent {
    return Intent(Intent.ACTION_VIEW).setClassName(
        PACKAGE_NAME,
        addressableActivity.className
    )
}

interface AddressableActivity {
    /**
     * The activity class name.
     */
    val className: String
}

object Activities {

    object Challenge : AddressableActivity {
        override val className = "$PACKAGE_NAME.challenge.ui.ChallengeActivity"
    }

    object Profile : AddressableActivity {
        override val className = "$PACKAGE_NAME.profile.ui.ProfileActivity"
    }

    object Entry : AddressableActivity {
        const val EXTRA_UPDATE_ENTRY = "EXTRA_UPDATE_ENTRY"
        const val EXTRA_DATE_ENTRY = "EXTRA_DATE_ENTRY"

        override val className = "$PACKAGE_NAME.entry.ui.EntryActivity"
    }
}
