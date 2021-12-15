@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package utils.extensions

fun <T : Any> MutableMap<T, Int>.putOrAdd(key: T, amount: Int) {
    put(key, getOrDefault(key, 0) + amount)
}

fun <T : Any> MutableMap<T, Long>.putOrAdd(key: T, amount: Long) {
    put(key, getOrDefault(key, 0) + amount)
}

/**
 * Removes the specified keys and their corresponding values from this map.
 *
 * Based on MutableMap<K, V>.remove(K) and MutableCollection<T>.utils.extensions.removeAll(Collection<T>).
 *
 * @return `true` if any of the specified keys were removed from the map, `false` if the map was not modified.
 */
fun <K, V : Any> MutableMap<K, V>.removeAll(keys: Iterable<K>): Boolean {
    return keys.mapNotNull { remove(it) }.isNotEmpty()
}
