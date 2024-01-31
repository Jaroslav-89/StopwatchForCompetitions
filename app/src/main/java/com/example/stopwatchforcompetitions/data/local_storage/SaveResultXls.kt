package com.example.stopwatchforcompetitions.data.local_storage

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import com.example.stopwatchforcompetitions.data.converters.AthleteDbConvertor
import com.example.stopwatchforcompetitions.data.db.entity.AthleteEntity
import com.example.stopwatchforcompetitions.data.db.entity.RaceEntity
import com.example.stopwatchforcompetitions.domain.model.Athlete
import com.example.stopwatchforcompetitions.util.Util
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileOutputStream

class SaveResultXls(
    private val context: Context,
    private val athleteDbConvertor: AthleteDbConvertor
) {

    fun saveRaceInXls(
        race: RaceEntity,
        athletesEntity: List<AthleteEntity>
    ) {
        try {
            val athletes = athleteDbConvertor.mapList(athletesEntity)
            val athletesSortedByPosition =
                athletes
                    .sortedBy { it.addLastResult }
                    .sortedByDescending { it.lapsTime.size }
            val maxLaps = getMaxLapsInRace(athletes)

            val wb = XSSFWorkbook()
            val sheet = wb.createSheet("Sheet1")

            var row = sheet.createRow(0)
            var cell = row.createCell(0)
            cell.setCellValue("Дата старта:")
            cell = row.createCell(1)
            cell.setCellValue(Util.convertLongToDate(race.startTime))

            row = sheet.createRow(1)
            cell = row.createCell(0)
            cell.setCellValue("Время старта:")
            cell = row.createCell(1)
            cell.setCellValue(Util.convertLongToTime(race.startTime))

            row = sheet.createRow(2)
            cell = row.createCell(0)
            cell.setCellValue("Название:")
            cell = row.createCell(1)
            cell.setCellValue(race.name)

            row = sheet.createRow(3)
            cell = row.createCell(0)
            cell.setCellValue("Дистанция:")
            cell = row.createCell(1)
            cell.setCellValue("${race.lapDistance}")

            row = sheet.createRow(4)
            cell = row.createCell(0)
            cell.setCellValue("Участников:")
            cell = row.createCell(1)
            cell.setCellValue("${athletes.size}")

            row = sheet.createRow(5)
            cell = row.createCell(0)
            cell.setCellValue("Описание:")
            cell = row.createCell(1)
            cell.setCellValue(race.description)

            row = sheet.createRow(7)
            cell = row.createCell(0)
            cell.setCellValue("Место")
            cell = row.createCell(1)
            cell.setCellValue("Номер")
            val totalTimeCell = maxLaps + 2
            cell = row.createCell(totalTimeCell)
            cell.setCellValue("Общее время")

            for (i in 1..maxLaps) {
                val lap = i + 1
                cell = row.createCell(lap)
                cell.setCellValue("Круг $i")
            }

            for (athlete in athletesSortedByPosition) {
                val lapsTime = athlete.lapsTime
                val athleteRow = athletesSortedByPosition.indexOf(athlete) + 8
                row = sheet.createRow(athleteRow)
                cell = row.createCell(0)
                val athletePosition = athletesSortedByPosition.indexOf(athlete) + 1
                cell.setCellValue("$athletePosition")
                cell = row.createCell(1)
                val athleteNumber = athlete.number
                cell.setCellValue(athleteNumber)

                for (lap in lapsTime) {
                    val currentLapTimeLong = if (lapsTime.indexOf(lap) > 0) {
                        val previousLap = lapsTime.indexOf(lap) - 1
                        lap - lapsTime[previousLap]
                    } else {
                        lap - athlete.race
                    }
                    val currentLapTime = Util.getTimeFormat(currentLapTimeLong)

                    val lapNumberCell = lapsTime.indexOf(lap) + 2
                    cell = row.createCell(lapNumberCell)
                    cell.setCellValue(currentLapTime)
                }

                val totalRaceTime = Util.getTimeFormat(athlete.addLastResult - race.startTime)
                cell = row.createCell(totalTimeCell)
                cell.setCellValue(totalRaceTime)
            }

            if (Build.VERSION.SDK_INT > 29) {
                val contentValues = ContentValues().apply {
                    put(
                        MediaStore.MediaColumns.DISPLAY_NAME,
                        "${Util.convertLongToDate(race.startTime)}:${Util.convertLongToTime(race.startTime)}.xls"
                    )
                    put(MediaStore.Downloads.MIME_TYPE, "application/vnd.ms-excel")
                    put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                }
                val dstUri =
                    context.contentResolver.insert(
                        MediaStore.Downloads.EXTERNAL_CONTENT_URI,
                        contentValues
                    )
                if (dstUri != null) {
                    val dst = context.contentResolver.openOutputStream(dstUri)
                    wb.write(dst)
                    dst?.close()
                }
            } else {
                val downloadsDirectory =
                    context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
                val file = File(
                    downloadsDirectory,
                    "${Util.convertLongToDate(race.startTime)}:${Util.convertLongToTime(race.startTime)}.xls"
                )
                file.createNewFile()

                val fileOutputStream = FileOutputStream(file)

                wb.write(fileOutputStream)
                fileOutputStream.close()

                context.openFileOutput(
                    "${Util.convertLongToDate(race.startTime)}:${
                        Util.convertLongToTime(
                            race.startTime
                        )
                    }.xls", Context.MODE_APPEND
                )
                val outputStream = FileOutputStream(file)

                wb.write(outputStream)
                outputStream.close()
            }
        } catch (e: Exception) {
            Log.d("xxx", e.toString())
        }
    }

    private fun getMaxLapsInRace(athletes: List<Athlete>): Int {
        var maxLaps = 0
        for (athlete in athletes) {
            val numberOfLaps = athlete.lapsTime.size
            if (numberOfLaps > maxLaps) maxLaps = numberOfLaps
        }
        return maxLaps
    }
}