package com.soyvictorherrera.bdates.core.coroutines

import javax.inject.Qualifier

@Qualifier
annotation class MainCoroutineScope

@Qualifier
annotation class IODispatcher

@Qualifier
annotation class DefaultDispatcher

@Qualifier
annotation class MainDispatcher
