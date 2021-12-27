package com.soyvictorherrera.bdates.factory

import com.soyvictorherrera.bdates.data.persistence.InMemoryFriendDataSource
import com.soyvictorherrera.bdates.data.persistence.InMemoryTemplatesDataSource
import com.soyvictorherrera.bdates.data.repository.FriendRepository
import com.soyvictorherrera.bdates.data.repository.TemplatesRepository
import com.soyvictorherrera.bdates.data.repository.impl.FriendRepositoryImpl
import com.soyvictorherrera.bdates.data.repository.impl.TemplatesRepositoryImpl
import com.soyvictorherrera.bdates.data.repository.mapper.JSONFriendMapper

object RepositoryFactory {

    fun getFriendRepository(): FriendRepository {
        return FriendRepositoryImpl(
            friendDataSource = InMemoryFriendDataSource(),
            friendMapper = JSONFriendMapper()
        )
    }

    fun getTemplatesRepository(): TemplatesRepository {
        return TemplatesRepositoryImpl(
            templatesDataSource = InMemoryTemplatesDataSource()
        )
    }

}
