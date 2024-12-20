package day20

import kotlinx.coroutines.*
import println
import readInput

fun main() = runBlocking(Dispatchers.Default) {
    suspend fun <A, B> Iterable<A>.parallelMap(f: suspend (A) -> B): List<B> = coroutineScope {
        map { async { f(it) } }.awaitAll()
    }

    data class Point(val x: Int, val y: Int) {
        operator fun plus(other: Point) = Point(x + other.x, y + other.y)

        fun neighbours(): Set<Point> = setOf(
            Point(x + 1, y),
            Point(x - 1, y),
            Point(x, y + 1),
            Point(x, y - 1),
        )
    }

    data class Puzzle(
        val start: Point,
        val end: Point,
        val walls: Set<Point>,
        val emptySpaces: Set<Point>,
    )

    val directions = setOf(
        Point(1, 0),
        Point(-1, 0),
        Point(0, 1),
        Point(0, -1),
    )

    fun parse(input: List<String>): Puzzle {
        val walls = mutableSetOf<Point>()
        val emptySpaces = mutableSetOf<Point>()
        lateinit var start: Point
        lateinit var end: Point

        input.forEachIndexed { y, line ->
            line.forEachIndexed { x, c ->
                when (c) {
                    '#' -> walls.add(Point(x, y))
                    'S' -> start = Point(x, y)
                    'E' -> end = Point(x, y)
                    '.' -> emptySpaces.add(Point(x, y))
                }
            }
        }
        return Puzzle(start, end, walls, emptySpaces + start + end)
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

    suspend fun part1(input: List<String>, timeToSave: Int): Int {
        val (start, end, walls, emptySpaces) = parse(input)

        val distances = dijkstra(emptySpaces, start)
        val referenceTime = distances.filterKeys { it == end }
            .values
            .min()

        // With 2 picoseconds cheat time, you can pass through 1 piece of wall
        return walls
            // Optimization: only consider walls with at least 2 free spaces next to it
            .filter { wall ->
                wall.neighbours()
                    .count { emptySpaces.contains(it) } >= 2
            }
            .parallelMap { wall ->
                dijkstra(emptySpaces + wall, start)
                    .filterKeys { it == end }
                    .values
                    .min()
            }
            .count { it <= referenceTime - timeToSave }
    }

    fun part2(input: List<String>, timeToSave: Int): String {
        TODO()
    }

    val testInput = readInput("day20/Day20_test")
    check(part1(testInput, 2) == 44)
    check(part1(testInput, 4) == 30)
    check(part1(testInput, 6) == 16)
    check(part1(testInput, 8) == 14)
    check(part1(testInput, 10) == 10)
    check(part1(testInput, 12) == 8)
    check(part1(testInput, 20) == 5)
    check(part1(testInput, 36) == 4)
    check(part1(testInput, 38) == 3)
    check(part1(testInput, 40) == 2)
    check(part1(testInput, 64) == 1)
    // check(part2(testInput, 50) == 285)
    // check(part2(testInput, 52) == 253)
    // check(part2(testInput, 54) == 222)
    // check(part2(testInput, 56) == 193)
    // check(part2(testInput, 58) == 154)
    // check(part2(testInput, 60) == 129)
    // check(part2(testInput, 62) == 106)
    // check(part2(testInput, 64) == 86)
    // check(part2(testInput, 66) == 67)
    // check(part2(testInput, 68) == 55)
    // check(part2(testInput, 70) == 41)
    // check(part2(testInput, 72) == 29)
    // check(part2(testInput, 74) == 7)
    // check(part2(testInput, 76) == 3)

    val input = readInput("day20/Day20")
    part1(input, 100).println()
    // part2(input, 100).println()
}
