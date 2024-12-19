package day19

import println
import readInput

fun main() {
    val possibleDesigns = mutableMapOf<String, Long>()

    fun numberOfPossibleDesigns(design: String, patterns: List<String>): Long {
        if (design == "") return 1
        return possibleDesigns.getOrPut(design) {
            return@getOrPut patterns.filter { design.startsWith(it) }
                .sumOf { numberOfPossibleDesigns(design.removePrefix(it), patterns) }
        }
    }

    fun part1(input: List<String>): Int {
        possibleDesigns.clear()

        val patterns = input.first().split(", ")
        val designs = input.drop(2)

        return designs.count { design ->
            numberOfPossibleDesigns(design, patterns) > 0
        }
    }

    fun part2(input: List<String>): Long {
        possibleDesigns.clear()

        val patterns = input.first().split(", ")
        val designs = input.drop(2)

        return designs.sumOf { design ->
            numberOfPossibleDesigns(design, patterns)
        }
    }

    val testInput = readInput("day19/Day19_test")
    check(part1(testInput) == 6)
    check(part2(testInput) == 16L)

    val input = readInput("day19/Day19")
    part1(input).println()
    part2(input).println()
}
