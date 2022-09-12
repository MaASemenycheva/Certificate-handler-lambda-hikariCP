package ru.cbr.ilk.model.dto

import java.util.*

class CertificateDecode (
    val keyId: String,
    val certificateId: String,
    val certBeginDate: Date,
    val certExpiredDate: Date,
    val keyBeginDate: Date,
    val keyExpiredDate: Date,
    val certStatus: String,
    val orgO: String,
    val nameCn: String,
    val departmentOu: String,
    val oidValue: String,
    val dnCnCert: String
)