import utils.GridPoint2d
import utils.bounds
import utils.readInput
import utils.shortestDistanceTo

private typealias RiskMap = Map<GridPoint2d, Int>

fun main() {
    fun List<String>.toMap(): RiskMap {
        fun riskAt(point: GridPoint2d): Int {
            return this
                .getOrNull(point.y)
                ?.getOrNull(point.x)
                ?.digitToInt()!!
        }

        return buildMap {
            for (y in this@toMap.indices) {
                for (x in this@toMap[0].indices) {
                    val point = GridPoint2d(x, y)
                    put(point, riskAt(point))
                }
            }
        }
    }

    fun part1(input: List<String>): Int {
        val riskMap = input.toMap()
        val bounds = riskMap.keys.bounds()
        val start = GridPoint2d.origin
        val end = GridPoint2d(x = bounds.xMax, y = bounds.yMax)

        return start
            .shortestDistanceTo(
                target = end,
                getNeighbors = { current ->
                    current
                        .adjacentPoints()
                        .filter { point -> point in bounds }
                        .associateWith { point -> riskMap[point]!! }
                }
            )
            ?: error("Should not reach here.")
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    val testInput = """
        1163751742
        1381373672
        2136511328
        3694931569
        7463417111
        1319128137
        1359912421
        3125421639
        1293138521
        2311944581
    """.trimIndent().split('\n')

    check(part1(testInput) == 40)

    val input = readInput("Day15")
    println(part1(input))
}
