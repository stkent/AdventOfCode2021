package utils.extensions

// Assumes: both IntRanges are contiguous!
fun IntRange.intersect(other: IntRange): IntRange {
    val newMin = maxOf(first, other.first)
    val newMax = minOf(last, other.last)
    return newMin..newMax
}
