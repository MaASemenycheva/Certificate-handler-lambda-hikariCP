package ru.cbr.ilk

import kotlin.reflect.KClass


// Usage:
// "a".let("b"::plus)
// 1L.to(2L).let(Long::plus)
// Similar to: inline fun <S, T, R> Pair<S,T>.let(block: (S, T) -> R): R = block(first, second)
inline fun <S, T, R> Pair<S, T>.let(block: S.(T) -> R): R = first.block(second)

//deconstructors: inline fun <S, T, U, R> Pair<Pair<S, T>, U>.let(block: S.(T, U) -> R): R = first.first.block(first.second, second)

//Usage:
//Object.let(::requireNotNull) { "This object cannot be null!" }
inline fun <T> T.let(block: (T, () -> Any) -> T, noinline function: () -> Any): T = block(this, function)

@Suppress("UNCHECKED_CAST")
fun <T, R: Any> T.cast(unused: KClass<R>): R = this as R