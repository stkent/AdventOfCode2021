import utils.readInput

fun main() {
    fun part1(input: List<String>): Int {
        return input
            .zipWithNext()
            .count { it.first < it.second }
    }

    fun part2(input: List<String>): Int {
        return input
            .asSequence()
            .map(String::toInt)
            .windowed(size = 3, step = 1, partialWindows = false)
            .map(List<Int>::sum)
            .zipWithNext()
            .count { it.first < it.second }
    }

    val testInput = """
        199
        200
        208
        210
        200
        207
        240
        269
        260
        263
    """.trimIndent().split('\n')

    check(part1(testInput) == 7)
    check(part2(testInput) == 5)

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}
