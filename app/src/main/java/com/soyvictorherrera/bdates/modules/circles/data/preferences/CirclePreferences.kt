package com.soyvictorherrera.bdates.modules.circles.data.preferences

import com.soyvictorherrera.bdates.core.persistence.Key
import com.soyvictorherrera.bdates.core.persistence.KeyValueStoreContract
import com.soyvictorherrera.bdates.core.persistence.booleanKey
import com.soyvictorherrera.bdates.core.persistence.stringKey
import com.soyvictorherrera.bdates.modules.eventList.data.datasource.assets.forEach
import javax.inject.Inject
import org.json.JSONObject

interface CirclePreferencesContract {
    var isLocalCircleCreated: Boolean
    var localCircleId: String?
    val updateTimestamps: KeyValuePreference<Long>
}

class CirclePreferences @Inject constructor(
    private val store: KeyValueStoreContract,
) : CirclePreferencesContract {
    private companion object {
        val IS_LOCAL_CIRCLE_CREATED_KEY = booleanKey("is_local_circle_created")
        val LOCAL_CIRCLE_ID_KEY = stringKey("local_circle_id")
        val UPDATE_TIMESTAMPS = stringKey("update_timestamps")
    }

    override var isLocalCircleCreated: Boolean
        get() = store[IS_LOCAL_CIRCLE_CREATED_KEY] ?: false
        set(value) {
            store[IS_LOCAL_CIRCLE_CREATED_KEY] = value
        }

    override var localCircleId: String?
        get() = store[LOCAL_CIRCLE_ID_KEY]
        set(value) {
            store[LOCAL_CIRCLE_ID_KEY] = value
        }

    override val updateTimestamps: KeyValuePreference<Long> = KeyValueLongPreference(
        store = store,
        storeKey = UPDATE_TIMESTAMPS
    )
}

interface KeyValuePreference<T> {
    operator fun set(key: String, value: T?)
    operator fun get(key: String): T?
}

internal class KeyValueLongPreference(
    private val store: KeyValueStoreContract,
    private val storeKey: Key<String>,
) : KeyValuePreference<Long> {
    private val valueJSON: JSONObject

    init {
        val valueString = store[storeKey].orEmpty()
        valueJSON = try {
            JSONObject(valueString)
        } catch (ex: Exception) {
            JSONObject()
        }
        valueJSON.names()?.forEach {
            this[it as String] = valueJSON.getLong(it)
        }
    }

    override fun set(key: String, value: Long?) {
        valueJSON.put(key, value)
        valueJSON.sync()
    }

    override fun get(key: String): Long? {
        return valueJSON.optLong(key)
    }

    private fun JSONObject.sync() {
        store[storeKey] = toString()
    }
}
