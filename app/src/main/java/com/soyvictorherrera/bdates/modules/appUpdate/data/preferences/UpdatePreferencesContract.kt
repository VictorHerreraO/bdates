package com.soyvictorherrera.bdates.modules.appUpdate.data.preferences

import com.soyvictorherrera.bdates.core.persistence.KeyValueStoreContract
import com.soyvictorherrera.bdates.core.persistence.stringKey
import javax.inject.Inject

interface UpdatePreferencesContract {
    var previousVersionCode: Long?
}

class UpdatePreferences @Inject constructor(
    private val store: KeyValueStoreContract,
) : UpdatePreferencesContract {

    companion object {
        private val PREVIOUS_VERSION_KEY = stringKey("previous_version")
    }

    override var previousVersionCode: Long?
        get() = store[PREVIOUS_VERSION_KEY]?.toLong()
        set(value) {
            store[PREVIOUS_VERSION_KEY] = value?.toString()
        }
}