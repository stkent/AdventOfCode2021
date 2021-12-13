import utils.extensions.elementCounts
import utils.extensions.flip
import utils.readInput

fun main() {
    val startCave = "start"
    val endCave = "end"

    fun List<String>.edges(): Map<String, Set<String>> {
        return this
            .map { rawEdge ->
                rawEdge
                    .split('-')
                    .let { (startCave, endCave) -> startCave to endCave }
            }
            .flatMap { edge -> listOf(edge, edge.flip()) }
            .groupBy { (startCave, _) -> startCave }
            .mapValues { (_, edges) ->
                edges
                    .map { (_, endCave) -> endCave }
                    .toSet()
            }
    }

    fun List<String>.countPaths(canRevisitSmallCaves: (visitedSmallCaves: List<String>) -> Boolean): Int {
        val edges = this.edges()

        val allCaves = edges.keys
        val nonTerminalCaves = allCaves - startCave - endCave
        val bigCaves = nonTerminalCaves.filter { cave -> cave.all(Char::isUpperCase) }.toSet()
        val smallCaves = nonTerminalCaves - bigCaves

        val incompletePaths = ArrayDeque(listOf(listOf(startCave)))
        val completePaths = mutableSetOf<List<String>>()

        while (incompletePaths.isNotEmpty()) {
            val currentPath = incompletePaths.removeFirst()
            val currentCave = currentPath.last()
            val adjacentCaves = edges[currentCave]!!
            val visitedSmallCaves = currentPath.filter { cave -> cave in smallCaves }

            val blockedCaves = buildSet {
                add(startCave)

                if (!canRevisitSmallCaves(visitedSmallCaves)) {
                    addAll(visitedSmallCaves)
                }
            }

            (adjacentCaves - blockedCaves)
                .forEach { nextCave ->
                    val extendedPath = buildList {
                        addAll(currentPath)
                        add(nextCave)
                    }

                    if (nextCave == endCave) {
                        completePaths += extendedPath
                    } else {
                        incompletePaths += extendedPath
                    }
                }
        }

        return completePaths.size
    }

    fun part1(input: List<String>): Int {
        return input.countPaths(canRevisitSmallCaves = { false })
    }

    fun part2(input: List<String>): Int {
        return input
            .countPaths(
                canRevisitSmallCaves = { visitedSmallCaves ->
                    visitedSmallCaves
                        .elementCounts()
                        .none { (_, visitCount) -> visitCount > 1 }
                }
            )
    }

    val testInput = """
        start-A
        start-b
        A-c
        A-b
        b-d
        A-end
        b-end
    """.trimIndent().split('\n')

    check(part1(testInput) == 10)
    check(part2(testInput) == 36)

    val input = readInput("Day12")
    println(part1(input))
    println(part2(input))
}
