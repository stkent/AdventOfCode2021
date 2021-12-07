import utils.readInput
import kotlin.math.abs

fun main() {
    fun calculateFuel(input: String, cost: (distance: Int) -> Int): Int {
        val positions = input.split(',').map(String::toInt)

        val min = positions.minOrNull()!!
        val max = positions.maxOrNull()!!

        return (min..max)
            .minOf { candidate -> positions.sumOf { cost(abs(candidate - it)) } }
    }

    fun part1(input: String): Int {
        return calculateFuel(input, cost = { it })
    }

    fun part2(input: String): Int {
        return calculateFuel(input, cost = { it * (it + 1) / 2 })
    }

    val testInput = "16,1,2,0,4,2,7,1,2,14"

    check(part1(testInput) == 37)
    check(part2(testInput) == 168)

    val input = readInput("Day07").first()
    println(part1(input))
    println(part2(input))
}
