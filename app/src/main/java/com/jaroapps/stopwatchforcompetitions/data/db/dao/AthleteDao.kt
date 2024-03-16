package com.jaroapps.stopwatchforcompetitions.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.jaroapps.stopwatchforcompetitions.data.db.entity.AthleteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AthleteDao {
    @Upsert(entity = AthleteEntity::class)
    suspend fun insertAthlete(athlete: AthleteEntity)

    @Query("SELECT * FROM athlete_table WHERE race = :race ORDER BY addLastResult DESC")
    fun getAllAthletesInRace(race: Long): Flow<List<AthleteEntity>>

    @Query("DELETE FROM athlete_table WHERE race = :race")
    fun deleteAllAthletesInRace(race: Long)
}