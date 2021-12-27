package com.soyvictorherrera.bdates.data.persistence

import org.json.JSONArray
import org.json.JSONObject

class InMemoryFriendDataSource {
    private var list: JSONArray

    private object Source {
        const val JSON = "[]"
    }

    init {
        list = JSONArray(Source.JSON)
    }

    fun getFriendList(): List<JSONObject> {
        return mutableListOf<JSONObject>().apply {
            for (i in 0 until list.length()) {
                list.optJSONObject(i)?.let {
                    add(it)
                }
            }
        }
    }

}