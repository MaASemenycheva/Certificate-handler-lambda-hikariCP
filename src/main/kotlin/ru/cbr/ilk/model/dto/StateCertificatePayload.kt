package ru.cbr.ilk.model.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class StateCertificatePayload (
    val certificate: String,
    val keyId: String,
    //val memberId: String,
    //val senderId: String,
    //val walletId: String,
    //val roleId: Int,

        )