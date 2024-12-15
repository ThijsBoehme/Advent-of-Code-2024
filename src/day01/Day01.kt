package day01

import println
import readInput
import kotlin.math.abs

fun main() {
    fun parse(input: List<String>): Pair<List<Int>, List<Int>> {
        val left = mutableListOf<Int>()
        val right = mutableListOf<Int>()

        input.forEach {
            val (a, b) = it.split("\\s+".toRegex())
            left.add(a.toInt())
            right.add(b.toInt())
        }
        return Pair(left, right)
    }

    fun part1(input: List<String>): Int {
        val (left, right) = parse(input)

        return left.sorted()
            .zip(right.sorted())
            .sumOf { (a, b) -> abs(a - b) }
    }

    fun part2(input: List<String>): Int {
        val (left, right) = parse(input)

        return left.sumOf { a ->
            a * right.count { it == a }
        }
    }

    val testInput = readInput("day01/Day01_test")
    check(part1(testInput) == 11)
    check(part2(testInput) == 31)

    val input = readInput("day01/Day01")
    part1(input).println()
    part2(input).println()
}
