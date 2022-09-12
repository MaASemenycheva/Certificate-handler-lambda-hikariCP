package ru.cbr.ilk.service

import ru.cbr.ilk.config.Configurer
import java.sql.Connection

abstract class DatabaseService (private val configurer: Configurer, private val connectionProvider: () -> Connection) {
    protected val batchSize = configurer.getBatchSize()
    protected val timeout = configurer.getTimeout()

    protected fun getConnection() = connectionProvider().takeIf { it.isValid(timeout) }
}