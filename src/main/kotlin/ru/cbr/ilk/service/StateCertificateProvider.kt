package ru.cbr.ilk.service

import mu.KotlinLogging
import ru.cbr.ilk.config.Configurer
import ru.cbr.ilk.model.dao.StateCertificate
import java.sql.Connection
import ru.cbr.ilk.map

private val logger = KotlinLogging.logger {}

class StateCertificateProvider  (configurer:Configurer, connectionProvider: () -> Connection) : DatabaseService(configurer, connectionProvider) {
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