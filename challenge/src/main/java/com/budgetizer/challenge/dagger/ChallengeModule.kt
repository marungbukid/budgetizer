package com.budgetizer.challenge.dagger

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.budgetizer.challenge.ui.ChallengeViewModel
import com.budgetizer.challenge.ui.ChallengeViewModelFactory
import com.budgetizer.challenge.ui.FragmentsViewModel
import com.budgetizer.challenge.ui.FragmentsViewModelFactory
import com.budgetizer.core.dagger.DataManagerModule
import com.budgetizer.core.dagger.challenge.ChallengeDataModule
import com.budgetizer.core.dagger.entry.EntryDataModule
import dagger.Module
import dagger.Provides

@Module(
    includes = [
        DataManagerModule::class,
        EntryDataModule::class,
        ChallengeDataModule::class
    ]
)
class ChallengeModule(private val activity: FragmentActivity) {

    @Provides
    fun provideChallengeViewModel(
        factory: ChallengeViewModelFactory
    ): ChallengeViewModel =
        ViewModelProvider(activity, factory).get(ChallengeViewModel::class.java)

    @Provides
    fun provideFragmentsViewModel(
        factory: FragmentsViewModelFactory
    ): FragmentsViewModel =
        ViewModelProvider(activity, factory).get(FragmentsViewModel::class.java)
}