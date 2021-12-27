package com.soyvictorherrera.bdates.data.repository.mapper

import com.soyvictorherrera.bdates.domain.model.FriendModel
import com.soyvictorherrera.bdates.utils.Mapper
import org.json.JSONObject

class JSONFriendMapper : Mapper<JSONObject, FriendModel>() {
    override fun toModel(value: JSONObject): FriendModel = with(value) {
        return FriendModel(
            id = optString("id"),
            name = optString("nombre"),
            birthDateString = optString("fechaNac")
        )
    }

    override fun fromModel(model: FriendModel): JSONObject = with(model) {
        return JSONObject()
            .put("id", id)
            .put("nombre", name)
            .put("fechaNac", birthDateString)
    }
}
