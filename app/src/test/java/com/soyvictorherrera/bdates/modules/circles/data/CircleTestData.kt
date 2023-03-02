package com.soyvictorherrera.bdates.modules.circles.data

import com.soyvictorherrera.bdates.modules.circles.data.datasource.local.CircleEntity
import com.soyvictorherrera.bdates.modules.circles.domain.model.Circle

fun circleModel() = Circle(
    id = "circle-foo",
    name = "Foo",
    description = "Foo description",
    isLocalOnly = false
)

fun circleEntity() = CircleEntity(
    id = "circle-foo",
    name = "Foo",
    description = "Foo description",
    isLocalOnly = false
)
