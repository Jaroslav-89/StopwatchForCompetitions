package com.jaroapps.stopwatchforcompetitions.domain.api

import android.net.Uri
import com.jaroapps.stopwatchforcompetitions.domain.model.Athlete
import com.jaroapps.stopwatchforcompetitions.domain.model.Race
import kotlinx.coroutines.flow.Flow

interface StopwatchRepository {
    suspend fun checkRaceIsStarted(): Race?
    suspend fun getRaceInformation(startData: Long): Race
    suspend fun updateRace(race: Race)
    suspend fun stopRace()
    suspend fun deleteDaceAndAthletes(startData: Long)
    suspend fun addAthleteResult(newAthlete: Athlete)
    fun getAllAthletesInRace(race: Long): Flow<List<Athlete>>
    fun getAllRaces(): Flow<List<Race>>
    suspend fun getLastRace(): Race
    suspend fun saveResultInXls(race: Long, uri: Uri)
}