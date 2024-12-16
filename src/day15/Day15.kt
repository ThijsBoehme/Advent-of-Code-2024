package day15

import println
import readInput

private sealed class Object(var xRange: IntRange, var y: Int, private val map: List<Object>) {
    fun canMove(direction: Direction): Boolean {
        if (this is Wall) return false

        return when (direction) {
            Direction.Up, Direction.Down -> {
                xRange.flatMap { x -> map.filter { x in it.xRange && y + direction.dy == it.y } }
                    .toSet()
                    .all { it.canMove(direction) }
            }

            Direction.Right -> {
                map.filter { it.xRange.contains(xRange.last + direction.dx) && y == it.y }
                    .all { it.canMove(direction) }
            }

            Direction.Left -> {
                map.filter { it.xRange.contains(xRange.first + direction.dx) && y == it.y }
                    .all { it.canMove(direction) }
            }
        }
    }

    fun move(direction: Direction) {
        when (direction) {
            Direction.Up, Direction.Down -> {
                xRange.flatMap { x -> map.filter { x in it.xRange && y + direction.dy == it.y } }
                    .toSet()
                    .forEach { it.move(direction) }
            }

            Direction.Right -> {
                map.filter { it.xRange.contains(xRange.last + direction.dx) && y == it.y }
                    .forEach { it.move(direction) }
            }

            Direction.Left -> {
                map.filter { it.xRange.contains(xRange.first + direction.dx) && y == it.y }
                    .forEach { it.move(direction) }
            }
        }
        xRange = (xRange.first + direction.dx)..(xRange.last + direction.dx)
        y += direction.dy
    }
}

private class Robot(x: IntRange, y: Int, map: List<Object>) : Object(x, y, map)
private class Box(x: IntRange, y: Int, map: List<Object>) : Object(x, y, map)
private class Wall(x: IntRange, y: Int, map: List<Object>) : Object(x, y, map)

private enum class Direction(private val c: Char, val dx: Int, val dy: Int) {
    Up('^', 0, -1),
    Right('>', 1, 0),
    Down('v', 0, 1),
    Left('<', -1, 0);

    companion object {
        fun fromChar(c: Char): Direction {
            return entries.first { it.c == c }
        }
    }
}

fun main() {

    fun parse(input: List<String>, doubleWide: Boolean = false): Pair<List<Object>, List<Direction>> {
        val map = buildList {
            input
                .takeWhile { it.isNotEmpty() }
                .forEachIndexed { y, row ->
                    row.forEachIndexed { x, c ->
                        when (c) {
                            '#' -> add(Wall(if (doubleWide) (2 * x)..(2 * x + 1) else x..x, y, this))
                            'O' -> add(Box(if (doubleWide) (2 * x)..(2 * x + 1) else x..x, y, this))
                            '@' -> add(Robot(if (doubleWide) (2 * x)..(2 * x) else x..x, y, this))
                        }
                    }
                }
        }
        val instructions = input.takeLastWhile { it.isNotEmpty() }.joinToString("").map { Direction.fromChar(it) }

        return map to instructions
    }

    fun solve(pair: Pair<List<Object>, List<Direction>>): Int {
        val (map, instructions) = pair
        val robot = map.first { it is Robot }

        instructions.forEach { instruction ->
            if (robot.canMove(instruction)) {
                robot.move(instruction)
            }
        }

        return map.filterIsInstance<Box>()
            .sumOf { 100 * it.y + it.xRange.first }
    }

    val testInput1 = readInput("day15/Day15_test1")
    check(solve(parse(testInput1)) == 2028)
    val testInput2 = readInput("day15/Day15_test2")
    check(solve(parse(testInput2)) == 10092)
    check(solve(parse(testInput2, true)) == 9021)

    val input = readInput("day15/Day15")
    solve(parse(input)).println()
    solve(parse(input, true)).println()
}
