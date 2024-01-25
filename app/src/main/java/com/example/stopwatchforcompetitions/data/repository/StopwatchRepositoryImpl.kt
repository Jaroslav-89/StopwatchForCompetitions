package com.example.stopwatchforcompetitions.data.repository

import com.example.stopwatchforcompetitions.data.converters.AthleteDbConvertor
import com.example.stopwatchforcompetitions.data.converters.RaceDbConvertor
import com.example.stopwatchforcompetitions.data.db.AppDataBase
import com.example.stopwatchforcompetitions.data.db.entity.AthleteEntity
import com.example.stopwatchforcompetitions.data.local_storage.SaveResultXls
import com.example.stopwatchforcompetitions.domain.api.StopwatchRepository
import com.example.stopwatchforcompetitions.domain.model.Athlete
import com.example.stopwatchforcompetitions.domain.model.Race
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class StopwatchRepositoryImpl(
    private val dataBase: AppDataBase,
    private val raceDbConvertor: RaceDbConvertor,
    private val athleteDbConvertor: AthleteDbConvertor,
    private val saveResultXls: SaveResultXls
) : StopwatchRepository {

    override suspend fun checkRaceIsStarted(): Race? {
        val raceEntity = dataBase.raceDao().checkRaceOnStarted(true)
        return if (raceEntity != null)
            raceDbConvertor.map(raceEntity)
        else
            null
    }

    override suspend fun getRaceInformation(startData: Long): Race {
        return raceDbConvertor.map(dataBase.raceDao().getRaceInformation(startData))
    }

    override suspend fun updateRace(race: Race) {
        val raceEntity = raceDbConvertor.map(race)
        dataBase.raceDao().insertRace(raceEntity)
    }

    override suspend fun stopRace() {
        val race = dataBase.raceDao().checkRaceOnStarted(true)
        race?.copy(isStarted = false)?.let { dataBase.raceDao().insertRace(it) }
    }

    override suspend fun deleteDaceAndAthletes(startData: Long) {
        dataBase.raceDao().deleteRaceByData(startData)
        dataBase.athleteDao().deleteAllAthletesInRace(startData)
    }

    override suspend fun addAthleteResult(newAthlete: Athlete) {
        val addTime = System.currentTimeMillis()
        dataBase.athleteDao().insertAthlete(
            athleteDbConvertor.map(
                newAthlete.copy(
                    addLastResult = addTime
                )
            )
        )
    }

    override fun getAllAthletesInRace(race: Long): Flow<List<Athlete>> {
        return dataBase.athleteDao().getAllAthletesInRace(race)
            .map { value: List<AthleteEntity> -> value.map { athleteDbConvertor.map(it) } }
    }

    override fun getAllRaces(): Flow<List<Race>> = flow {
        val raceEntityList = dataBase.raceDao().getAllRaces()
        if (raceEntityList.isNotEmpty()) {
            emit(raceEntityList.map { raceDbConvertor.map(it) })
        } else {
            emit(emptyList())
        }
    }

    override suspend fun getLastRace(): Race {
        return raceDbConvertor.map(dataBase.raceDao().getLastRace())
    }

    override suspend fun saveResultInXls(race: Long) {
        val raceEntity = dataBase.raceDao().getRaceInformation(race)
        dataBase.athleteDao().getAllAthletesInRace(race).collect() {
            val athletesEntity = it
            saveResultXls.saveRaceInXls(raceEntity, athletesEntity)
        }
    }
}