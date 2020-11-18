package com.budgetizer.core.dagger.challenge

import androidx.fragment.app.FragmentActivity
import com.budgetizer.core.challenge.data.ChallengeLocalDataSource
import com.budgetizer.core.challenge.data.ChallengeRepository
import com.budgetizer.core.dagger.scope.FeatureScope
import com.budgetizer.core.data.AppDatabase
import com.budgetizer.core.data.challenge.ChallengeDao
import dagger.Module
import dagger.Provides

@Module
class ChallengeDataModule(private val activity: FragmentActivity) {

    @Provides
    @FeatureScope
    fun provideChallengeRepository(
        localDataSource: ChallengeLocalDataSource
    ): ChallengeRepository =
        ChallengeRepository.getInstance(localDataSource)

    @Provides
    @FeatureScope
    fun provideChallengeDao(): ChallengeDao =
        AppDatabase.getInstance(activity).challengeDao()
}