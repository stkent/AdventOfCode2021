fun main() {
    fun part1(input: Map<Int, Int>): Int {
        var turn = 1
        val positions = input.toMutableMap()
        val scores = mutableMapOf(1 to 0, 2 to 0)

        fun play() {
            val player = (turn - 1) % 2 + 1
            val move = 3 * (3 * turn - 1)
            val newPosition = (positions[player]!! + move - 1) % 10 + 1
            positions[player] = newPosition
            scores[player] = scores[player]!! + newPosition
            turn += 1
        }

        while (true) {
            play()
            if (scores.any { (_, score) -> score >= 1000 }) {
                val lowScore = scores.values.minOrNull()!!
                val rolls = 3 * (turn - 1)
                return lowScore * rolls
            }
        }
    }

    fun part2(input: Map<Int, Int>): Long {
        return 0
    }

    val testInput = mapOf(1 to 4, 2 to 8)

    check(part1(testInput) == 739785)
//    check(part2(testInput) == 444356092776315L)

    val input = mapOf(1 to 7, 2 to 1)
    println(part1(input))
}
