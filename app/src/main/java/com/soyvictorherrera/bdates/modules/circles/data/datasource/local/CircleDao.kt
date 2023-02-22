package com.soyvictorherrera.bdates.modules.circles.data.datasource.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface CircleDao {
    @Query("SELECT * FROM circles")
    suspend fun getAll(): List<CircleEntity>

    @Query("SELECT * FROM  circles WHERE id = :id")
    suspend fun getById(id: String): CircleEntity

    @Upsert
    suspend fun upsertAll(vararg circles: CircleEntity)
}
