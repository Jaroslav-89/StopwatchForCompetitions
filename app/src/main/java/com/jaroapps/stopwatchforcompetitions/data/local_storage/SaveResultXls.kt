package com.jaroapps.stopwatchforcompetitions.data.local_storage

import android.net.Uri
import com.jaroapps.stopwatchforcompetitions.data.db.entity.AthleteEntity
import com.jaroapps.stopwatchforcompetitions.data.db.entity.RaceEntity

interface SaveResultXls {
    fun saveRaceInXls(
        race: RaceEntity,
        athletesEntity: List<AthleteEntity>,
        uri: Uri,
    )
}