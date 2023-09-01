package com.soyvictorherrera.bdates.modules.circles.data.datasource.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "circles")
data class CircleEntity(
    @PrimaryKey val id: String,
    @ColumnInfo("name") val name: String,
    @ColumnInfo("description") val description: String?,
    @ColumnInfo("local_only") val isLocalOnly: Boolean,
    @ColumnInfo("update_date") val updateDate: Long?,
)
