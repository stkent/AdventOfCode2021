@file:Suppress("SpellCheckingInspection", "LocalVariableName")

import SnailfishNumber.Paired
import SnailfishNumber.Regular
import utils.readInput
import utils.treeDepthFirstSearch
import kotlin.math.max

private sealed class SnailfishNumber(var parent: Paired? = null) {

    abstract val magnitude: Int

    class Regular(var value: Int) : SnailfishNumber() {

        override val magnitude: Int
            get() = value

        override fun toString() = "$value"

        fun split() {
            // Create a Paired number to replace this Regular number:
            val left = this.value.floorDiv(2)
            val right = this.value - left
            val replacement = Paired(left = Regular(left), right = Regular(right))

            // {Remove this from} and {add replacement to} the tree:
            val parent = this.parent ?: return

            replacement.parent = parent

            if (parent.left == this) {
                parent.left = replacement
            } else {
                parent.right = replacement
            }
        }

    }

    class Paired(var left: SnailfishNumber, var right: SnailfishNumber) : SnailfishNumber() {

        override val magnitude: Int
            get() = 3 * left.magnitude + 2 * right.magnitude

        init {
            left.parent = this
            right.parent = this
        }

        override fun toString() = "[$left,$right]"

        operator fun plus(other: Paired): Paired {
            val result = Paired(left = this, right = other)
            result.reduce()
            return result
        }

        private fun reduce() {
            while (true) {
                val pairedToExplode = findPairedToExplode(this)
                if (pairedToExplode != null) {
                    pairedToExplode.explode()
                    continue
                }

                val regularToSplit = findRegularToSplit(this)
                if (regularToSplit != null) {
                    regularToSplit.split()
                    continue
                }

                break
            }
        }

        private fun findPairedToExplode(root: Paired): Paired? {
            return treeDepthFirstSearch<Pair<SnailfishNumber, Int>>(
                root = root to 0,
                children = {
                    val (number, depth) = this
                    val newDepth = depth + 1
                    when (number) {
                        is Paired -> listOf(number.left to newDepth, number.right to newDepth)
                        is Regular -> emptyList()
                    }
                },
                predicate = { (number, depth) ->
                    number is Paired && depth >= 4
                }
            )?.first as Paired?
        }

        private fun findRegularToSplit(root: Paired): Regular? {
            return treeDepthFirstSearch<SnailfishNumber>(
                root = root,
                children = {
                    when (this) {
                        is Paired -> listOf(this.left, this.right)
                        is Regular -> emptyList()
                    }
                },
                predicate = { number ->
                    number is Regular && number.value >= 10
                }
            ) as Regular?
        }

        private fun explode() {
            // Push exploding pair values to the left and right:
            oneSidedExplode(isLeft = true)
            oneSidedExplode(isLeft = false)

            // Create a Regular number to replace this Paired number:
            val replacement = Regular(value = 0)

            // {Remove this from} and {add replacement to} the tree:
            val parent = this.parent ?: return

            replacement.parent = parent

            if (parent.left == this) {
                parent.left = replacement
            } else {
                parent.right = replacement
            }
        }

        private fun oneSidedExplode(isLeft: Boolean) {
            val _left = if (isLeft) Paired::left else Paired::right
            val _right = if (isLeft) Paired::right else Paired::left
            val int = (_left.get(this) as Regular).value

            val stack = ArrayDeque<Pair<SnailfishNumber, Boolean>>()
            val seen = mutableSetOf<SnailfishNumber>(this)
            stack.add(parent!! to false)

            while (stack.isNotEmpty()) {
                val (number, hasGoneLeft) = stack.removeLast()

                when (number) {
                    is Paired -> {
                        // Add new numbers to explore (last-added is first-explored):
                        number.parent?.let { stack.addLast(it to hasGoneLeft) }
                        if (_left(number) !in seen) stack.addLast(_left(number) to true)
                        if (hasGoneLeft && _right(number) !in seen) stack.addLast(_right(number) to true)

                        seen += number
                    }

                    is Regular -> {
                        number.value = number.value + int
                        return
                    }
                }
            }
        }

    }
}

fun main() {
    fun String.toSnailfishNumber(): Paired {
        val stack = ArrayDeque<SnailfishNumber>()

        for (char in this) {
            when {
                char.isDigit() -> stack.addLast(Regular(value = char.digitToInt()))
                char == ']' -> {
                    val right = stack.removeLast()
                    val left = stack.removeLast()
                    stack.addLast(Paired(left = left, right = right))
                }
            }
        }

        return stack.first() as Paired
    }

    fun part1(input: List<String>): Int {
        return input
            .map(String::toSnailfishNumber)
            .reduce(Paired::plus)
            .magnitude
    }

    fun part2(input: List<String>): Int {
        var result = Int.MIN_VALUE

        for (i in input.indices) {
            for (j in input.indices) {
                if (i == j) continue
                val first = input[i].toSnailfishNumber()
                val second = input[j].toSnailfishNumber()
                result = max(result, (first + second).magnitude)
            }
        }

        return result
    }

    val testInput = """
        [[[0,[5,8]],[[1,7],[9,6]]],[[4,[1,2]],[[1,4],2]]]
        [[[5,[2,8]],4],[5,[[9,9],0]]]
        [6,[[[6,2],[5,6]],[[7,6],[4,7]]]]
        [[[6,[0,7]],[0,9]],[4,[9,[9,0]]]]
        [[[7,[6,4]],[3,[1,3]]],[[[5,5],1],9]]
        [[6,[[7,3],[3,2]]],[[[3,8],[5,7]],4]]
        [[[[5,4],[7,7]],8],[[8,3],8]]
        [[9,3],[[9,9],[6,[4,9]]]]
        [[2,[[7,7],7]],[[5,8],[[9,3],[0,2]]]]
        [[[[5,2],5],[8,[3,7]]],[[5,[7,5]],[4,4]]]
    """.trimIndent().split('\n')

    check(part1(testInput) == 4140)
    check(part2(testInput) == 3993)

    val input = readInput("Day18")
    println(part1(input))
    println(part2(input))
}
