package ru.cbr.ilk.service

import io.micrometer.core.instrument.MeterRegistry
import org.springframework.stereotype.Service
import ru.cbr.ilk.config.Configurer
import java.sql.Connection

abstract class DatabaseService(
    open val configure: Configurer,
    open val connectionProvider: () -> Connection,
    open val meterRegistry: MeterRegistry,
) {
    var count = meterRegistry.gauge("certCounter", 0)
    protected val batchSize = configure.getBatchSize()
    protected val timeout = configure.getTimeout()
    protected fun getConnection() = connectionProvider().takeIf { it.isValid(timeout)
    }
}