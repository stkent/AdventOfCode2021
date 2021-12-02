fun main() {
    fun part1(input: List<String>): Int {
        var x = 0
        var depth = 0

        for (command in input) {
            val (verb, n) = command.split(' ')
            when (verb) {
                "forward" -> x += n.toInt()
                "up" -> depth -= n.toInt()
                "down" -> depth += n.toInt()
                else -> error("Unrecognized verb.")
            }
        }

        return x * depth
    }

    fun part2(input: List<String>): Int {
        var x = 0
        var depth = 0
        var aim = 0

        for (command in input) {
            val (verb, n) = command.split(' ')
            when (verb) {
                "forward" -> {
                    depth += aim * n.toInt()
                    x += n.toInt()
                }

                "up" -> aim -= n.toInt()
                "down" -> aim += n.toInt()
                else -> error("Unrecognized verb.")
            }
        }

        return x * depth
    }

    val testInput = listOf(
        "forward 5",
        "down 5",
        "forward 8",
        "up 3",
        "down 8",
        "forward 2"
    )

    check(part1(testInput) == 150)
    check(part2(testInput) == 900)

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}
