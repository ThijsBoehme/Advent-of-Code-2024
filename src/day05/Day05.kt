package day05

import println
import kotlin.io.path.Path
import kotlin.io.path.readText

fun main() {
    fun readInput(name: String) = Path("src/$name.txt").readText().trim()

    fun parseRules(rules: List<String>) = rules.map { it.split("|")[0] to it.split("|")[1] }

    fun parseUpdates(updates: List<String>) = updates.map { it.split(",") }

    fun part1(rules: List<Pair<String, String>>, updates: List<List<String>>): Int {
        return updates.filter { update ->
            val reversePairsToSearch = update.flatMapIndexed { index: Int, s: String ->
                update.drop(index + 1)
                    .map { it to s }
            }
            rules.none { reversePairsToSearch.contains(it) }
        }.sumOf { it[it.size / 2].toInt() }
    }

    fun part2(rules: List<Pair<String, String>>, updates: List<List<String>>): Int {
        val incorrectUpdates = updates.filter { update ->
            val reversePairsToSearch = update.flatMapIndexed { index: Int, s: String ->
                update.drop(index + 1)
                    .map { it to s }
            }
            rules.any { reversePairsToSearch.contains(it) }
        }

        return incorrectUpdates.map { update ->
            val relevantRules = rules.filter { (left, right) ->
                update.contains(left) && update.contains(right)
            }.toMutableList()

            val orderedPages = mutableListOf<String>()
            while (relevantRules.isNotEmpty()) {
                val pageNumber = relevantRules.first { (left, _) ->
                    relevantRules.none { (_, right) -> left == right }
                }.first
                orderedPages.add(pageNumber)
                relevantRules.removeAll { (left, _) -> left == pageNumber }
            }

            val finalPages =
                rules.filter { (_, right) -> rules.map { it.first }.none { it == right } }.map { it.second }.distinct()

            orderedPages.addAll(finalPages)

            orderedPages
        }.sumOf { it[it.size / 2].toInt() }
    }

    val testInput = readInput("day05/Day05_test")
    val (testRules, testUpdates) = testInput.split("\n\n").map(String::lines)
    check(part1(parseRules(testRules), parseUpdates(testUpdates)) == 143)
    check(part2(parseRules(testRules), parseUpdates(testUpdates)) == 123)

    val input = readInput("day05/Day05")
    val (rules, updates) = input.split("\n\n").map(String::lines)
    part1(parseRules(rules), parseUpdates(updates)).println()
    part2(parseRules(rules), parseUpdates(updates)).println()
}
