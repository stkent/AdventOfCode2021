import utils.GridPoint2d
import utils.bounds
import utils.extensions.collapseToString
import utils.readInput

private const val LIT = '#'
private const val UNLIT = '.'

fun main() {
    fun List<String>.litPixels(): Map<GridPoint2d, Boolean> {
        fun charAt(point: GridPoint2d): Char? {
            return this
                .getOrNull(point.y)
                ?.getOrNull(point.x)
        }

        return buildMap {
            for (y in this@litPixels.indices) {
                for (x in this@litPixels[0].indices) {
                    val point = GridPoint2d(x, y)
                    put(point, charAt(point) == LIT)
                }
            }
        }
    }

    fun List<String>.parse(): Pair<String, Map<GridPoint2d, Boolean>> {
        val algorithm = this.first()
        val image = this.drop(2).litPixels()
        return algorithm to image
    }

    algorithm@ fun String.enhance(image: Map<GridPoint2d, Boolean>, iteration: Int): Map<GridPoint2d, Boolean> {
        val oldBounds = image.keys.bounds()
        val xMin = oldBounds.xMin - 1
        val xMax = oldBounds.xMax + 1
        val yMin = oldBounds.yMin - 1
        val yMax = oldBounds.yMax + 1

        val outOfBoundsPixelsAreLit =
            when {
                // Never turned on:
                first() == UNLIT -> false
                // Turned on during the first enhancement, and never turned off:
                first() == LIT && last() == LIT -> iteration > 1
                // Turned on during off-numbered enhancements, and off during even-numbered enhancements:
                first() == LIT && last() == UNLIT -> iteration % 2 == 0
                else -> error("Should not reach here.")
            }

        return buildMap {
            for (x in xMin..xMax) {
                for (y in yMin..yMax) {
                    val centerPoint = GridPoint2d(x, y)

                    val inputPoints =
                        buildSet {
                            add(centerPoint)
                            addAll(centerPoint.surroundingPoints())
                        }

                    val index =
                        inputPoints
                            .sortedWith(compareBy(GridPoint2d::y).thenBy(GridPoint2d::x))
                            .map { point ->
                                when (point) {
                                    in image -> if (image[point]!!) '1' else '0'
                                    else -> if (outOfBoundsPixelsAreLit) '1' else '0'
                                }
                            }
                            .collapseToString()
                            .toInt(radix = 2)

                    put(centerPoint, this@algorithm[index] == LIT)
                }
            }
        }
    }

    fun List<String>.litPixelCount(enhancementCount: Int): Int {
        val (algorithm, image) = this.parse()

        return (1..enhancementCount)
            .fold(image) { outputImage, iteration ->
                algorithm.enhance(outputImage, iteration)
            }
            .values.count { it }
    }

    fun part1(input: List<String>): Int {
        return input.litPixelCount(enhancementCount = 2)
    }

    fun part2(input: List<String>): Int {
        return input.litPixelCount(enhancementCount = 50)
    }

    val testInput = """
        ..#.#..#####.#.#.#.###.##.....###.##.#..###.####..#####..#....#..#..##..###..######.###...####..#..#####..##..#.#####...##.#.#..#.##..#.#......#.###.######.###.####...#.##.##..#..#..#####.....#.#....###..#.##......#.....#..#..#..##..#...##.######.####.####.#.#...#.......#..#.#.#...####.##.#......#..#...##.#.##..#...##.#.##..###.#......#.#.......#.#.#.####.###.##...#.....####.#..#..#.##.#....##..#.####....##...##..#...#......#.#.......#.......##..####..#...#.#.#...##..#.#..###..#####........#..####......#..#
        
        #..#.
        #....
        ##..#
        ..#..
        ..###
    """.trimIndent().split('\n')

    check(part1(testInput) == 35)
    check(part2(testInput) == 3351)

    val input = readInput("Day20")
    println(part1(input))
    println(part2(input))
}
