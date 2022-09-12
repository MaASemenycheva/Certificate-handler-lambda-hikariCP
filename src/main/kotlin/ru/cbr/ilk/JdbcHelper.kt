package ru.cbr.ilk

import java.sql.ResultSet

fun <T> ResultSet.map(mapper: (ResultSet) -> T): List<T> = mutableListOf<T>().also {
    while (next()) {
        let(mapper).let(it::add)
    }
}