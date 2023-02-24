package com.soyvictorherrera.bdates.core.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import com.soyvictorherrera.bdates.modules.circles.data.datasource.local.CircleDao
import com.soyvictorherrera.bdates.modules.circles.data.datasource.local.CircleEntity
import com.soyvictorherrera.bdates.modules.eventList.data.datasource.local.EventDao
import com.soyvictorherrera.bdates.modules.eventList.data.datasource.local.EventEntity

const val APP_DATABASE_NAME = "app_database.db"

@Database(
    version = 1,
    entities = [
        CircleEntity::class,
        EventEntity::class,
    ],
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun circleDao(): CircleDao
    abstract fun eventDao(): EventDao
}
