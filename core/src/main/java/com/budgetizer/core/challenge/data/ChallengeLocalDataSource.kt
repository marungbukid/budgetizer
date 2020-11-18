package com.budgetizer.core.challenge.data

import com.budgetizer.core.data.CoroutinesDispatcherProvider
import com.budgetizer.core.data.Result
import com.budgetizer.core.data.challenge.ChallengeDao
import com.budgetizer.core.data.challenge.model.Challenge
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ChallengeLocalDataSource @Inject constructor(
    private val challengeDao: ChallengeDao,
    private val dispatcherProvider: CoroutinesDispatcherProvider
) {

    suspend fun getChallenges(): Result<List<Challenge>> = withContext(dispatcherProvider.io) {
        return@withContext try {
            Result.Success(challengeDao.getChallenges())
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun addChallenge(challenge: Challenge) = withContext(dispatcherProvider.io) {
        challengeDao.insertChallenge(challenge)
    }

    suspend fun updateChallenge(challenge: Challenge) = withContext(dispatcherProvider.io) {
        challengeDao.updateChallenge(challenge)
    }

    companion object {
        const val CHALLENGE_PREFS = "CHALLENGE_PREFS"
        const val KEY_SHOULD_INTRO = "KEY_SHOULD_INTRO"
        const val KEY_INCREMENTS = "KEY_INCREMENTS"
    }
}