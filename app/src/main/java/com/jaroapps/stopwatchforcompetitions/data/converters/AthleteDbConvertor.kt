package com.jaroapps.stopwatchforcompetitions.data.converters

import com.jaroapps.stopwatchforcompetitions.data.db.entity.AthleteEntity
import com.jaroapps.stopwatchforcompetitions.domain.model.Athlete

class AthleteDbConvertor {
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

    fun mapList(athletes: List<AthleteEntity>): List<Athlete> {
        val result = mutableListOf<Athlete>()
        for (athlete in athletes) {
            result.add(map(athlete))
        }
        return result.toList()
    }

    fun mapStringToListLong(lapsTime: String): List<Long> {
        return if (lapsTime.isEmpty()) {
            emptyList()
        } else {
            lapsTime.split(",").map { it.toLong() }
        }
    }
}