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

    fun solve(riskMap: Map<GridPoint2d, Int>): Int {
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

    fun part1(input: List<String>): Int {
        val fullMap = input.toMap()
        return solve(riskMap = fullMap)
    }

    fun part2(input: List<String>): Int {
        val smallMap = input.toMap()
        val (_, maxX, _, maxY) = smallMap.keys.bounds()

        val fullMap = buildMap<GridPoint2d, Int> {
            smallMap.forEach { (inputPoint, inputRisk) ->
                repeat(5) { nx ->
                    repeat(5) { ny ->
                        val point = inputPoint.shiftBy(dx = nx * (maxX + 1), dy = ny * (maxY + 1))
                        val risk = 1 + (inputRisk + nx + ny - 1) % 9
                        put(point, risk)
                    }
                }
            }
        }

        return solve(riskMap = fullMap)
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
    check(part2(testInput) == 315)

    val input = readInput("Day15")
    println(part1(input))
    println(part2(input))
}
