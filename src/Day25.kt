import utils.GridPoint2d
import utils.readInput

fun main() {
    fun List<String>.parse(): Map<GridPoint2d, Char> {
        fun charAt(point: GridPoint2d): Char {
            return this.getOrNull(point.y)?.getOrNull(point.x)!!
        }

        return buildMap {
            for (y in this@parse.indices) {
                for (x in this@parse[0].indices) {
                    val point = GridPoint2d(x, y)
                    val char = charAt(point)
                    if (charAt(point) != '.') put(point, char)
                }
            }
        }
    }

    @Suppress("DuplicatedCode")
    fun Map<GridPoint2d, Char>.move(xMax: Int, yMax: Int): Map<GridPoint2d, Char> {
        val prev = this
        val next = this.toMutableMap()

        prev.filterValues { char -> char == '>' }
            .keys
            .map { point -> point to point.copy(x = (point.x + 1) % (xMax + 1)) }
            .filterNot { (_, target) -> target in prev }
            .forEach { (point, target) ->
                next -= point
                next[target] = '>'
            }

        next.filterValues { char -> char == 'v' }
            .keys
            .map { point -> point to point.copy(y = (point.y + 1) % (yMax + 1)) }
            .filterNot { (_, target) -> target in next }
            .forEach { (point, target) ->
                next -= point
                next[target] = 'v'
            }

        return next
    }

    fun part1(input: List<String>): Int {
        val xMax = input[0].lastIndex
        val yMax = input.lastIndex

        return generateSequence(
            seed = input.parse(),
            nextFunction = { fish -> fish.move(xMax, yMax) }
        ).zipWithNext()
            .indexOfFirst { (prev, next) -> prev == next }
            .plus(1)
    }

    val testInput = """
        v...>>.vv>
        .vv>>.vv..
        >>.>v>...v
        >>v>>.>.v.
        v>v.vv.v..
        >.>>..v...
        .vv..>.>v.
        v.v..>>v.v
        ....v..v.>
    """.trimIndent().split('\n')

    check(part1(testInput) == 58)

    val input = readInput("Day25")
    println(part1(input))
}
