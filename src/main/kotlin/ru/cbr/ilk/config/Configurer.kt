package ru.cbr.ilk.config

import com.zaxxer.hikari.HikariDataSource
import org.postgresql.ds.common.BaseDataSource

interface Configurer {
    fun configure(ds: BaseDataSource): BaseDataSource
    fun hikariConfigure(ds: HikariDataSource): HikariDataSource
    fun getBatchSize(): Int
    fun getTimeout(): Int
    fun getInterval(): Long

}