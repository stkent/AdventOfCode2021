import utils.GridPoint2d
import utils.readInput

private data class State(val positions: Map<Char, Set<GridPoint2d>>)

private val hallwayDestinations =
    listOf(-5, -4, -2, 0, 2, 4, 5).map { x -> GridPoint2d(x, 0) }

private val finalDestinations =
    mapOf(
        'A' to (-2..-1).map { y -> GridPoint2d(-3, y) },
        'B' to (-2..-1).map { y -> GridPoint2d(-1, y) },
        'C' to (-2..-1).map { y -> GridPoint2d(+1, y) },
        'D' to (-2..-1).map { y -> GridPoint2d(+3, y) },
    )

fun main() {
    fun List<String>.parse(): State {
        val positions =
            this.drop(2)
                .take(2)
                .map { row -> row.filter(Char::isLetter) }
                .flatMapIndexed { rowIndex, letters ->
                    letters.mapIndexed { letterIndex, letter ->
                        letter to GridPoint2d(x = -3 + 2 * letterIndex, y = -rowIndex - 1)
                    }
                }
                .groupBy { (letter, _) -> letter }
                .mapValues { (_, letterLocations) ->
                    letterLocations
                        .map { (_, location) -> location }
                        .toSet()
                }

        return State(positions)
    }

    fun part1(input: List<String>): Int {
        val initialState = input.parse()
        return 0
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    val testInput = """
        #############
        #...........#
        ###B#C#B#D###
          #A#D#C#A#
          #########
    """.trimIndent().split('\n')

    check(part1(testInput) == 12521)

    val input = readInput("Day23")
    println(part1(input))
}
