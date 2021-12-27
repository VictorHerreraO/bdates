package com.soyvictorherrera.bdates.data.repository

interface TemplatesRepository {
    suspend fun getTemplateList(): List<String>
}
