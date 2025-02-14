package com.soyvictorherrera.bdates.modules.eventList.data.datasource.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface EventDao {
    @Query("SELECT * FROM events")
    suspend fun getAll(): List<EventEntity>

    @Query("SELECT * FROM events WHERE id = :id")
    suspend fun getById(id: String): EventEntity?

    @Upsert
    suspend fun upsertAll(vararg events: EventEntity)

    @Query("DELETE FROM events WHERE id = :id")
    suspend fun deleteById(id: String)
}
