package com.example.stopwatchforcompetitions.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.stopwatchforcompetitions.data.db.entity.RaceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RaceDao {
    @Upsert(entity = RaceEntity::class)
    suspend fun insertRace(race: RaceEntity)

    @Delete(entity = RaceEntity::class)
    suspend fun deleteRace(race: RaceEntity)

    @Query("SELECT * FROM race_table WHERE isStarted = :isStarted LIMIT 1")
    suspend fun checkRaceOnStarted(isStarted: Boolean): RaceEntity?

    @Query("SELECT * FROM race_table WHERE startTime = :startData LIMIT 1")
    suspend fun getRaceInformation(startData: Long): RaceEntity

    @Query("SELECT * FROM race_table ORDER BY startTime DESC")
    suspend fun getAllRaces(): List<RaceEntity>

    @Query("SELECT * FROM race_table ORDER BY startTime DESC LIMIT 1")
    suspend fun getLastRace(): RaceEntity
}