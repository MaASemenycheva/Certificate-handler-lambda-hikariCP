package ru.cbr.ilk.service

import mu.KotlinLogging
import ru.cbr.ilk.config.Configurer
import ru.cbr.ilk.model.dto.CertificateDecode
import java.sql.Connection
import java.sql.Timestamp

private val logger = KotlinLogging.logger {}

class CertificateDecodeConsumer (configurer: Configurer, connectionProvider: () -> Connection) : DatabaseService(configurer, connectionProvider) {
    private val schema = "public"
    private val insertQueryString = """
        INSERT INTO $schema.certificate_decode(keyId, certificate_id, cert_begin_date, cert_expired_date, key_begin_date, key_expired_date, cert_status, org_o, name_cn, department_ou, oid_value, dn_cn_cert)
        VALUES (?,?,?,?,?,?,?,?,?,?,?)
        ON CONFLICT DO NOTHING
    """.trimIndent()

    fun put(certificateDecodeList: List<CertificateDecode>): Unit = getConnection()
        ?.use { conn ->
            conn.prepareStatement(insertQueryString)
                .use { ps ->
                    certificateDecodeList.forEach {
                        ps.setString(1, it.certificateId)
                        ps.setTimestamp(2, it.certBeginDate.time.let(::Timestamp))
                        ps.setTimestamp(3, it.certExpiredDate.time.let(::Timestamp))
                        ps.setTimestamp(4, it.keyBeginDate.time.let(::Timestamp))
                        ps.setTimestamp(5, it.keyExpiredDate.time.let(::Timestamp))
                        ps.setString(6, it.certStatus)
                        ps.setString(7, it.orgO)
                        ps.setString(8, it.nameCn)
                        ps.setString(9, it.departmentOu)
                        ps.setString(10, it.oidValue)
                        ps.setString(11, it.dnCnCert)

                        ps.addBatch()
                    }
                    ps.executeBatch()

                    logger.info {
                        certificateDecodeList.joinToString(",") { it.certificateId }
                            .let { "Processed certificates: $it" }
                    }
                }
        }
        ?: logger.warn { "Failed to retrieve valid database connection" }
}