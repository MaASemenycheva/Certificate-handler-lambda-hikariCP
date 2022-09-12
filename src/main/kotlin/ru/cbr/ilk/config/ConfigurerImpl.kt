package ru.cbr.ilk.config

import com.zaxxer.hikari.HikariDataSource
import org.postgresql.ds.common.BaseDataSource
import ru.cbr.ilk.let
import java.util.*

class ConfigurerImpl: Configurer {
//    private val dsServerNames: Array<String> = getRequiredParameter("DS_SERVER_NAMES").split(',').toTypedArray()
//    private val dsPortNumbers: IntArray = getRequiredParameter("DS_PORT_NUMBERS").split(',').map(String::toInt).toTypedArray().toIntArray()
//    private val dsDatabaseName: String = getRequiredParameter("DS_DATABASE_NAME")
//    private val dsUser: String = getRequiredParameter("DS_USER")
//    private val dsPassword: String = getRequiredParameter("DS_PASSWORD")
//
//    private val dsBatchSize: Int = getOptionalParameter("DS_BATCH_SIZE")?.toInt()?.takeIf { it > 1 } ?:1
//    private val dsValidityTimeout: Int = getRequiredParameter("DS_VALIDITY_TIMEOUT").toInt().takeIf{ it > 10 } ?: 10
//    private val dsPoolingInterval: Long = getRequiredParameter("DS_POOLING_INTERVAL").toLong().takeIf{ it > 10 } ?: 10

    private val dsServerNames: Array<String> = arrayOf("localhost")
    private val dsPortNumbers: IntArray = intArrayOf(5432)
    private val dsDatabaseName: String = "test"
    private val dsUser: String = "postgres"
    private val dsPassword: String = "admin"

    private val dsBatchSize: Int = 10
    private val dsValidityTimeout: Int = 10
    private val dsPoolingInterval: Long = 10

    @Suppress("SameParameterValue")
    private fun getOptionalParameter(name: String): String? = name.let(System::getenv)
        .takeUnless { it.isNullOrBlank() }

    private fun getRequiredParameter(name: String): String = name.let(::getOptionalParameter)
        .let(::requireNotNull) {"Failed to read parameter $name" }
         ?: "UNSET"

    override fun configure(ds: BaseDataSource) =ds.apply {
        serverNames = dsServerNames
        portNumbers = dsPortNumbers
        databaseName = dsDatabaseName
        user = dsUser
        password = dsPassword
    }

    override fun hikariConfigure(ds: HikariDataSource) =ds.apply {
//        jdbcUrl = "jdbc:postgresql://$dsServerNames:$dsPortNumbers.to/$dsDatabaseName"
//        jdbcUrl = "jdbc:postgresql://\"+dsServerNames+\":5432/test"
        jdbcUrl = "jdbc:postgresql://localhost:5432/test"
        username = dsUser
        password = dsPassword
        driverClassName = "org.postgresql.Driver"
        dataSourceProperties = mapOf("cachePrepStmts" to "true").toProperties()
        dataSourceProperties = mapOf("prepStmtCacheSize" to "250").toProperties()
        dataSourceProperties = mapOf("prepStmtCacheSqlLimit" to "2048").toProperties()
    }

    override fun getBatchSize(): Int = dsBatchSize
    override fun getTimeout(): Int = dsValidityTimeout
    override fun getInterval(): Long = dsPoolingInterval
}