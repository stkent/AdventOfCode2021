package utils

/**
 * Finds the first tree node satisfying [predicate], starting from [root], using a depth-first search.
 *
 * Children are explored in the order returned by [children].
 */
fun <T : Any> treeDepthFirstSearch(
    root: T,
    children: T.() -> List<T>,
    predicate: (T) -> Boolean,
): T? {
    val stack = ArrayDeque<T>()
    stack.add(root)

    while (stack.isNotEmpty()) {
        val node = stack.removeLast()
        if (predicate(node)) return node
        node.children().reversed().forEach(stack::addLast)
    }

    return null
}
