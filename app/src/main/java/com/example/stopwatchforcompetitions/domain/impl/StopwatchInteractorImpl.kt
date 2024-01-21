package com.example.stopwatchforcompetitions.domain.impl

import com.example.stopwatchforcompetitions.domain.api.StopwatchInteractor
import com.example.stopwatchforcompetitions.domain.api.StopwatchRepository
import com.example.stopwatchforcompetitions.domain.model.Athlete
import com.example.stopwatchforcompetitions.domain.model.Race
import kotlinx.coroutines.flow.Flow

class StopwatchInteractorImpl(private val repository: StopwatchRepository) : StopwatchInteractor {
    override suspend fun checkRaceIsStarted(): Race? {
        return repository.checkRaceIsStarted()
    }

    override suspend fun getRaceInformation(startData: Long): Race {
        return repository.getRaceInformation(startData)
    }

    override suspend fun updateRace(race: Race) {
        repository.updateRace(race)
    }

    override suspend fun stopRace() {
        repository.stopRace()
    }

    override suspend fun deleteDaceAndAthletes(startData: Long) {
        repository.deleteDaceAndAthletes(startData)
    }

    override suspend fun addAthleteResult(newAthlete: Athlete) {
        repository.addAthleteResult(newAthlete)
    }

    override fun getAllAthletesInRace(race: Long): Flow<List<Athlete>> {
        return repository.getAllAthletesInRace(race)
    }

    override fun getAllRaces(): Flow<List<Race>> {
        return repository.getAllRaces()
    }

    override suspend fun getLastRace(): Race {
        return repository.getLastRace()
    }
}