package com.example.stopwatchforcompetitions.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.stopwatchforcompetitions.data.db.dao.AthleteDao
import com.example.stopwatchforcompetitions.data.db.dao.RaceDao
import com.example.stopwatchforcompetitions.data.db.entity.AthleteEntity
import com.example.stopwatchforcompetitions.data.db.entity.RaceEntity

@Database(version = 4, entities = [RaceEntity::class, AthleteEntity::class])
abstract class AppDataBase : RoomDatabase() {

    abstract fun raceDao(): RaceDao

    abstract fun athleteDao(): AthleteDao

    companion object {
        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE 'race_table' ADD COLUMN 'isFavorite' INTEGER NOT NULL DEFAULT 0")
            }
        }

        val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE 'race_table' ADD COLUMN 'totalLapsInRace' INTEGER NOT NULL DEFAULT 0")
            }
        }
    }
}