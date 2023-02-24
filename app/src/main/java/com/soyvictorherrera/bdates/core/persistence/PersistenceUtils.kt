package com.soyvictorherrera.bdates.core.persistence

import java.util.UUID

typealias OnCreated = (String) -> Unit

fun randomUUID(): String = UUID.randomUUID().toString()
