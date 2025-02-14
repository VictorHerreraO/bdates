package com.soyvictorherrera.bdates.modules.notifications.data.preferences

import com.soyvictorherrera.bdates.core.date.DateFormattersContract
import com.soyvictorherrera.bdates.core.persistence.KeyValueStoreContract
import com.soyvictorherrera.bdates.core.persistence.stringKey
import java.time.LocalDate
import javax.inject.Inject

interface NotificationPreferencesContract {
    var lastShownNotificationDate: LocalDate?
}

class NotificationPreferences @Inject constructor(
    private val store: KeyValueStoreContract,
    private val dateFormatters: DateFormattersContract,
) : NotificationPreferencesContract {
    private companion object {
        val LAST_SHOWN_NOTIFICATION_DATE = stringKey("last_shown_notification_date")
    }

    private val ddMMYYYYFormatter get() = dateFormatters.ddMMYYYYFormatter

    override var lastShownNotificationDate: LocalDate?
        get() = store[LAST_SHOWN_NOTIFICATION_DATE]?.let {
            runCatching { LocalDate.parse(it, ddMMYYYYFormatter) }.getOrNull()
        }
        set(value) {
            store[LAST_SHOWN_NOTIFICATION_DATE] = value?.let {
                runCatching { it.format(ddMMYYYYFormatter) }.getOrNull()
            }
        }
}
