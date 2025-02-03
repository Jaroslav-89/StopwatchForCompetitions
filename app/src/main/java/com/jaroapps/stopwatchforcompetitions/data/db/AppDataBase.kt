package com.jaroapps.stopwatchforcompetitions.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.jaroapps.stopwatchforcompetitions.data.db.dao.AthleteDao
import com.jaroapps.stopwatchforcompetitions.data.db.dao.FastResultHistoryDao
import com.jaroapps.stopwatchforcompetitions.data.db.dao.RaceDao
import com.jaroapps.stopwatchforcompetitions.data.db.entity.AthleteEntity
import com.jaroapps.stopwatchforcompetitions.data.db.entity.FastResultHistoryEntity
import com.jaroapps.stopwatchforcompetitions.data.db.entity.RaceEntity

@Database(
    version = 5,
    entities = [
        RaceEntity::class,
        AthleteEntity::class,
        FastResultHistoryEntity::class
    ],
    exportSchema = false
)
    abstract class AppDataBase : RoomDatabase() {

        abstract fun raceDao(): RaceDao

    abstract fun athleteDao(): AthleteDao

    abstract fun fastResultHistoryDao(): FastResultHistoryDao

    companion object {
        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE 'race_table' ADD COLUMN 'isFavorite' INTEGER NOT NULL DEFAULT 0")
            }
        }

        val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE 'race_table' ADD COLUMN 'totalLapsInRace' INTEGER NOT NULL DEFAULT 0")
            }
        }

        val MIGRATION_4_5 = object : Migration(4, 5) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
            CREATE TABLE IF NOT EXISTS `fast_result_history_table` (
                `id` INTEGER NOT NULL, 
                `race` INTEGER NOT NULL, 
                `number` TEXT NOT NULL, 
             `currentLapNumber` INTEGER NOT NULL, 
                `currentLapTime` INTEGER NOT NULL, 
                `addLastResult` INTEGER NOT NULL, 
                PRIMARY KEY(`addLastResult`)
            )
        """
                )
            }
        }
    }
}