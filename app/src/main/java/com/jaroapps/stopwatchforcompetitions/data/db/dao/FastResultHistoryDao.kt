package com.jaroapps.stopwatchforcompetitions.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import com.jaroapps.stopwatchforcompetitions.data.db.entity.FastResultHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FastResultHistoryDao {
    @Upsert(entity = FastResultHistoryEntity::class)
    suspend fun addAthleteResult(athlete: FastResultHistoryEntity)

    @Query("SELECT * FROM fast_result_history_table ORDER BY addLastResult DESC")
    fun getAllResultHistory(): Flow<List<FastResultHistoryEntity>>

    @Query("DELETE FROM fast_result_history_table")
    suspend fun deleteAllAthletesInRace()

    @Query("DELETE FROM fast_result_history_table WHERE number = :number")
    suspend fun deleteAthleteByNumber(number: String)

    @Query("SELECT * FROM fast_result_history_table WHERE number = :number")
    suspend fun getResultHistoryWithAthleteNumber(number: String): List<FastResultHistoryEntity>

    @Insert(entity = FastResultHistoryEntity::class)
    suspend fun addAllAthleteResultWithNewNumber(athleteList: List<FastResultHistoryEntity>)
}