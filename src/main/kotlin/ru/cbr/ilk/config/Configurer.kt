package ru.cbr.ilk.config

import com.zaxxer.hikari.HikariDataSource
import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.MeterRegistry
import org.postgresql.ds.common.BaseDataSource

interface Configurer {
    fun configure(ds: BaseDataSource): BaseDataSource
    fun hikariConfigure(ds: HikariDataSource): HikariDataSource
    fun getBatchSize(): Int
    fun getTimeout(): Int
    fun getInterval(): Long
    //общее количество обработанных сертификатов
    fun getTotalNumberOfProcessedCertificates (count: Int): Int
    //количество обработанных сертификатов за последний час
    fun getNumberOfProcessedCertificatesInTheLastHour(): Long
    //общее количство ошибок при обработке сертификатов
    fun getTotalNumberOfErrorsWhileProcessingCertificates(): Long
    //количество ошибок при обработке сертификатов за последний час
    fun getNumberOfCertificateProcessingErrorsInTheLastHour(): Long
    fun getMessage(): String
}