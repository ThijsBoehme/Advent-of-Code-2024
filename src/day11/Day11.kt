package day11

import println
import readInput
import kotlin.math.floor
import kotlin.math.log
import kotlin.math.pow

fun main() {
    fun part1(input: String, blinks: Int): Int {
        return input.split(" ")
            .map { it.toLong() }
            .sumOf {
                var stones = listOf(it)
                repeat(blinks) {
                    stones = stones.flatMap { stone ->
                        val numberOfDigits = floor(log(stone.toDouble(), 10.0) + 1).toInt()
                        when {
                            stone == 0L -> listOf(1)
                            numberOfDigits % 2 == 0 -> listOf(
                                floor(stone / (10.0.pow(numberOfDigits / 2.0))).toLong(),
                                (stone % (10.0.pow(numberOfDigits / 2.0))).toLong(),
                            )

                            else -> listOf(stone * 2024)
                        }
                    }
                }
                stones.size
            }
    }

    fun part2(input: String, blinks: Int): Long {
        var stones: Map<Long, Long> = input.split(" ")
            .map { it.toLong() }
            .groupingBy { it }
            .eachCount()
            .mapValues { it.value.toLong() }

        repeat(blinks) {
            val nextStones = buildMap<Long, Long> {
                stones.forEach { (stone, amount) ->
                    val numberOfDigits = floor(log(stone.toDouble(), 10.0) + 1).toInt()
                    when {
                        stone == 0L -> put(1L, getOrDefault(1L, 0L) + amount)

                        numberOfDigits % 2 == 0 -> {
                            val upperHalf = floor(stone / (10.0.pow(numberOfDigits / 2.0))).toLong()
                            put(upperHalf, getOrDefault(upperHalf, 0L) + amount)

                            val lowerHalf = stone % (10.0.pow(numberOfDigits / 2.0)).toLong()
                            put(lowerHalf, getOrDefault(lowerHalf, 0L) + amount)
                        }

                        else -> put(stone * 2024L, getOrDefault(stone * 2024L, 0L) + amount)
                    }
                }
            }
            stones = nextStones
        }

        return stones.values.sum()
    }

    check(part1("0 1 10 99 999", 1) == 7)
    check(part1("125 17", 6) == 22)
    check(part1("125 17", 25) == 55312)
    check(part2("0 1 10 99 999", 1) == 7L)
    check(part2("125 17", 6) == 22L)
    check(part2("125 17", 25) == 55312L)

    val input = readInput("day11/Day11").first()
    part1(input, 25).println()
    part2(input, 75).println()
}
