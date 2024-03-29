package com.soyvictorherrera.bdates.core.persistence

import android.content.SharedPreferences
import androidx.core.content.edit
import javax.inject.Inject
import kotlin.reflect.KClass

const val APP_SHARED_PREFS_FILE_NAME = "app_preferences"

class Key<T : Any> internal constructor(val name: String, val type: KClass<T>)

/**
 * Create a [Key] with name = [name] and type = [String]
 * Shortcut for:
 * ```
 * Key(name, String::class)
 * ```
 */
fun stringKey(name: String): Key<String> = Key(name, String::class)

/**
 * Create a [Key] with name = [name] and type = [Boolean]
 * Shortcut for:
 * ```
 * Key(name, Boolean::class)
 * ```
 */
fun booleanKey(name: String): Key<Boolean> = Key(name, Boolean::class)

interface KeyValueStoreContract {
    /**
     * Stores a value of type [T] with the given [Key.name]
     *
     * @throws ClassCastException if [value] can't be casted as [T]
     */
    @Throws(ClassCastException::class)
    operator fun <T : Any> set(key: Key<T>, value: T?)

    /**
     * Retrieve a value of type [T] with the given [Key.name]
     * @throws ClassCastException if the stored value can't be casted as [T]
     */
    @Throws(ClassCastException::class)
    operator fun <T : Any> get(key: Key<T>): T?
}

class KeyValueStore @Inject constructor(
    private val sharedPreferences: SharedPreferences,
) : KeyValueStoreContract {

    override fun <T : Any> set(key: Key<T>, value: T?) {
        sharedPreferences.edit {
            when (key.type) {
                String::class -> putString(key.name, value as String)
                Boolean::class -> putBoolean(key.name, value as Boolean)
                else -> throw IllegalArgumentException("Unknown key type: ${key.type}")
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    override operator fun <T : Any> get(key: Key<T>): T? {
        val contained = sharedPreferences.contains(key.name)
        return if (contained) {
            with(sharedPreferences) {
                when (key.type) {
                    String::class -> getString(key.name, null)
                    Boolean::class -> getBoolean(key.name, false)
                    else -> throw IllegalArgumentException("Unknown key type: ${key.type}")
                }
            } as T
        } else {
            null
        }
    }
}
