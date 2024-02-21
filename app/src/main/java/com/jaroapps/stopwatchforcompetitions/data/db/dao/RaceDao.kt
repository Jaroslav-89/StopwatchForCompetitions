package com.jaroapps.stopwatchforcompetitions.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.jaroapps.stopwatchforcompetitions.data.db.entity.RaceEntity

@Dao
interface RaceDao {
    @Upsert(entity = RaceEntity::class)
    suspend fun insertRace(race: RaceEntity)

    @Query("SELECT * FROM race_table WHERE isStarted = :isStarted LIMIT 1")
    suspend fun checkRaceOnStarted(isStarted: Boolean): RaceEntity?

    @Query("SELECT * FROM race_table WHERE startTime = :startData LIMIT 1")
    suspend fun getRaceInformation(startData: Long): RaceEntity

    @Query("SELECT * FROM race_table ORDER BY startTime DESC")
    suspend fun getAllRaces(): List<RaceEntity>

    @Query("SELECT * FROM race_table ORDER BY startTime DESC LIMIT 1")
    suspend fun getLastRace(): RaceEntity

    @Query("DELETE FROM race_table WHERE startTime = :startData")
    suspend fun deleteRaceByData(startData: Long)
}