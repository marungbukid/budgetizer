package com.budgetizer.challenge.dagger

import com.budgetizer.challenge.ui.ChallengeActivity
import com.budgetizer.challenge.ui.fiftytwo.FiftyTwoWeekFragment
import com.budgetizer.core.dagger.CoreComponent
import com.budgetizer.core.dagger.SharedPreferencesModule
import com.budgetizer.core.dagger.challenge.ChallengeDataModule
import com.budgetizer.core.dagger.entry.EntryDataModule
import com.budgetizer.core.dagger.scope.FeatureScope
import com.budgetizer.core.data.challenge.model.Challenge
import dagger.BindsInstance
import dagger.Component

@Component(
    modules = [ChallengeModule::class, SharedPreferencesModule::class],
    dependencies = [CoreComponent::class]
)
@FeatureScope
interface ChallengeComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun challengeInstance(challenge: Challenge): Builder
        fun coreComponent(module: CoreComponent): Builder
        fun entryDataModule(module: EntryDataModule): Builder
        fun challengeDataModule(module: ChallengeDataModule): Builder
        fun challengeModule(module: ChallengeModule): Builder
        fun sharedPreferencesModule(module: SharedPreferencesModule): Builder
        fun build(): ChallengeComponent
    }

    fun inject(into: ChallengeActivity)
}