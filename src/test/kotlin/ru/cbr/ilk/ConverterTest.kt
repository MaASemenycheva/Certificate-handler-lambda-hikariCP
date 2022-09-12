package ru.cbr.ilk

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import ru.cbr.ilk.model.converter.stateCertificateToCertificateDecode
import ru.cbr.ilk.model.dto.CertificateDecode
import java.util.*
import ru.cbr.ilk.model.dao.StateCertificate

class ConverterTest {
    private fun assertEqualsCertificateDecode(expected: CertificateDecode, actual: CertificateDecode) {
        assertEquals(expected.certBeginDate, actual.certBeginDate)
        //TODO FIX TESTS INVOLVING DATES ON DOCKER assertEquals(expected.certBeginDate, actual.certBeginDate)
        assertEquals(expected.keyId, actual.keyId)
        assertEquals(expected.certificateId, actual.certificateId)
        assertEquals(expected.certStatus, actual.certStatus)
        assertEquals(expected.orgO, actual.orgO)
        assertEquals(expected.nameCn, actual.nameCn)
        assertEquals(expected.departmentOu, actual.departmentOu)
        assertEquals(expected.oidValue, actual.oidValue)
        assertEquals(expected.dnCnCert, actual.dnCnCert)
    }


    @Test
    fun testConvert() {
        val testCertId = "testIdentifier12345"
        val expectedObject = CertificateDecode(
            keyId = "0xF1768E0151D2a23fd7B6a543edbE9fB1572B5cFe",
            certificateId = "testIdentifier12345",
            certBeginDate = Date(2021-1900, 8, 27, 14, 34, 7), //"Mon Sep 27 14:34:07 MSK 2021"
            certExpiredDate = Date(2027-1900, 11, 21, 10, 48, 3), //"Mon Dec 27 10:48:03 MSK 2027"
            keyBeginDate = Date(2021-1900, 8, 27, 14, 34, 7), //"Mon Sep 27 14:34:07 MSK 2021"
            keyExpiredDate = Date(2022-1900, 11, 27, 14, 34, 7), //"Mon Dec 27 14:34:07 MSK 2022"
            certStatus = "UNCHECKED_CERTIFICATE_STATUS",
            orgO = "test.ru.cbrdc.prt.Iss.111111111-1111-4111-8111-11111111111",
            nameCn = "PROCESSING001",
            departmentOu = "test.ru.cbrdc.wlt.Iss.111111111-1111-4111-8111-111111111111",
            oidValue = "1.2.643.7.1.1.3.2",
            dnCnCert = "CN=PROCESSING001, OU=test.ru.cbrdc.wlt.Iss.111111111-1111-4111-8111-111111111111, O=test.ru.cbrdc.wlt.Iss.111111111-1111-4111-8111-111111111111, L=CBDC, ST=00, C=RU"
        )

        "/certificate.json"
            .let(ConverterTest::class.java::getResource)
            .readText()
            .let(testCertId::to)
            .let(::StateCertificate)
            .let(::stateCertificateToCertificateDecode)
            .let(expectedObject::to)
            .let(::assertEqualsCertificateDecode)
    }

}