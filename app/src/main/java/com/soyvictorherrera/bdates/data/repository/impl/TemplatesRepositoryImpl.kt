package com.soyvictorherrera.bdates.data.repository.impl

import com.soyvictorherrera.bdates.data.persistence.InMemoryTemplatesDataSource
import com.soyvictorherrera.bdates.data.repository.TemplatesRepository

class TemplatesRepositoryImpl(
    private val templatesDataSource: InMemoryTemplatesDataSource
) : TemplatesRepository {
    override suspend fun getTemplateList(): List<String> {
        return templatesDataSource.getTemplateList()
    }
}