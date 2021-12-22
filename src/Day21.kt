import kotlin.math.max
import kotlin.math.min

private class PlayerState(val place: Long, val score: Long) {
    fun move(distance: Long): PlayerState {
        val newPlace = (place + distance - 1) % 10 + 1
        val newScore = score + newPlace
        return PlayerState(place = newPlace, score = newScore)
    }
}

private data class GameState(val p1: PlayerState, val p2: PlayerState, val p1Turn: Boolean) {
    val maxScore = max(p1.score, p2.score)
    val minScore = min(p1.score, p2.score)

    fun applyMove(moveDistance: Long): GameState {
        return GameState(
            p1 = if (p1Turn) p1.move(moveDistance) else p1,
            p2 = if (p1Turn) p2 else p2.move(moveDistance),
            p1Turn = !p1Turn
        )
    }

    companion object {
        fun initial(places: Pair<Long, Long>): GameState {
            return GameState(
                p1 = PlayerState(place = places.first, score = 0),
                p2 = PlayerState(place = places.second, score = 0),
                p1Turn = true
            )
        }
    }
}

private class DeterministicDie {
    var rollCount: Long = 0
        private set

    fun sumOfNextThreeRolls(): Long {
        val result = 3 * (rollCount + 2)
        rollCount += 3
        return result
    }
}

private class Wins(val p1: Long, val p2: Long) {
    operator fun plus(other: Wins) = Wins(p1 + other.p1, p2 + other.p2)
}

private operator fun Long.times(wins: Wins) = Wins(this * wins.p1, this * wins.p2)

fun main() {
    fun part1(input: Pair<Long, Long>): Long {
        var state = GameState.initial(places = input)
        val die = DeterministicDie()

        while (state.maxScore < 1000) {
            val moveDistance = die.sumOfNextThreeRolls()
            state = state.applyMove(moveDistance)
        }

        return state.minScore * die.rollCount
    }

    val quantumDieDistanceFrequencies =
        mapOf(
            3L to 1L,
            4L to 3L,
            5L to 6L,
            6L to 7L,
            7L to 6L,
            8L to 3L,
            9L to 1L
        )

    fun part2Wins(state: GameState, cache: MutableMap<GameState, Wins>): Wins {
        return when {
            state.maxScore >= 21 ->
                if (state.p1.score > state.p2.score) Wins(1, 0) else Wins(0, 1)

            state in cache -> cache[state]!!

            else -> {
                quantumDieDistanceFrequencies
                    .map { (distance, frequency) ->
                        frequency * part2Wins(state.applyMove(distance), cache)
                    }
                    .reduce(Wins::plus)
                    .also { wins -> cache[state] = wins }
            }
        }
    }

    fun part2(input: Pair<Long, Long>): Long {
        val cache = mutableMapOf<GameState, Wins>()
        val wins = part2Wins(GameState.initial(places = input), cache)
        return max(wins.p1, wins.p2)
    }

    val testInput = 4L to 8L
    check(part1(testInput) == 739785L)
    check(part2(testInput) == 444356092776315L)

    val input = 7L to 1L
    println(part1(input))
    println(part2(input))
}
