import utils.GridPoint2d
import utils.bounds
import utils.readInput

private sealed class Fold {
    class X(val x: Int) : Fold() {
        override fun toString() = "Fold(x = $x)"
    }

    class Y(val y: Int) : Fold() {
        override fun toString() = "Fold(y = $y)"
    }
}

fun main() {
    fun List<String>.parse(): Pair<Set<GridPoint2d>, List<Fold>> {
        val dots = mutableSetOf<GridPoint2d>()
        val folds = mutableListOf<Fold>()

        for (line in this.filter(String::isNotEmpty)) {
            when {
                line.startsWith("fold along x=") -> folds += Fold.X(x = line.drop(13).toInt())
                line.startsWith("fold along y=") -> folds += Fold.Y(y = line.drop(13).toInt())
                else -> dots += line.split(',').let { (x, y) -> GridPoint2d(x.toInt(), y.toInt()) }
            }
        }

        return dots to folds
    }

    fun Set<GridPoint2d>.apply(fold: Fold): Set<GridPoint2d> {
        return buildSet {
            for (dot in this@apply) {
                add(
                    when (fold) {
                        is Fold.X -> GridPoint2d(dot.x - 2 * (dot.x - fold.x).coerceAtLeast(0), dot.y)
                        is Fold.Y -> GridPoint2d(dot.x, dot.y - 2 * (dot.y - fold.y).coerceAtLeast(0))
                    }
                )
            }
        }
    }

    fun part1(input: List<String>): Int {
        val (dots, folds) = input.parse()
        var visibleDots = dots

        visibleDots = visibleDots.apply(folds.first())
        return visibleDots.size
    }

    fun part2(input: List<String>): String {
        val (dots, folds) = input.parse()
        var visibleDots = dots

        for (fold in folds) {
            visibleDots = visibleDots.apply(fold)
        }

        val bounds = visibleDots.bounds()
        return buildString {
            for (y in bounds.yMin..bounds.yMax) {
                for (x in bounds.xMin..bounds.xMax) {
                    append(if (GridPoint2d(x, y) in visibleDots) '#' else '.')
                }

                append('\n')
            }
        }
    }

    val testInput = """
        6,10
        0,14
        9,10
        0,3
        10,4
        4,11
        6,0
        6,12
        4,1
        0,13
        10,12
        3,4
        3,0
        8,4
        1,10
        2,14
        8,10
        9,0
        
        fold along y=7
        fold along x=5
    """.trimIndent().split('\n')

    check(part1(testInput) == 17)
    check(part2(testInput) == """
        #####
        #...#
        #...#
        #...#
        #####

    """.trimIndent())

    val input = readInput("Day13")
    println(part1(input))
    println(part2(input))
}
