package com.soyvictorherrera.bdates.modules.circles.data.preferences

import com.soyvictorherrera.bdates.core.persistence.KeyValueStoreContract
import com.soyvictorherrera.bdates.core.persistence.booleanKey
import com.soyvictorherrera.bdates.core.persistence.stringKey
import javax.inject.Inject

interface CirclePreferencesContract {
    var isLocalCircleCreated: Boolean
    var localCircleId: String?
}

class CirclePreferences @Inject constructor(
    private val store: KeyValueStoreContract
) : CirclePreferencesContract {
    private companion object {
        val IS_LOCAL_CIRCLE_CREATED_KEY = booleanKey("is_local_circle_created")
        val LOCAL_CIRCLE_ID_KEY = stringKey("local_circle_id")
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
}
