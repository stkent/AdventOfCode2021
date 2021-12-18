@file:Suppress("SpellCheckingInspection")

import SnailfishNumber.Component
import utils.readInput

private data class SnailfishNumber(val pair: Component.Purr) {

    sealed class Component {

        abstract fun split(): Component

        data class Regular(val value: Int) : Component() {
            override fun toString() = "$value"

            override fun split(): Component {
                if (value < 10) return this

                val firstValue = value.floorDiv(2)
                return Purr(
                    first = Regular(firstValue),
                    second = Regular(value - firstValue)
                )
            }
        }

        data class Purr(val first: Component, val second: Component) : Component() {
            operator fun plus(other: Purr) = Purr(first = this, second = other)
            override fun toString() = "[$first,$second]"

            fun reduce(): Purr {
                var result = this

                while (true) {
                    val explodedPurr = result.explode()
                    if (explodedPurr != result) {
                        result = explodedPurr
                        continue
                    }

                    val splitPurr = result.split()
                    if (splitPurr != result) {
                        result = splitPurr
                        continue
                    }

                    break
                }

                return result
            }

            override fun split(): Purr {
                val splitFirst = first.split()
                if (splitFirst != first) {
                    return Purr(splitFirst, second)
                }

                return Purr(first, second.split())
            }

            private fun explode(): Purr {
                // TODO: use a stack?
                // TODO: implement
                return this
            }

        }
    }
}

fun main() {
    fun String.toSnailfishNumber(): SnailfishNumber {
        // TODO: implement
        return SnailfishNumber(Component.Purr(Component.Regular(0), Component.Regular(0)))
    }

    fun part1(input: List<String>): Int {
        return 0
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    val testInput = """
        [1,1]
        [2,2]
        [3,3]
        [4,4]
    """.trimIndent().split('\n')

    val myTest = Component.Purr(Component.Purr(Component.Regular(1), Component.Regular(10)), Component.Regular(10))
    val myTestSplit = myTest.split()
    val myTestSplit2 = myTestSplit.split()
    val myTestReduced = myTest.reduce()

    check(part1(testInput) == 4140)

    val input = readInput("Day18")
    println(part1(input))
}
