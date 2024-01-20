package com.example.stopwatchforcompetitions.data.converters

import com.example.stopwatchforcompetitions.data.db.entity.RaceEntity
import com.example.stopwatchforcompetitions.domain.model.Race

class RaceDbConvertor {
    fun map(race: Race): RaceEntity {
        return RaceEntity(
            startTime = race.startTime,
            name = race.name,
            description = race.description,
            imgUrl = race.imgUrl,
            lapDistance = race.lapDistance,
            athletes = race.athletes.joinToString(","),
            isStarted = race.isStarted
        )
    }

    fun map(race: RaceEntity): Race {
        return Race(
            startTime = race.startTime,
            name = race.name,
            description = race.description,
            imgUrl = race.imgUrl,
            lapDistance = race.lapDistance,
            athletes = mapStringToList(race.athletes),
            isStarted = race.isStarted
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