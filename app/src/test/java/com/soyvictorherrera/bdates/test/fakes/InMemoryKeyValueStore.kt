package com.soyvictorherrera.bdates.test.fakes

import com.soyvictorherrera.bdates.core.persistence.Key
import com.soyvictorherrera.bdates.core.persistence.KeyValueStoreContract

@Suppress("UNCHECKED_CAST")
class InMemoryKeyValueStore : KeyValueStoreContract {
    private val values = mutableMapOf<String, Any?>()

    override fun <T : Any> set(key: Key<T>, value: T?) {
        values[key.name] = value
    }

    override fun <T : Any> get(key: Key<T>): T? {
        return values[key.name] as? T
    }
}