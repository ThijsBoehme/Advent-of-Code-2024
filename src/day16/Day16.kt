package day16

import println
import readInput

data class Vec2(val x: Int, val y: Int) {
    operator fun plus(other: Vec2) = Vec2(x + other.x, y + other.y)
}

private data class Node(val position: Vec2, val orientation: Direction)

private data class Reindeer(val x: Int, val y: Int, val orientation: Direction)

private enum class Direction(val vector: Vec2) {
    North(Vec2(0, -1)),
    East(Vec2(1, 0)),
    South(Vec2(0, 1)),
    West(Vec2(-1, 0));

    fun turnClockwise() = when (this) {
        North -> East
        East -> South
        South -> West
        West -> North
    }

    fun turnCounterClockwise() = when (this) {
        North -> West
        East -> North
        South -> East
        West -> South
    }
}

fun main() {
    fun parse(input: List<String>): Triple<MutableSet<Vec2>, Reindeer, Vec2> {
        val walls = mutableSetOf<Vec2>()
        val emptySpaces = mutableSetOf<Vec2>()
        lateinit var reindeer: Reindeer
        lateinit var end: Vec2

        input.forEachIndexed { y, line ->
            line.forEachIndexed { x, c ->
                when (c) {
                    '#' -> walls.add(Vec2(x, y))
                    'S' -> reindeer = Reindeer(x, y, Direction.East)
                    'E' -> end = Vec2(x, y)
                    '.' -> emptySpaces.add(Vec2(x, y))
                }
            }
        }
        return Triple(emptySpaces, reindeer, end)
    }

    fun dijkstra(
        start: Reindeer,
        end: Vec2,
        emptySpaces: MutableSet<Vec2>
    ): Pair<MutableMap<Node, Int>, MutableMap<Node, MutableList<Node>>> {
        val vertices = buildList {
            Direction.entries.forEach { direction ->
                add(Node(Vec2(start.x, start.y), direction))
                add(Node(end, direction))
                emptySpaces.forEach {
                    add(Node(it, direction))
                }
            }
        }

        val distances = vertices.associateWith { Int.MAX_VALUE }.toMutableMap()
        val prev = mutableMapOf<Node, MutableList<Node>>()
        val queue = vertices.toMutableList()
        distances[Node(Vec2(start.x, start.y), start.orientation)] = 0

        while (queue.isNotEmpty()) {
            val u = queue.minBy { distances[it]!! }
            queue.remove(u)

            val v1 = Node(u.position + u.orientation.vector, u.orientation)
            distances.compute(v1) { _, distance ->
                distance?.let {
                    val alt = distances[u]!! + 1
                    if (alt <= distance) {
                        prev.getOrPut(v1) { mutableListOf() }.add(u)
                    }
                    minOf(distance, alt)
                }
            }

            val v2 = Node(u.position, u.orientation.turnClockwise())
            distances.compute(v2) { _, distance ->
                distance?.let {
                    val alt = distances[u]!! + 1000
                    if (alt <= distance) {
                        prev.getOrPut(v2) { mutableListOf() }.add(u)
                    }
                    minOf(distance, alt)
                }
            }

            val v3 = Node(u.position, u.orientation.turnCounterClockwise())
            distances.compute(v3) { _, distance ->
                distance?.let {
                    val alt = distances[u]!! + 1000
                    if (alt <= distance) {
                        prev.getOrPut(v3) { mutableListOf() }.add(u)
                    }
                    minOf(distance, alt)
                }
            }
        }

        return distances to prev
    }

    fun part1(input: List<String>): Int {
        val (emptySpaces, start, end) = parse(input)

        val (distances, _) = dijkstra(start, end, emptySpaces)

        return distances.filterKeys { it.position == end }
            .values
            .min()
    }

    fun part2(input: List<String>): Int {
        val (emptySpaces, start, end) = parse(input)

        val (distances, prev) = dijkstra(start, end, emptySpaces)

        val endNode = distances.filterKeys { it.position == end }
            .minBy { it.value }
            .key

        val s = mutableSetOf<Node>()
        var u = listOf(endNode)
        var nextU = mutableListOf<Node>()

        while (u.isNotEmpty()) {
            u.forEach {
                if (prev[it]?.isNotEmpty() == true) {
                    s.add(it)
                    nextU.addAll(prev[it]!!)
                }
            }
            u = nextU
            nextU = mutableListOf()
        }

        return s.map { it.position }.toSet().size
    }

    val testInput1 = readInput("day16/Day16_test1")
    check(part1(testInput1) == 7036)
    val testInput2 = readInput("day16/Day16_test2")
    check(part1(testInput2) == 11048)
    check(part2(testInput1) == 45)
    check(part2(testInput2) == 64)

    val input = readInput("day16/Day16")
    part1(input).println()
    part2(input).println()
}
