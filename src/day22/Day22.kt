package day22

import println
import readInput

fun main() {
    val evolutions = mutableMapOf<Long, Long>()

    fun generateSecrets(input: String): List<Long> {
        return buildList {
            var secret = input.toLong()
            repeat(2000) {
                secret = evolutions.getOrPut(secret) {
                    val mul = secret * 64
                    if (mul < 0) println("Negative number")
                    secret = mul xor secret
                    secret %= 16777216

                    val div = secret / 32
                    secret = div xor secret
                    secret %= 16777216

                    val mul2 = secret * 2048
                    if (mul2 < 0) println("Negative number")
                    secret = mul2 xor secret
                    secret %= 16777216

                    secret
                }
                add(secret)
            }
        }
    }

    fun part1(input: List<String>): Long {
        return input.map(::generateSecrets)
            .sumOf { it.last() }
    }

    fun part2(input: List<String>): Int {
        val priceLists = input.map(::generateSecrets)
            .map { sequence ->
                sequence.map { it % 10 }
                    .map(Long::toInt)
            }

        val differencesList = priceLists.map { priceList ->
            priceList.zipWithNext()
                .map { it.second - it.first }
        }

        val sequences = differencesList.flatMap { differences ->
            differences.windowed(4, 1)
        }.toSet()

        var maxSum = 0
        return sequences.maxOf { sequence ->
            val indices = differencesList.map { differenceList ->
                differenceList.windowed(4, 1).indexOfFirst { it == sequence }
            }

            val sum = priceLists.zip(indices.map { it })
                .sumOf { (priceList, index) ->
                    if (index == -1) 0
                    else priceList.getOrNull(index + 3) ?: 0
                }

            if (sum > maxSum) maxSum = sum.also { println("New max sum: $it") }
            sum
        }
    }

    val testInput = readInput("day22/Day22_test")
    check(part1(testInput) == 37327623L)

    val input = readInput("day22/Day22")
    part1(input).println()
    part2(input).println()
}
