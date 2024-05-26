package com.jaroapps.stopwatchforcompetitions.data.converters

import com.jaroapps.stopwatchforcompetitions.data.db.entity.RaceEntity
import com.jaroapps.stopwatchforcompetitions.domain.model.Race

object RaceDbConvertor {
    fun map(race: Race): RaceEntity {
        return RaceEntity(
            startTime = race.startTime,
            name = race.name,
            description = race.description,
            imgUrl = race.imgUrl,
            lapDistance = race.lapDistance,
            totalLapsInRace = race.totalLapsInRace,
            athletes = race.athletes.joinToString(","),
            isStarted = race.isStarted,
            isFavorite = race.isFavorite
        )
    }

    fun map(race: RaceEntity): Race {
        return Race(
            startTime = race.startTime,
            name = race.name,
            description = race.description,
            imgUrl = race.imgUrl,
            lapDistance = race.lapDistance,
            totalLapsInRace = race.totalLapsInRace,
            athletes = mapStringToList(race.athletes),
            isStarted = race.isStarted,
            isFavorite = race.isFavorite
        )
    }

    private fun mapStringToList(athletes: String): List<String> {
        return if (athletes.isEmpty()) {
            emptyList()
        } else {
            athletes.split(",")
        }
    }
}