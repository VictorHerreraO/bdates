package com.soyvictorherrera.bdates.modules.eventList.data.datasource.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "events")
data class EventEntity(
    @PrimaryKey val id: String,
    @ColumnInfo("circle_id") val circleId: String,
    @ColumnInfo("name") val name: String,
    @ColumnInfo("day_of_month") val dayOfMonth: Int,
    @ColumnInfo("month_of_year") val monthOfYear: Int,
    @ColumnInfo("year") val year: Int?,
)
