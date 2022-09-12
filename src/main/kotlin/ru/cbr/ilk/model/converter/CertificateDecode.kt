package ru.cbr.ilk.model.converter

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import ru.cbr.ilk.let


import ru.cbr.ilk.model.dao.StateCertificate
import ru.cbr.ilk.model.dto.CertificateDecode
import ru.cbr.ilk.model.dto.StateCertificatePayload
import ru.cbr.ilk.model.dto.X509CertificateData

fun x509CertificateDataToCertificateDecode (
    x509cd: X509CertificateData,
    keyId: String,
    certificateId: String,
    certStatus: String
): CertificateDecode = CertificateDecode(
    keyId = keyId,
    certificateId = certificateId,
    certBeginDate = x509cd.certBeginDate,
    certExpiredDate = x509cd.certExpiredDate,
    keyBeginDate = x509cd.keyBeginDate,
    keyExpiredDate = x509cd.keyExpiredDate,
    certStatus = certStatus,
    orgO = x509cd.orgO,
    nameCn = x509cd.nameCn,
    departmentOu = x509cd.departmentOu,
    oidValue = x509cd.oidValue,
    dnCnCert = x509cd.dnCnCert,
)

val objectMapper = jacksonObjectMapper().registerKotlinModule()

fun stateCertificateToCertificateDecode(
    stateCertificate: StateCertificate,
    certStatus: String = "UNCHECKED_CERTIFICATE_STATUS"
): CertificateDecode = (stateCertificate.payload to StateCertificatePayload::class.java)
    .let(objectMapper::readValue)
    .let {
        x509CertificateDataToCertificateDecode(
            x509cd = it.certificate.let(X509CertificateData::base64toX509CertificateData),
            keyId = it.keyId,
            certificateId = stateCertificate.certificateId,
            certStatus = certStatus,
        )
    }
