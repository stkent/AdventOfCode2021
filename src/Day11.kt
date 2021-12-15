import utils.GridPoint2d
import utils.extensions.elementCounts
import utils.readInput

private typealias EnergyMap = Map<GridPoint2d, Int>

fun main() {
    fun List<String>.toMap(): EnergyMap {
        fun energyAt(point: GridPoint2d): Int {
            return this
                .getOrNull(point.y)
                ?.getOrNull(point.x)
                ?.digitToInt()!!
        }

        return buildMap {
            for (y in this@toMap.indices) {
                for (x in this@toMap[0].indices) {
                    val point = GridPoint2d(x, y)
                    put(point, energyAt(point))
                }
            }
        }
    }

    fun EnergyMap.step(): EnergyMap {
        val flashEnergy = 10
        val flashed = mutableSetOf<GridPoint2d>()
        var liveMap = this.mapValues { (_, energy) -> energy + 1 }

        while (true) {
            val newFlashers =
                liveMap
                    .filterValues { energy -> energy >= flashEnergy }
                    .keys
                    .minus(flashed)

            if (newFlashers.isEmpty()) break

            val energyIncreases =
                newFlashers
                    .flatMap(GridPoint2d::surroundingPoints)
                    .elementCounts()

            liveMap = liveMap.mapValues { (point, energy) ->
                energy + energyIncreases.getOrDefault(point, 0)
            }

            flashed.addAll(newFlashers)
        }

        return liveMap.mapValues { (_, energy) -> if (energy >= flashEnergy) 0 else energy }
    }

    fun flasherCount(map: EnergyMap): Int {
        return map.values.count { it == 0 }
    }

    fun energyMaps(input: List<String>): Sequence<EnergyMap> {
        return generateSequence(input.toMap(), { map -> map.step() })
    }

    fun part1(input: List<String>, steps: Int): Int {
        return energyMaps(input)
            .take(steps + 1)
            .sumOf(::flasherCount)
    }

    fun part2(input: List<String>): Int {
        return energyMaps(input)
            .indexOfFirst { map -> map.values.all { energy -> energy == 0 } }
    }

    val testInput1 = """
        11111
        19991
        19191
        19991
        11111
    """.trimIndent().split('\n')

    val testInput2 = """
        5483143223
        2745854711
        5264556173
        6141336146
        6357385478
        4167524645
        2176841721
        6882881134
        4846848554
        5283751526
    """.trimIndent().split('\n')

    check(
        testInput1.toMap().step() == """
        34543
        40004
        50005
        40004
        34543
    """.trimIndent().split('\n').toMap()
    )

    check(
        testInput1.toMap().step().step() == """
        45654
        51115
        61116
        51115
        45654
    """.trimIndent().split('\n').toMap()
    )

    check(part1(testInput2, 10) == 204)
    check(part1(testInput2, 100) == 1656)
    check(part2(testInput2) == 195)

    val input = readInput("Day11")
    println(part1(input, 100))
    println(part2(input))
}
