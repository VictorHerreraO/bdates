package com.soyvictorherrera.bdates.modules.circles.domain.model

data class Circle(
    val id: String?,
    val name: String,
    val description: String?,
    val isLocalOnly: Boolean,
    val updateDate: Long?,
)
