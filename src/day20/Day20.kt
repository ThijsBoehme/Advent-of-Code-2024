package day20

import println
import readInput
import kotlin.math.abs

fun main() {
    data class Point(val x: Int, val y: Int) {
        operator fun plus(other: Point) = Point(x + other.x, y + other.y)

        fun distanceTo(other: Point) = abs(x - other.x) + abs(y - other.y)
    }

    data class Puzzle(
        val start: Point,
        val end: Point,
        val emptySpaces: Set<Point>,
    )

    val directions = setOf(
        Point(1, 0),
        Point(-1, 0),
        Point(0, 1),
        Point(0, -1),
    )

    fun parse(input: List<String>): Puzzle {
        val emptySpaces = mutableSetOf<Point>()
        lateinit var start: Point
        lateinit var end: Point

        input.forEachIndexed { y, line ->
            line.forEachIndexed { x, c ->
                when (c) {
                    'S' -> start = Point(x, y)
                    'E' -> end = Point(x, y)
                    '.' -> emptySpaces.add(Point(x, y))
                }
            }
        }
        return Puzzle(start, end, emptySpaces + start + end)
    }

    fun dijkstra(vertices: Set<Point>, start: Point): Map<Point, Int> {
        val distances = vertices.associateWith { Int.MAX_VALUE }.toMutableMap()
        val queue = vertices.toMutableList()
        distances[start] = 0

        while (queue.isNotEmpty()) {
            val u = queue
                .filter { distances[it]!! != Int.MAX_VALUE }
                .minByOrNull { distances[it]!! }
                ?: break
            queue.remove(u)

            directions.forEach { direction ->
                val v = u + direction
                distances.compute(v) { _, distance ->
                    distance?.let {
                        val alt = distances[u]!! + 1
                        minOf(distance, alt)
                    }
                }
            }
        }

        return distances
    }

    fun solve(input: List<String>, timeToSave: Int, cheatTime: Int): Int {
        val (start, end, emptySpaces) = parse(input)

        val distancesFromStart = dijkstra(emptySpaces, start)
        val distancesFromEnd = dijkstra(emptySpaces, end)
        val referenceTime = distancesFromStart.filterKeys { it == end }
            .values
            .min()

        return distancesFromStart
            .keys
            .flatMap { a ->
                distancesFromEnd.keys.map { b -> a to b }
            }
            .filter { (a, b) -> a.distanceTo(b) in 1..cheatTime }
            .filter { (a, b) -> distancesFromStart[a]!! < distancesFromStart[b]!! }
            .count { (a, b) -> distancesFromStart[a]!! + distancesFromEnd[b]!! + a.distanceTo(b) <= referenceTime - timeToSave }
    }

    val testInput = readInput("day20/Day20_test")
    check(solve(testInput, 2, 2) == 44)
    check(solve(testInput, 4, 2) == 30)
    check(solve(testInput, 6, 2) == 16)
    check(solve(testInput, 8, 2) == 14)
    check(solve(testInput, 10, 2) == 10)
    check(solve(testInput, 12, 2) == 8)
    check(solve(testInput, 20, 2) == 5)
    check(solve(testInput, 36, 2) == 4)
    check(solve(testInput, 38, 2) == 3)
    check(solve(testInput, 40, 2) == 2)
    check(solve(testInput, 64, 2) == 1)

    check(solve(testInput, 50, 20) == 285)
    check(solve(testInput, 52, 20) == 253)
    check(solve(testInput, 54, 20) == 222)
    check(solve(testInput, 56, 20) == 193)
    check(solve(testInput, 58, 20) == 154)
    check(solve(testInput, 60, 20) == 129)
    check(solve(testInput, 62, 20) == 106)
    check(solve(testInput, 64, 20) == 86)
    check(solve(testInput, 66, 20) == 67)
    check(solve(testInput, 68, 20) == 55)
    check(solve(testInput, 70, 20) == 41)
    check(solve(testInput, 72, 20) == 29)
    check(solve(testInput, 74, 20) == 7)
    check(solve(testInput, 76, 20) == 3)

    val input = readInput("day20/Day20")
    solve(input, 100, 2).println()
    solve(input, 100, 20).println()
}
