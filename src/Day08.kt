import com.google.common.collect.HashBiMap
import utils.extensions.pow
import utils.readInput

data class Entry(val inPatterns: List<Set<Char>>, val outPatterns: List<Set<Char>>)

fun main() {
    fun parseEntry(entry: String): Entry {
        return entry
            .split(" | ")
            .map { line ->
                line.split(' ')
                    .map(String::toSet)
            }
            .let { Entry(inPatterns = it[0], outPatterns = it[1]) }
    }

    fun parseInput(input: List<String>): List<Entry> {
        return input.map(::parseEntry)
    }

    fun part1(input: List<String>): Int {
        return parseInput(input)
            .flatMap(Entry::outPatterns)
            .count { outPattern -> outPattern.size in setOf(2, 3, 4, 7) }
    }

    fun buildSegmentsToDigitMap(patterns: List<Set<Char>>): Map<Set<Char>, Int> {
        val result = HashBiMap.create<Set<Char>, Int>()

        fun segmentsFor(digit: Int): Set<Char> = result.inverse()[digit]!!

        patterns
            .sortedBy { pattern -> pattern.size }
            .forEach { pattern ->
                val size = pattern.size

                val digit = when {
                    size == 2 -> 1
                    size == 3 -> 7
                    size == 4 -> 4
                    size == 5 && pattern.containsAll(segmentsFor(1)) -> 3
                    size == 5 && pattern.intersect(segmentsFor(4)).size == 3 -> 5
                    size == 5 -> 2
                    size == 6 && !pattern.containsAll(segmentsFor(1)) -> 6
                    size == 6 && pattern.intersect(segmentsFor(4)).size == 4 -> 9
                    size == 6 -> 0
                    size == 7 -> 8
                    else -> error("Should not reach here.")
                }

                result += pattern to digit
            }

        return result
    }

    fun outValue(entry: String): Int {
        val (inPatterns, outPatterns) = parseEntry(entry)

        val segmentsToDigitMap = buildSegmentsToDigitMap(inPatterns)

        return outPatterns
            .map(segmentsToDigitMap::getValue)
            .reversed()
            .foldIndexed(initial = 0) { index, acc, digit -> acc + digit * 10.pow(index) }
    }

    fun part2(input: List<String>): Int {
        return input.sumOf(::outValue)
    }

    val testInput = """
        be cfbegad cbdgef fgaecd cgeb fdcge agebfd fecdb fabcd edb | fdgacbe cefdb cefbgd gcbe
        edbfga begcd cbg gc gcadebf fbgde acbgfd abcde gfcbed gfec | fcgedb cgb dgebacf gc
        fgaebd cg bdaec gdafb agbcfd gdcbef bgcad gfac gcb cdgabef | cg cg fdcagb cbg
        fbegcd cbd adcefb dageb afcb bc aefdc ecdab fgdeca fcdbega | efabcd cedba gadfec cb
        aecbfdg fbg gf bafeg dbefa fcge gcbea fcaegb dgceab fcbdga | gecf egdcabf bgf bfgea
        fgeab ca afcebg bdacfeg cfaedg gcfdb baec bfadeg bafgc acf | gebdcfa ecba ca fadegcb
        dbcfg fgd bdegcaf fgec aegbdf ecdfab fbedc dacgb gdcebf gf | cefg dcbef fcge gbcadfe
        bdfegc cbegaf gecbf dfcage bdacg ed bedf ced adcbefg gebcd | ed bcgafe cdgba cbgef
        egadfb cdbfeg cegd fecab cgb gbdefca cg fgcdab egfdb bfceg | gbdfcae bgc cg cgb
        gcafb gcf dcaebfg ecagb gf abcdeg gaef cafbge fdbac fegbdc | fgae cfgab fg bagce
    """.trimIndent().split('\n')

    check(part1(testInput) == 26)
    check(outValue(testInput[0]) == 8394)
    check(outValue(testInput[1]) == 9781)
    check(outValue(testInput[2]) == 1197)
    check(outValue(testInput[3]) == 9361)
    check(outValue(testInput[4]) == 4873)
    check(outValue(testInput[5]) == 8418)
    check(outValue(testInput[6]) == 4548)
    check(outValue(testInput[7]) == 1625)
    check(outValue(testInput[8]) == 8717)
    check(outValue(testInput[9]) == 4315)
    check(part2(testInput) == 61229)

    val input = readInput("Day08")
    println(part1(input))
    println(part2(input))
}
