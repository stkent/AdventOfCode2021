import utils.extensions.mode
import utils.extensions.pow
import utils.readInput

fun main() {
    fun part1(input: List<String>): Int {
        val nBits = input.first().length
        val ints = input.map { it.toInt(radix = 2) }

        val gamma = (0 until nBits)
            .map { bitNumber ->
                ints.map { (it shr bitNumber) and 1 }.mode()!!.modalValue
            }
            .withIndex()
            .fold(0) { acc, (power, bit) -> acc + (bit shl power) }

        val epsilon = (2.pow(nBits) - 1) - gamma

        return gamma * epsilon
    }

    fun part2Rating(ints: List<Int>, bitNumber: Int, mostFrequent: Boolean): Int {
        if (ints.size == 1) return ints.first()
        if (ints.isEmpty()) error("Should not reach here.")

        val retainedInts = ints
            .groupBy { (it shr bitNumber) and 1 }
            .toList()
            .sortedWith(
                compareBy(
                    { (_, numbers) -> numbers.size * (if (mostFrequent) 1 else -1) }, // Ascending or descending.
                    { (key, _) -> key * (if (mostFrequent) 1 else -1) }               // Prioritize 1s or 0s.
                )
            )
            .last()
            .second

        return part2Rating(ints = retainedInts, bitNumber = bitNumber - 1, mostFrequent)
    }

    fun part2(input: List<String>): Int {
        val nBits = input.first().length
        val ints = input.map { it.toInt(radix = 2) }

        val o2 = part2Rating(ints, bitNumber = nBits - 1, mostFrequent = true)
        val co2 = part2Rating(ints, bitNumber = nBits - 1, mostFrequent = false)

        return o2 * co2
    }

    val testInput = listOf(
        "00100",
        "11110",
        "10110",
        "10111",
        "10101",
        "01111",
        "00111",
        "11100",
        "10000",
        "11001",
        "00010",
        "01010",
    )

    check(part1(testInput) == 198)
    check(part2(testInput) == 230)

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}
