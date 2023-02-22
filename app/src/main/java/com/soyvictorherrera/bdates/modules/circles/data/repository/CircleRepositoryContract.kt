package com.soyvictorherrera.bdates.modules.circles.data.repository

import com.soyvictorherrera.bdates.modules.circles.domain.model.Circle

interface CircleRepositoryContract {

    /**
     * Get a list of all the user circles
     */
    suspend fun getCircles(): List<Circle>

    /**
     * Get a circle by its [id]
     */
    suspend fun getCircle(id: String): Circle

    /**
     * Create a new [circle]
     */
    suspend fun createCircle(circle: Circle)

    /**
     * Update the given [circle]
     */
    suspend fun updateCircle(circle: Circle)

}