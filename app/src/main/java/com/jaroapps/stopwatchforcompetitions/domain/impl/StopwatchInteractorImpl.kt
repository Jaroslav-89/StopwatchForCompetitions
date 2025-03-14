package com.jaroapps.stopwatchforcompetitions.domain.impl

import android.net.Uri
import com.jaroapps.stopwatchforcompetitions.domain.api.StopwatchInteractor
import com.jaroapps.stopwatchforcompetitions.domain.api.StopwatchRepository
import com.jaroapps.stopwatchforcompetitions.domain.model.Athlete
import com.jaroapps.stopwatchforcompetitions.domain.model.FastResultAthleteHistory
import com.jaroapps.stopwatchforcompetitions.domain.model.Race
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

    override fun getAllFastResultInRace(): Flow<List<FastResultAthleteHistory>> {
        return repository.getAllFastResultInRace()
    }

    override fun getAllRaces(): Flow<List<Race>> {
        return repository.getAllRaces()
    }

    override suspend fun getLastRace(): Race {
        return repository.getLastRace()
    }

    override suspend fun saveResultInXls(race: Long, uri: Uri) {
        repository.saveResultInXls(race, uri)
    }

    override suspend fun changeAthleteNumber(athleteForChange: Athlete, newAthlete: Athlete, race: Race) {
        repository.changeAthleteNumber(athleteForChange, newAthlete, race)
    }
}