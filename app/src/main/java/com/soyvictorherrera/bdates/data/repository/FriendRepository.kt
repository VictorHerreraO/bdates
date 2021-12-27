package com.soyvictorherrera.bdates.data.repository

import com.soyvictorherrera.bdates.domain.model.FriendModel

interface FriendRepository {

    suspend fun getFriendList(): List<FriendModel>

}
