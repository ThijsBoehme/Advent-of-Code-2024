package day16

import println
import readInput

data class Vec2(val x: Int, val y: Int) {
    operator fun plus(other: Vec2) = Vec2(x + other.x, y + other.y)
}

private data class Node(val position: Vec2, val orientation: Direction)

private data class Reindeer(val x: Int, val y: Int, val orientation: Direction)

private enum class Direction(val dx: Int, val dy: Int, val vector: Vec2) {
    North(0, -1, Vec2(0, -1)),
    East(1, 0, Vec2(1, 0)),
    South(0, 1, Vec2(0, 1)),
    West(-1, 0, Vec2(-1, 0));

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

    fun opposite() = when (this) {
        North -> South
        East -> West
        South -> South
        West -> East
    }
}

fun main() {
    fun print(objects: Map<Pair<Int, Int>, Char>, width: Int, height: Int) {
        repeat(height) { y ->
            repeat(width) { x ->
                val character = objects.getOrDefault(x to y, ' ')
                print(character)
            }
            println()
        }
    }

//    fun part1(input: List<String>): Int {
//        fun pathsToEnd(
//            walls: Set<Pair<Int, Int>>,
//            reindeer: Reindeer,
//            history: Set<Reindeer>,
//            end: Pair<Int, Int>
//        ): Long {
//            print(
//                buildMap {
//                    walls.forEach { wall -> put(wall, '#') }
//                    (history + reindeer).forEach {
//                        put(
//                            it.x to it.y, when (it.orientation) {
//                                Direction.North -> '^'
//                                Direction.East -> '>'
//                                Direction.South -> 'v'
//                                Direction.West -> '<'
//                            }
//                        )
//                    }
//                    put(end, 'E')
//                },
//                input[0].length,
//                input.size
//            )
////            println(reindeer)
//            // If at destination, return 0
//            if (reindeer.x == end.first && reindeer.y == end.second) return 0
//
//            // If in wall or already visited location, return Int.MAX
//            if (walls.contains(reindeer.x to reindeer.y)
//                || history.any {
//                    it.x == reindeer.x && it.y == reindeer.y
////                        && it.orientation == reindeer.orientation.opposite()
//                }
//            ) {
//                return Int.MAX_VALUE.toLong()
//            }
//
//            // Going ahead
//            val spaceAhead = reindeer.x + reindeer.orientation.dx to reindeer.y + reindeer.orientation.dy
//            val pathLengthGoingForward = if (walls.contains(spaceAhead)) {
//                Int.MAX_VALUE.toLong()
//            } else {
//                pathsToEnd(
//                    walls,
//                    reindeer.copy(x = spaceAhead.first, y = spaceAhead.second),
//                    history + reindeer,
//                    end
//                ) + 1
//            }
//
//            // Turning left
//            val lookingLeft = reindeer.copy(orientation = reindeer.orientation.turnCounterClockwise())
//            val spaceLeft = lookingLeft.x + lookingLeft.orientation.dx to reindeer.y + lookingLeft.orientation.dy
//            val pathLengthTurningLeft = if (walls.contains(spaceLeft)) {
//                Int.MAX_VALUE.toLong()
//            } else {
//                pathsToEnd(
//                    walls,
//                    lookingLeft.copy(x = spaceLeft.first, y = spaceLeft.second),
//                    history + reindeer,
//                    end
//                ) + 1001
//            }
//
//            // Turning right
//            val lookingRight = reindeer.copy(orientation = reindeer.orientation.turnClockwise())
//            val spaceRight = lookingRight.x + lookingRight.orientation.dx to reindeer.y + lookingRight.orientation.dy
//            val pathLengthTurningRight = if (walls.contains(spaceRight)) {
//                Int.MAX_VALUE.toLong()
//            } else {
//                pathsToEnd(
//                    walls,
//                    reindeer.copy(orientation = reindeer.orientation.turnClockwise()),
//                    history + reindeer,
//                    end
//                ) + 1001
//            }
//
//            return minOf(pathLengthGoingForward, pathLengthTurningLeft, pathLengthTurningRight)
//        }
//
//        val walls = mutableSetOf<Pair<Int, Int>>()
//        lateinit var reindeer: Reindeer
//        lateinit var end: Pair<Int, Int>
//
//        input.forEachIndexed { y, line ->
//            line.forEachIndexed { x, c ->
//                when (c) {
//                    '#' -> walls.add(Pair(x, y))
//                    'S' -> reindeer = Reindeer(x, y, Direction.East)
//                    'E' -> end = x to y
//                }
//            }
//        }
//
//        return pathsToEnd(walls, reindeer, emptySet(), end).toInt()
//    }

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

//        val oldQueue = buildMap {
//            Direction.entries.forEach { direction ->
//                put(Node(Vec2(start.x, start.y), direction), Int.MAX_VALUE)
//                put(Node(end, direction), Int.MAX_VALUE)
//                emptySpaces.forEach {
//                    put(Node(it, direction), Int.MAX_VALUE)
//                }
//            }
//
//            put(Node(Vec2(start.x, start.y), start.orientation), 0)
//        }.toMutableMap()

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

        val (distances, prev) = dijkstra(start, end, emptySpaces)

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
