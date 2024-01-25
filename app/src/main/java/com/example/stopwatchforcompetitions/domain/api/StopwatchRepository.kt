package com.example.stopwatchforcompetitions.domain.api

import com.example.stopwatchforcompetitions.domain.model.Athlete
import com.example.stopwatchforcompetitions.domain.model.Race
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
}