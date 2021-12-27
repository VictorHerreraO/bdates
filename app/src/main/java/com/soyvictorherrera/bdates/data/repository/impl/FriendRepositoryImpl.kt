package com.soyvictorherrera.bdates.data.repository.impl

import com.soyvictorherrera.bdates.data.persistence.InMemoryFriendDataSource
import com.soyvictorherrera.bdates.data.repository.FriendRepository
import com.soyvictorherrera.bdates.data.repository.mapper.JSONFriendMapper
import com.soyvictorherrera.bdates.domain.model.FriendModel

class FriendRepositoryImpl(
    private val friendDataSource: InMemoryFriendDataSource,
    private val friendMapper: JSONFriendMapper
) : FriendRepository {

    override suspend fun getFriendList(): List<FriendModel> {
        return friendDataSource.getFriendList()
            .map {
                friendMapper.toModel(it)
            }
    }

}
