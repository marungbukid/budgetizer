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

package com.budgetizer.ui.stats

import com.marungbukid.charts.line.LineChartEntry

data class StatsChartEntry(
    val index: Long,
    val value: Double,
    val timestamp: Long,
    val label: String
) : LineChartEntry() {
    override fun getIndex(): Int {
        return index.toInt()
    }

    override fun getValue(): Float {
        return value.toFloat()
    }

    override fun getDateTime(): Long {
        return timestamp
    }
}
