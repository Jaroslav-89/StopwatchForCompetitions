package com.jaroapps.stopwatchforcompetitions.data.converters

import com.jaroapps.stopwatchforcompetitions.data.db.entity.AthleteEntity
import com.jaroapps.stopwatchforcompetitions.data.db.entity.FastResultHistoryEntity
import com.jaroapps.stopwatchforcompetitions.domain.model.Athlete
import com.jaroapps.stopwatchforcompetitions.domain.model.FastResultAthleteHistory

object AthleteDbConvertor {
    fun map(athlete: Athlete): AthleteEntity {
        return AthleteEntity(
            id = athlete.id,
            race = athlete.race,
            number = athlete.number,
            lapsTime = athlete.lapsTime.joinToString(","),
            addLastResult = athlete.addLastResult
        )
    }

    fun map(athlete: AthleteEntity): Athlete {
        return Athlete(
            id = athlete.id,
            race = athlete.race,
            number = athlete.number,
            lapsTime = mapStringToListLong(athlete.lapsTime),
            addLastResult = athlete.addLastResult,
            isExpandable = false
        )
    }

    fun map(athlete: FastResultAthleteHistory): FastResultHistoryEntity {
        return FastResultHistoryEntity(
            id = athlete.id,
            race = athlete.race,
            number = athlete.number,
            currentLapNumber = athlete.currentLapNumber,
            currentLapTime = athlete.currentLapTime,
            addLastResult = athlete.addLastResult,
        )
    }

    fun map(athlete: FastResultHistoryEntity): FastResultAthleteHistory {
        return FastResultAthleteHistory(
            id = athlete.id,
            race = athlete.race,
            number = athlete.number,
            currentLapNumber = athlete.currentLapNumber,
            currentLapTime = athlete.currentLapTime,
            addLastResult = athlete.addLastResult,
        )
    }

    fun mapAthleteToFastResult(athlete: Athlete): FastResultAthleteHistory {
        return FastResultAthleteHistory(
            id = athlete.id,
            race = athlete.race,
            number = athlete.number,
            currentLapNumber = athlete.lapsTime.size,
            currentLapTime = getCurrentLapTime(athlete),
            addLastResult = athlete.addLastResult,
        )
    }

    fun mapList(athletes: List<AthleteEntity>): List<Athlete> {
        val result = mutableListOf<Athlete>()
        for (athlete in athletes) {
            result.add(map(athlete))
        }
        return result.toList()
    }

    private fun getCurrentLapTime(athlete: Athlete): Long {
        return if (athlete.lapsTime.size > 2) {
            athlete.lapsTime[athlete.lapsTime.size - 1] - athlete.lapsTime[athlete.lapsTime.size - 2]
        } else {
            athlete.lapsTime[athlete.lapsTime.size - 1] - athlete.race
        }
    }

    private fun mapStringToListLong(lapsTime: String): List<Long> {
        return if (lapsTime.isEmpty()) {
            emptyList()
        } else {
            lapsTime.split(",").map { it.toLong() }
        }
    }
}