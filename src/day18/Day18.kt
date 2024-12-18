package day18

import println
import readInput

fun main() {
    data class Point(val x: Int, val y: Int) {
        operator fun plus(other: Point) = Point(x + other.x, y + other.y)
    }

    val directions = setOf(
        Point(1, 0),
        Point(-1, 0),
        Point(0, 1),
        Point(0, -1),
    )

    fun dijkstra(vertices: List<Point>): MutableMap<Point, Int> {
        val distances = vertices.associateWith { Int.MAX_VALUE }.toMutableMap()
        val queue = vertices.toMutableList()
        distances[Point(0, 0)] = 0

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

    fun part1(input: List<String>, size: Int, steps: Int): Int {
        val grid = buildList {
            (0..size).forEach { x ->
                (0..size).forEach { y ->
                    add(Point(x, y))
                }
            }
        }

        val walls = input.take(steps)
            .map { it.split(",").let { (x, y) -> Point(x.toInt(), y.toInt()) } }
            .toSet()

        val distances = dijkstra(grid - walls)
        return distances.filterKeys { it == Point(size, size) }
            .values
            .min()
    }

    /**
     * Searching by iteration takes too long, so employing Binary search
     */
    fun part2(input: List<String>, size: Int): String {
        var lowerBound = 0
        var upperBound = input.size + 1

        while (true) {
            val testValue = lowerBound + (upperBound - lowerBound) / 2

            if (part1(input, size, testValue) == Int.MAX_VALUE) {
                upperBound = testValue
            } else {
                lowerBound = testValue
            }

            if (lowerBound + 1 == upperBound) {
                return input[testValue - 1]
            }
        }
    }

    val testInput = readInput("day18/Day18_test")
    check(part1(testInput, 6, 12) == 22)
    check(part2(testInput, 6) == "6,1")

    val input = readInput("day18/Day18")
    part1(input, 70, 1024).println()
    part2(input, 70).println()
}
