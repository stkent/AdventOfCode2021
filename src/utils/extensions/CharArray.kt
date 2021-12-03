@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package utils.extensions

fun CharArray.collapseToString(): String {
    return joinToString(separator = "")
}
