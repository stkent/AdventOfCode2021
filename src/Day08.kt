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

    val segmentsToDigitMap: Map<Set<Char>, Int> =
        mapOf(
            setOf('a', 'b', 'c', 'e', 'f', 'g') to 0,
            setOf('c', 'f') to 1,
            setOf('a', 'c', 'd', 'e', 'g') to 2,
            setOf('a', 'c', 'd', 'f', 'g') to 3,
            setOf('b', 'c', 'd', 'f') to 4,
            setOf('a', 'b', 'd', 'f', 'g') to 5,
            setOf('a', 'b', 'd', 'e', 'f', 'g') to 6,
            setOf('a', 'c', 'f') to 7,
            setOf('a', 'b', 'c', 'd', 'e', 'f', 'g') to 8,
            setOf('a', 'b', 'c', 'd', 'f', 'g') to 9,
        )

    // Returns a mapping between illuminated segments (keys) and active wires (values).
    fun buildSegmentToWireMap(inPatterns: List<Set<Char>>): Map<Char, Char> {
        return mapOf<Char, Char>()
    }

    fun outValue(entry: String): Int {
        val (inPatterns, outPatterns) = parseEntry(entry)

        val segmentToWireMap = buildSegmentToWireMap(inPatterns)

        return outPatterns
            .map { outPattern: Set<Char> ->
                outPattern
                    .map { outSegment: Char -> segmentToWireMap[outSegment]!! }
                    .toSet()
                    .let { activeWires: Set<Char> -> segmentsToDigitMap[activeWires]!! }
            }
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

    val additionalTestInputPart2 =
        "acedgfb cdfbe gcdfa fbcad dab cefabd cdfgeb eafb cagedb ab | cdfeb fcadb cdfeb cdbaf"

    check(outValue(additionalTestInputPart2) == 5353)
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
