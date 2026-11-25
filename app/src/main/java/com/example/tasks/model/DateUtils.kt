package com.example.tasks.model

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

private val isoFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
private val shortFormat = SimpleDateFormat("d MMM", Locale.getDefault())
private val weekdayFormat = SimpleDateFormat("EEE", Locale.getDefault())

fun todayIso(): String = isoFormat.format(Date())

fun isoToMillis(iso: String): Long =
    try {
        isoFormat.parse(iso)?.time ?: System.currentTimeMillis()
    } catch (_: java.text.ParseException) {
        System.currentTimeMillis()
    }

fun millisToIso(millis: Long): String = isoFormat.format(Date(millis))

fun shiftDate(iso: String, days: Int): String {
    val calendar = Calendar.getInstance()
    calendar.time = Date(isoToMillis(iso))
    calendar.add(Calendar.DAY_OF_YEAR, days)
    return isoFormat.format(calendar.time)
}

fun formatShortDate(iso: String): String = shortFormat.format(Date(isoToMillis(iso)))

fun formatWeekday(iso: String): String = weekdayFormat.format(Date(isoToMillis(iso)))

fun formatTime(hour: Int, minute: Int): String =
    String.format(Locale.getDefault(), "%02d:%02d", hour, minute)
