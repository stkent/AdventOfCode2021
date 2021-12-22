import utils.GridBounds2d
import utils.GridPoint2d
import utils.GridVector2d
import kotlin.math.sign

private data class ProbeState(val position: GridPoint2d, val velocity: GridVector2d)

fun main() {
    fun ProbeState.next(): ProbeState {
        return copy(
            position = position.copy(
                x = position.x + velocity.x,
                y = position.y + velocity.y,
            ),
            velocity = velocity.copy(
                x = velocity.x - velocity.x.sign,
                y = velocity.y - 1
            )
        )
    }

    fun ProbeState.trajectoryToTarget(target: GridBounds2d): List<ProbeState>? {
        val trajectory =
            generateSequence(this, ProbeState::next)
                .takeWhile { (position, _) ->
                    position.x <= target.xMax && position.y >= target.yMin
                }
                .toList()

        if (trajectory.none { (position, _) -> position in target }) return null
        return trajectory
    }

    fun GridBounds2d.validTrajectories(): List<List<ProbeState>> {
        // Assuming xMin > 0:
        val validInitialVx =
            generateSequence(1, Int::inc)
                .dropWhile { vx -> vx * (vx + 1) / 2 < this.xMin } // Drops Vx that eventually fall short.
                .takeWhile { vx -> vx <= this.xMax } // Drops Vx that immediately overshoot.
                .toList()

        // Max initial Vy of 1000 works but is not fully general ¯\_(ツ)_/¯:
        val validInitialVy = (yMin..1000).toList()

        val result = buildList {
            for (initialVx in validInitialVx) {
                for (initialVy in validInitialVy) {
                    val initialState =
                        ProbeState(
                            position = GridPoint2d.origin,
                            velocity = GridVector2d(x = initialVx, y = initialVy)
                        )

                    initialState
                        .trajectoryToTarget(target = this@validTrajectories)
                        ?.run(::add)
                }
            }
        }

        return result
    }

    fun part1(input: GridBounds2d): Int {
        return input
            .validTrajectories()
            .flatten()
            .maxOf { (position, _) -> position.y }
    }

    fun part2(input: GridBounds2d): Int {
        return input
            .validTrajectories()
            .count()
    }

    val testInput = GridBounds2d(xMin = 20, xMax = 30, yMin = -10, yMax = -5)
    check(part1(testInput) == 45)
    check(part2(testInput) == 112)

    val input = GridBounds2d(xMin = 150, xMax = 193, yMin = -136, yMax = -86)
    println(part1(input))
    println(part2(input))
}
