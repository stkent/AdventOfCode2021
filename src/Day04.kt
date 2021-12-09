import com.google.common.math.IntMath.sqrt
import utils.readInput
import java.math.RoundingMode.UNNECESSARY

private data class Input(val drawOrder: List<Int>, val boards: List<Board>)

private data class Board(val nums: List<Int>) {
    private val size: Int = sqrt(nums.size, UNNECESSARY)

    private fun numAt(row: Int, col: Int): Int = nums[row * size + col]

    private val cols: List<Set<Int>> =
        (0 until size).map { col -> ((0 until size).map { row -> numAt(row, col) }).toSet() }

    private val rows: List<Set<Int>> =
        (0 until size).map { row -> ((0 until size).map { col -> numAt(row, col) }).toSet() }

    val rowsAndCols = rows + cols
}

fun main() {
    fun parseInput(input: List<String>): Input {
        val drawOrder: List<Int> = input.first().split(',').map(String::toInt)

        val numSeparator = Regex("\\s+")
        val boards: List<Board> = input
            .drop(1)
            .chunked(6) { rows ->
                Board(
                    rows.drop(1)
                        .map(String::trim)
                        .flatMap { trimmedRow -> trimmedRow.split(numSeparator) }
                        .map(String::toInt)
                )
            }

        return Input(drawOrder, boards)
    }

    fun score(board: Board, draws: Set<Int>): Int {
        return (board.nums.toSet() - draws).sum() * draws.last()
    }

    fun part1(input: List<String>): Int {
        val (drawOrder, boards) = parseInput(input)
        val draws = mutableSetOf<Int>()

        for (draw in drawOrder) {
            draws += draw

            boards
                .firstOrNull { board -> board.rowsAndCols.any(draws::containsAll) }
                ?.let { return score(it, draws) }
        }

        error("Should not reach here.")
    }

    fun part2(input: List<String>): Int {
        val (drawOrder, boards) = parseInput(input)
        val draws = mutableSetOf<Int>()
        val liveBoards = boards.toMutableSet()

        for (draw in drawOrder) {
            draws += draw

            val newlyDoneBoards = liveBoards.filter { board -> board.rowsAndCols.any(draws::containsAll) }
            liveBoards -= newlyDoneBoards.toSet()

            if (liveBoards.isEmpty()) return score(newlyDoneBoards.first(), draws)
        }

        error("Should not reach here.")
    }

    val testInput = """
        7,4,9,5,11,17,23,2,0,14,21,24,10,16,13,6,15,25,12,22,18,20,8,19,3,26,1

        22 13 17 11  0
         8  2 23  4 24
        21  9 14 16  7
         6 10  3 18  5
         1 12 20 15 19
        
         3 15  0  2 22
         9 18 13 17  5
        19  8  7 25 23
        20 11 10 24  4
        14 21 16 12  6
        
        14 21 17 24  4
        10 16 15  9 19
        18  8 23 26 20
        22 11 13  6  5
         2  0 12  3  7
    """.trimIndent().split('\n')

    check(part1(testInput) == 4512)
    check(part2(testInput) == 1924)

    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}
