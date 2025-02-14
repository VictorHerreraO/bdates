package com.soyvictorherrera.bdates.util

import org.junit.rules.TestWatcher
import org.junit.runner.Description
import timber.log.Timber

/**
 * Remove all planted [Timber] trees after tests have finished
 */
class TimberTestRule : TestWatcher() {
    override fun finished(description: Description) {
        Timber.uprootAll()
    }
}
