import utils.extensions.elementCounts
import utils.readInput

fun main() {
    fun countFish(input: String, days: Int): Long {
        var fishCounts =
            input
                .split(',')
                .map(String::toInt)
                .elementCounts()
                .mapValues { (_, value) -> value.toLong() }

        repeat(days) {
            fishCounts = buildMap {
                (8 downTo 1).forEach { timer ->
                    put(timer - 1, fishCounts.getOrDefault(timer, 0))
                }

                val resets = fishCounts.getOrDefault(0, 0)
                put(6, getOrDefault(6, 0) + resets)
                put(8, resets)
            }
        }

        return fishCounts.values.sum()
    }

    fun part1(input: String): Long {
        return countFish(input, days = 80)
    }

    fun part2(input: String): Long {
        return countFish(input, days = 256)
    }

    val testInput = "3,4,3,1,2"
    check(part1(testInput) == 5934L)
    check(part2(testInput) == 26984457539)

    val input = readInput("Day06").first()
    println(part1(input))
    println(part2(input))
}
