package ru.cbr.ilk.model.dao

import java.sql.ResultSet

class StateCertificate (
    val certificateId: String,
    val payload: String
) {
    constructor(rs: ResultSet): this(rs.getString("certificate_id"), rs.getString("payload"))
}