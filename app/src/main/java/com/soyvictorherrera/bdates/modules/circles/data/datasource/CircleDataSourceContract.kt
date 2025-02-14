package com.soyvictorherrera.bdates.modules.circles.data.datasource


interface CircleDataSourceContract<T> {

    /**
     * Get a list of all the user circles
     */
    suspend fun getCircles(): List<T>

    /**
     * Get a circle by its [id]
     */
    suspend fun getCircle(id: String): T

    /**
     * Create a new circle
     */
    suspend fun createCircle(circle: T): String

    /**
     * Update a circle by it's [id]
     */
    suspend fun updateCircle(circle: T)

}
