package day07

import println
import readInput

fun main() {

    fun parse(line: String): Pair<Long, List<Long>> {
        val (expectedResultString, operandsString) = line.split(": ")
        val expectedResult = expectedResultString.toLong()
        val operands = operandsString.split(" ").map { it.toLong() }

        return expectedResult to operands
    }

    fun calibrationResult(
        input: List<String>,
        operations: (Long, Long) -> List<Long>
    ) = input
        .map(::parse)
        .filter { (expectedResult, operands) ->
            var intermediateResults = listOf(operands.first())
            operands.drop(1)
                .forEach { operand ->
                    intermediateResults = intermediateResults.flatMap { operations(it, operand) }
                }

            intermediateResults.contains(expectedResult)
        }
        .sumOf { it.first }

    fun part1(input: List<String>) = calibrationResult(input) { a, b -> listOf(a + b, a * b) }

    fun part2(input: List<String>) =
        calibrationResult(input) { a, b -> listOf(a + b, a * b, (a.toString() + b.toString()).toLong()) }

    val testInput = readInput("day07/Day07_test")
    check(part1(testInput) == 3749L)
    check(part2(testInput) == 11387L)

    val input = readInput("day07/Day07")
    part1(input).println()
    part2(input).println()
}
