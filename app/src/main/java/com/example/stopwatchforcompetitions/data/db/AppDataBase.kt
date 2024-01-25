package com.example.stopwatchforcompetitions.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.stopwatchforcompetitions.data.db.dao.AthleteDao
import com.example.stopwatchforcompetitions.data.db.dao.RaceDao
import com.example.stopwatchforcompetitions.data.db.entity.AthleteEntity
import com.example.stopwatchforcompetitions.data.db.entity.RaceEntity

@Database(version = 2, entities = [RaceEntity::class, AthleteEntity::class], exportSchema = false)
abstract class AppDataBase : RoomDatabase() {

    abstract fun raceDao(): RaceDao

    abstract fun athleteDao(): AthleteDao
}