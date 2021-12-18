import Packet.Literal
import Packet.Operator
import utils.readInput

private fun String.toBits(): String {
    return this
        .toCharArray()
        .joinToString("") { char ->
            when (char) {
                '0' -> "0000"
                '1' -> "0001"
                '2' -> "0010"
                '3' -> "0011"
                '4' -> "0100"
                '5' -> "0101"
                '6' -> "0110"
                '7' -> "0111"
                '8' -> "1000"
                '9' -> "1001"
                'A' -> "1010"
                'B' -> "1011"
                'C' -> "1100"
                'D' -> "1101"
                'E' -> "1110"
                'F' -> "1111"
                else -> error("Should not reach here.")
            }
        }
}

data class PacketParseResult<T : Packet>(val packet: T, val remainder: String)

sealed class Packet(open val version: Int) {
    data class Literal(override val version: Int, val value: Long) : Packet(version) {
        companion object {
            private const val bitsPerDigit = 5

            fun from(version: Int, contentBits: String): PacketParseResult<Literal> {
                var bitIndex = 0

                val value =
                    buildString {
                        while (true) {
                            val isLastDigit = contentBits[bitIndex] == '0'
                            append(contentBits.substring(bitIndex + 1 until bitIndex + bitsPerDigit))
                            bitIndex += bitsPerDigit
                            if (isLastDigit) break
                        }
                    }.toLong(radix = 2)

                val packet = Literal(version = version, value = value)
                val remainder = contentBits.substring(bitIndex)
                return PacketParseResult(packet, remainder)
            }
        }
    }

    data class Operator(override val version: Int, val typeId: Int, val packets: List<Packet>) : Packet(version) {
        companion object {
            fun from(version: Int, typeId: Int, contentBits: String): PacketParseResult<Operator> {
                val lengthTypeId = contentBits[0]
                var remainingBits = ""

                val subPackets = buildList {
                    when (lengthTypeId) {
                        '0' -> {
                            var remainingSubPacketsBitsCount = contentBits.substring(1 until 16).toLong(radix = 2)
                            remainingBits = contentBits.substring(16)

                            while (remainingSubPacketsBitsCount > 0) {
                                val (packet, remainder) = parseFirst(remainingBits)
                                add(packet)
                                remainingSubPacketsBitsCount -= (remainingBits.length - remainder.length)
                                remainingBits = remainder
                            }
                        }

                        '1' -> {
                            var remainingSubPacketCount = contentBits.substring(1 until 12).toLong(radix = 2)
                            remainingBits = contentBits.substring(12)

                            while (remainingSubPacketCount > 0) {
                                val (packet, remainder) = parseFirst(remainingBits)
                                add(packet)
                                remainingBits = remainder
                                remainingSubPacketCount -= 1
                            }
                        }

                        else -> error("Should not reach here.")
                    }
                }

                val packet = Operator(version = version, typeId = typeId, packets = subPackets)
                val remainder = remainingBits
                return PacketParseResult(packet, remainder)
            }
        }
    }

    companion object {
        fun parseFirst(bits: String): PacketParseResult<out Packet> {
            val version = bits.substring(0 until 3).toInt(radix = 2)
            val typeId = bits.substring(3 until 6).toInt(radix = 2)
            val contentBits = bits.substring(6..bits.lastIndex)

            return when (typeId) {
                4 -> Literal.from(
                    version = version,
                    contentBits = contentBits
                )

                else -> Operator.from(
                    version = version,
                    typeId = typeId,
                    contentBits = contentBits
                )
            }
        }
    }
}

fun main() {
    fun Packet.versionSum(): Int {
        return when (this) {
            is Literal -> version
            is Operator -> version + packets.sumOf(Packet::versionSum)
        }
    }

    fun part1(input: String): Int {
        val (packet, _) = Packet.parseFirst(input.toBits())
        return packet.versionSum()
    }

    fun Packet.resolve(): Long {
        return when (this) {
            is Literal -> value
            is Operator -> when (typeId) {
                0 -> packets.sumOf { packet -> packet.resolve() }
                1 -> packets.fold(1) { acc, packet -> acc * packet.resolve() }
                2 -> packets.minOf { packet -> packet.resolve() }
                3 -> packets.maxOf { packet -> packet.resolve() }
                5 -> if (packets[0].resolve() > packets[1].resolve()) 1 else 0
                6 -> if (packets[0].resolve() < packets[1].resolve()) 1 else 0
                7 -> if (packets[0].resolve() == packets[1].resolve()) 1 else 0
                else -> error("Should not reach here.")
            }
        }
    }

    fun part2(input: String): Long {
        val (packet, _) = Packet.parseFirst(input.toBits())
        return packet.resolve()
    }

    val testInput0 = "D2FE28"
    val testInput1 = "38006F45291200"
    val testInput2 = "EE00D40C823060"
    val testInput3 = "8A004A801A8002F478"
    val testInput4 = "620080001611562C8802118E34"
    val testInput5 = "C0015000016115A2E0802F182340"
    val testInput6 = "A0016C880162017C3686B18A3D4780"

    check(Packet.parseFirst(testInput0.toBits()).packet == Literal(version = 6, value = 2021))

    check(
        Packet.parseFirst(testInput1.toBits()).packet ==
            Operator(
                version = 1,
                typeId = 6,
                packets = listOf(
                    Literal(version = 6, value = 10),
                    Literal(version = 2, value = 20),
                )
            )
    )

    check(
        Packet.parseFirst(testInput2.toBits()).packet ==
            Operator(
                version = 7,
                typeId = 3,
                packets = listOf(
                    Literal(version = 2, value = 1),
                    Literal(version = 4, value = 2),
                    Literal(version = 1, value = 3),
                )
            )
    )

    check(part1(testInput3) == 16)
    check(part1(testInput4) == 12)
    check(part1(testInput5) == 23)
    check(part1(testInput6) == 31)

    val testInput7 = "C200B40A82"
    val testInput8 = "04005AC33890"
    val testInput9 = "880086C3E88112"
    val testInput10 = "CE00C43D881120"
    val testInput11 = "D8005AC2A8F0"
    val testInput12 = "F600BC2D8F"
    val testInput13 = "9C005AC2F8F0"
    val testInput14 = "9C0141080250320F1802104A08"

    check(part2(testInput7) == 3L)
    check(part2(testInput8) == 54L)
    check(part2(testInput9) == 7L)
    check(part2(testInput10) == 9L)
    check(part2(testInput11) == 1L)
    check(part2(testInput12) == 0L)
    check(part2(testInput13) == 0L)
    check(part2(testInput14) == 1L)

    val input = readInput("Day16").first()
    println(part1(input))
    println(part2(input))
}
