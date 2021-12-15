import utils.extensions.putOrAdd
import utils.readInput

typealias CharPair = Pair<Char, Char>

fun main() {
    fun List<String>.parse(): Pair<List<Char>, Map<CharPair, Char>> {
        val template =
            this.first()
                .toList()

        val rules: Map<CharPair, Char> =
            buildMap {
                this@parse
                    .drop(2)
                    .forEach { rule ->
                        rule.toCharArray().let { put(it[0] to it[1], it[6]) }
                    }
            }

        return template to rules
    }

    fun Map<CharPair, Long>.performInsertions(rules: Map<CharPair, Char>): Map<CharPair, Long> {
        val result = mutableMapOf<CharPair, Long>()

        this.forEach { (oldPair, count) ->
            val newChar = rules[oldPair]

            val newPairs: List<CharPair> =
                newChar
                    ?.let { listOf(oldPair.first to it, it to oldPair.second) }
                    ?: listOf(oldPair)

            newPairs.forEach { newPair ->
                result.putOrAdd(newPair, count)
            }
        }

        return result
    }

    fun solve(input: List<String>, iterationCount: Int): Long {
        val (template, rules) = input.parse()

        var pairCounts = buildMap<CharPair, Long> {
            for (index in 0 until template.lastIndex) {
                val pair = template[index] to template[index + 1]
                putOrAdd(pair, 1)
            }
        }

        repeat(iterationCount) {
            pairCounts = pairCounts.performInsertions(rules)
        }

        val doubledCharCounts = buildMap<Char, Long> {
            for ((pair, count) in pairCounts) {
                putOrAdd(pair.first, count)
                putOrAdd(pair.second, count)
            }

            // The interior chars of the template are all double-counted.
            // The edge chars of the template are not double-counted.
            // Compensate by adding one to each edge char count.
            putOrAdd(template.first(), 1)
            putOrAdd(template.last(), 1)
        }

        val maxCharCount = doubledCharCounts.values.maxOrNull()!! / 2
        val minCharCount = doubledCharCounts.values.minOrNull()!! / 2
        return maxCharCount - minCharCount
    }

    fun part1(input: List<String>): Long {
        return solve(input, 10)
    }

    fun part2(input: List<String>): Long {
        return solve(input, 40)
    }

    val testInput = """
        NNCB

        CH -> B
        HH -> N
        CB -> H
        NH -> C
        HB -> C
        HC -> B
        HN -> C
        NN -> C
        BH -> H
        NC -> B
        NB -> B
        BN -> B
        BB -> N
        BC -> B
        CC -> N
        CN -> C
    """.trimIndent().split('\n')

    check(part1(testInput) == 1588L)
    check(part2(testInput) == 2188189693529L)

    val input = readInput("Day14")
    println(part1(input))
    println(part2(input))
}
