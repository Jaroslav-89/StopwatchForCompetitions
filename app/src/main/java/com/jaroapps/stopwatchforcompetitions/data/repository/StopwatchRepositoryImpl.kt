package com.jaroapps.stopwatchforcompetitions.data.repository

import android.net.Uri
import com.jaroapps.stopwatchforcompetitions.data.converters.AthleteDbConvertor
import com.jaroapps.stopwatchforcompetitions.data.converters.RaceDbConvertor
import com.jaroapps.stopwatchforcompetitions.data.db.AppDataBase
import com.jaroapps.stopwatchforcompetitions.data.db.entity.AthleteEntity
import com.jaroapps.stopwatchforcompetitions.data.db.entity.FastResultHistoryEntity
import com.jaroapps.stopwatchforcompetitions.data.local_storage.SaveResultXls
import com.jaroapps.stopwatchforcompetitions.domain.api.StopwatchRepository
import com.jaroapps.stopwatchforcompetitions.domain.model.Athlete
import com.jaroapps.stopwatchforcompetitions.domain.model.FastResultAthleteHistory
import com.jaroapps.stopwatchforcompetitions.domain.model.Race
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class StopwatchRepositoryImpl(
    private val dataBase: AppDataBase,
    private val saveResultXls: SaveResultXls,
) : StopwatchRepository {

    override suspend fun checkRaceIsStarted(): Race? {
        val raceEntity = dataBase.raceDao().checkRaceOnStarted(true)
        return if (raceEntity != null)
            RaceDbConvertor.map(raceEntity)
        else
            null
    }

    override suspend fun getRaceInformation(startData: Long): Race {
        return RaceDbConvertor.map(dataBase.raceDao().getRaceInformation(startData))
    }

    override suspend fun updateRace(race: Race) {
        val raceEntity = RaceDbConvertor.map(race)
        dataBase.raceDao().insertRace(raceEntity)
    }

    override suspend fun stopRace() {
        val race = dataBase.raceDao().checkRaceOnStarted(true)
        race?.copy(isStarted = false)?.let { dataBase.raceDao().insertRace(it) }
        dataBase.fastResultHistoryDao().deleteAllAthletesInRace()
    }

    override suspend fun deleteDaceAndAthletes(startData: Long) {
        dataBase.raceDao().deleteRaceByData(startData)
        dataBase.athleteDao().deleteAllAthletesInRace(startData)
    }

    override suspend fun addAthleteResult(newAthlete: Athlete) {
        val addTime = System.currentTimeMillis()
        val fastResulAthlete = AthleteDbConvertor.mapAthleteToFastResult(
            newAthlete.copy(addLastResult = addTime)
        )
        dataBase.athleteDao().insertAthlete(
            AthleteDbConvertor.map(
                newAthlete.copy(
                    addLastResult = addTime
                )
            )
        )
        dataBase.fastResultHistoryDao().addAthleteResult(
            AthleteDbConvertor.map(fastResulAthlete)
        )
    }

    override fun getAllAthletesInRace(race: Long): Flow<List<Athlete>> {
        return dataBase.athleteDao().getAllAthletesInRace(race)
            .map { value: List<AthleteEntity> -> value.map { AthleteDbConvertor.map(it) } }
    }

    override fun getAllFastResultInRace(): Flow<List<FastResultAthleteHistory>> {
        return dataBase.fastResultHistoryDao().getAllResultHistory()
            .map { value: List<FastResultHistoryEntity> -> value.map { AthleteDbConvertor.map(it) } }
    }

    override fun getAllRaces(): Flow<List<Race>> = flow {
        dataBase.raceDao().getAllRaces().collect() {
            val raceEntityList = it
            if (raceEntityList.isNotEmpty()) {
                emit(raceEntityList.map { RaceDbConvertor.map(it) })
            } else {
                emit(emptyList())
            }
        }
    }

    override suspend fun getLastRace(): Race {
        return RaceDbConvertor.map(dataBase.raceDao().getLastRace())
    }

    override suspend fun saveResultInXls(race: Long, uri: Uri) {
        val raceEntity = dataBase.raceDao().getRaceInformation(race)
        dataBase.athleteDao().getAllAthletesInRace(race).collect() {
            val athletesEntity = it
            saveResultXls.saveRaceInXls(raceEntity, athletesEntity, uri)
        }
    }
}