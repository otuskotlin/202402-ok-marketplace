@file:Suppress("unused")

package ru.otus.otuskotlin.m1l5.dsl

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

fun AvailabilityContext.sun(time: String) = dayTimeOfWeek(DayOfWeek.SUNDAY, time)
fun AvailabilityContext.mon(time: String) = dayTimeOfWeek(DayOfWeek.MONDAY, time)
fun AvailabilityContext.tue(time: String) = dayTimeOfWeek(DayOfWeek.TUESDAY, time)
fun AvailabilityContext.wed(time: String) = dayTimeOfWeek(DayOfWeek.WEDNESDAY, time)
fun AvailabilityContext.thu(time: String) = dayTimeOfWeek(DayOfWeek.THURSDAY, time)
fun AvailabilityContext.fri(time: String) = dayTimeOfWeek(DayOfWeek.FRIDAY, time)
fun AvailabilityContext.sat(time: String) = dayTimeOfWeek(DayOfWeek.SATURDAY, time)

fun AvailabilityContext.tomorrow(time: String) {
    val dDay = LocalDate.now().plusDays(1)
    val dTime = LocalTime.parse(time)
    add(LocalDateTime.of(dDay, dTime))
}
