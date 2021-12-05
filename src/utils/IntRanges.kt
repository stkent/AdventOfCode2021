package utils

import kotlin.math.max
import kotlin.math.min

fun intRangeSpanning(i1: Int, i2: Int): IntRange {
    return IntRange(start = min(i1, i2), endInclusive = max(i1, i2))
}
