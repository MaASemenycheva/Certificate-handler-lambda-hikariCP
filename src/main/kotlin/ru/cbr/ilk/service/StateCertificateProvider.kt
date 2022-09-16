package ru.cbr.ilk.service

import com.zaxxer.hikari.HikariDataSource
import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.MeterRegistry
import mu.KotlinLogging
import ru.cbr.ilk.config.Configurer
import ru.cbr.ilk.config.ConfigurerImpl
import ru.cbr.ilk.map
import ru.cbr.ilk.model.dao.StateCertificate
import java.sql.Connection

private val logger = KotlinLogging.logger {}


class StateCertificateProvider  (configurer:Configurer, connectionProvider: () -> Connection,
                                 meterRegistry: MeterRegistry
) : DatabaseService(configurer, connectionProvider, meterRegistry) {




    private val schema = "public"
    private val queryString = """
        SELECT s.certificate_id, t.payload
        FROM $schema.state_certificate s 
        JOIN $schema.decoded_transaction t ON t.transaction_id = s.decoded_transaction_id
        LEFT JOIN $schema.certificate_decode d ON d.certificate_id = s.certificate_id
        WHERE d.certificate_id IS null
        LIMIT $batchSize
    """.trimIndent()




    fun get(): List<StateCertificate> = getConnection()
        ?.use {conn ->
            count = count?.plus(1)
            logger.info {"sdmsld ===== " + count}
            val config: Configurer = ConfigurerImpl()
            count?.apply(config::getTotalNumberOfProcessedCertificates)
            conn.createStatement()
                .use { st ->
                    st.executeQuery(queryString)
                        .use { it.map(::StateCertificate)}
                }
        }
        ?: let {
            logger.warn { "Failed to retrieve valid database connection" }
            emptyList()
        }
}