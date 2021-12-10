import LineType.Corrupted
import LineType.Incomplete
import com.google.common.collect.HashBiMap
import utils.readInput

private sealed class LineType {
    class Incomplete(val missingChars: List<Char>) : LineType()
    class Corrupted(val firstIncorrectChar: Char) : LineType()
}

fun main() {
    val charPairs = HashBiMap.create(
        mapOf(
            '(' to ')',
            '[' to ']',
            '{' to '}',
            '<' to '>',
        )
    )

    fun typeFor(line: String): LineType {
        val charStack = ArrayDeque<Char>()

        for (char in line) {
            when (char) {
                in charPairs.keys -> charStack.addLast(char)
                in charPairs.values -> {
                    if (charStack.lastOrNull() == charPairs.inverse()[char]!!) {
                        charStack.removeLast()
                    } else {
                        return Corrupted(firstIncorrectChar = char)
                    }
                }

                else -> error("Should not reach here.")
            }
        }

        return Incomplete(missingChars = charStack.reversed().map { charPairs[it]!! })
    }

    fun part1(input: List<String>): Long {
        return input
            .map(::typeFor)
            .filterIsInstance<Corrupted>()
            .sumOf { corruptedLine ->
                when (corruptedLine.firstIncorrectChar) {
                    ')' -> 3L
                    ']' -> 57L
                    '}' -> 1197L
                    '>' -> 25137L
                    else -> error("Should not reach here.")
                }
            }
    }

    fun part2(input: List<String>): Long {
        val orderedScores = input
            .map(::typeFor)
            .filterIsInstance<Incomplete>()
            .map { incompleteLine ->
                incompleteLine.missingChars
                    .fold(0L) { acc, char ->
                        5 * acc + when (char) {
                            ')' -> 1L
                            ']' -> 2L
                            '}' -> 3L
                            '>' -> 4L
                            else -> error("Should not reach here.")
                        }
                    }
            }
            .sorted()

        val middleIndex = (orderedScores.size - 1) / 2
        val middleScore = orderedScores[middleIndex]
        return middleScore
    }

    val testInput = """
        [({(<(())[]>[[{[]{<()<>>
        [(()[<>])]({[<{<<[]>>(
        {([(<{}[<>[]}>{[]{[(<()>
        (((({<>}<{<{<>}{[]{[]{}
        [[<[([]))<([[{}[[()]]]
        [{[{({}]{}}([{[{{{}}([]
        {<[[]]>}<{[{[{[]{()[[[]
        [<(<(<(<{}))><([]([]()
        <{([([[(<>()){}]>(<<{{
        <{([{{}}[<[[[<>{}]]]>[]]
    """.trimIndent().split('\n')

    check(part1(testInput) == 26397L)
    check(part2(testInput) == 288957L)

    val input = readInput("Day10")
    println(part1(input))
    println(part2(input))
}
