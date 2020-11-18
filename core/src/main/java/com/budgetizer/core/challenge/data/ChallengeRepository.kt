package com.budgetizer.core.challenge.data

import com.budgetizer.core.data.Result
import com.budgetizer.core.data.challenge.model.Challenge
import java.util.Date

class ChallengeRepository constructor(
    private val localDataSource: ChallengeLocalDataSource
) {

    suspend fun getChallenges(): Result<List<Challenge>> {
        val localEntries = localDataSource.getChallenges()
        if (localEntries is Result.Success) return localEntries

        return Result.Error(Exception("Unable to fetch challenges"))
    }

    suspend fun addChallenge(challenge: Challenge) {
        localDataSource.addChallenge(challenge)
    }

    suspend fun updateChallenge(challenge: Challenge) {
        localDataSource.updateChallenge(challenge)
    }

    companion object {
        @Volatile
        private var INSTANCE: ChallengeRepository? = null

        fun getInstance(
            localDataSource: ChallengeLocalDataSource
        ): ChallengeRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: ChallengeRepository(
                    localDataSource
                ).also { INSTANCE = it }
            }
        }
    }
}