package com.budgetizer.core.data.challenge

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.budgetizer.core.data.challenge.model.Challenge

@Dao
interface ChallengeDao {

    @Query("SELECT * FROM challenge")
    suspend fun getChallenges(): List<Challenge>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChallenge(challenge: Challenge)

    @Update
    suspend fun updateChallenge(challenge: Challenge)
}