import utils.GridPoint2d
import utils.readInput

fun main() {
    fun List<String>.heightAt(point: GridPoint2d): Int {
        return this
            .getOrNull(point.y)
            ?.getOrNull(point.x)
            ?.digitToInt()
            ?: Int.MAX_VALUE
    }

    fun List<String>.findLowPoints(): Set<GridPoint2d> {
        val xMax = this[0].length - 1
        val yMax = this.size - 1

        return buildSet {
            for (x in 0..xMax) {
                for (y in 0..yMax) {
                    val point = GridPoint2d(x = x, y = y)
                    val height = heightAt(point)
                    val adjacentHeights = point.adjacentPoints().map(::heightAt)

                    if (adjacentHeights.all { it > height }) {
                        add(point)
                    }
                }
            }
        }
    }

    fun part1(input: List<String>): Int {
        return input
            .findLowPoints()
            .sumOf { lowPoint -> input.heightAt(lowPoint) + 1 }
    }

    fun List<String>.findBasinSize(lowPoint2d: GridPoint2d): Int {
        val basinPoints = mutableSetOf(lowPoint2d)
        val rimPoints = mutableSetOf<GridPoint2d>()
        val candidates = ArrayDeque(lowPoint2d.adjacentPoints())

        while (candidates.isNotEmpty()) {
            val candidate = candidates.removeFirst()

            if (heightAt(candidate) >= 9) {
                rimPoints += candidate
                continue
            }

            basinPoints += candidate
            candidates.addAll(candidate.adjacentPoints() - basinPoints - rimPoints)
        }

        return basinPoints.size
    }

    fun List<String>.findBasinSizes(): List<Int> {
        return findLowPoints().map(::findBasinSize)
    }

    fun part2(input: List<String>): Int {
        return input
            .findBasinSizes()
            .sortedDescending()
            .take(3)
            .reduce(Int::times)
    }

    val testInput = """
        2199943210
        3987894921
        9856789892
        8767896789
        9899965678
    """.trimIndent().split('\n')

    check(part1(testInput) == 15)
    check(part2(testInput) == 1134)

    val input = readInput("Day09")
    println(part1(input))
    println(part2(input))
}
