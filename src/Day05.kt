import utils.GridPoint2d
import utils.GridVector2d
import utils.extensions.elementCounts
import utils.intRangeSpanning
import utils.readInput
import kotlin.math.abs
import kotlin.math.sign

fun main() {
    fun parseInputPoint(point: String): GridPoint2d {
        val (x, y) = point.split(',').map(String::toInt)
        return GridPoint2d(x = x, y = y)
    }

    fun parseInputLine(line: String): Pair<GridPoint2d, GridPoint2d> {
        val (p1, p2) = line.split(" -> ").map(::parseInputPoint)
        return p1 to p2
    }

    fun countDoublyCoveredPoints(input: List<String>, includeDiagonalLines: Boolean): Int {
        return input
            .map(::parseInputLine)
            .flatMap { (p1, p2) ->
                when {
                    p1.x == p2.x -> intRangeSpanning(p1.y, p2.y).map { y -> GridPoint2d(p1.x, y) }
                    p1.y == p2.y -> intRangeSpanning(p1.x, p2.x).map { x -> GridPoint2d(x, p1.y) }
                    else -> {
                        if (includeDiagonalLines) {
                            val stepVector = with(p1.vectorTo(p2)) { GridVector2d(x.sign, y.sign) }

                            buildList {
                                repeat(abs(p2.x - p1.x) + 1) { step ->
                                    add(p1 + stepVector * step)
                                }
                            }
                        } else {
                            emptyList()
                        }
                    }
                }
            }
            .elementCounts()
            .count { (_, pointCount) -> pointCount > 1 }
    }

    fun part1(input: List<String>): Int {
        return countDoublyCoveredPoints(input, includeDiagonalLines = false)
    }

    fun part2(input: List<String>): Int {
        return countDoublyCoveredPoints(input, includeDiagonalLines = true)
    }

    val testInput = """
        0,9 -> 5,9
        8,0 -> 0,8
        9,4 -> 3,4
        2,2 -> 2,1
        7,0 -> 7,4
        6,4 -> 2,0
        0,9 -> 2,9
        3,4 -> 1,4
        0,0 -> 8,8
        5,5 -> 8,2
    """.trimIndent().split('\n')

    check(part1(testInput) == 5)
    check(part2(testInput) == 12)

    val input = readInput("Day05")
    println(part1(input))
    println(part2(input))
}
