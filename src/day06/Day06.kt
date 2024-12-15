package day06

import println
import readInput

private enum class Direction {
    NORTH, SOUTH, EAST, WEST
}

fun main() {

    data class Guard(var x: Int, var y: Int, var heading: Direction) {
        fun turnRight() {
            heading = when (heading) {
                Direction.NORTH -> Direction.EAST
                Direction.SOUTH -> Direction.WEST
                Direction.EAST -> Direction.SOUTH
                Direction.WEST -> Direction.NORTH
            }
        }

        fun lookingAt(): Pair<Int, Int> {
            return when (heading) {
                Direction.NORTH -> (x to y - 1)
                Direction.SOUTH -> (x to y + 1)
                Direction.EAST -> (x + 1 to y)
                Direction.WEST -> (x - 1 to y)
            }
        }

        fun moveOneSpace() {
            lookingAt().let { (newX, newY) -> x = newX; y = newY }
        }
    }


    fun part1(rows: List<String>): Int {
        val field = rows.map { it.toMutableList() }.toMutableList()
        val guard = Guard(
            field.first { it.contains('^') }.indexOf('^'),
            field.indexOfFirst { it.contains('^') },
            Direction.NORTH
        )

        // field.print()

        while (guard.x in field[0].indices && guard.y in field.indices) {
            // For visualization
            // field[guard.y][guard.x] = when (guard.heading) {
            //     Direction.NORTH -> '^'
            //     Direction.SOUTH -> 'v'
            //     Direction.EAST -> '>'
            //     Direction.WEST -> '<'
            // }

            if (guard.lookingAt().let { (x, y) -> field.getOrNull(y)?.getOrNull(x)?.equals('#') == true }) {
                guard.turnRight()
                continue
            }

            field[guard.y][guard.x] = 'X'
            guard.moveOneSpace()

            // field.print()
        }

        return field.sumOf { line -> line.count { it == 'X' } }
    }

    fun part2(rows: List<String>): Int {
        val field = rows.map { it.toMutableList() }.toMutableList()

        val obstructionLocations = mutableListOf<Pair<Int, Int>>()
        field.forEachIndexed { row, line ->
            line.forEachIndexed { column, c ->
                if (c == '.') obstructionLocations.add(
                    column to row
                )
            }
        }

        return obstructionLocations.count { (oX, oY) ->
            val fieldCopy = rows.map { it.toMutableList() }.toMutableList()
            fieldCopy[oY][oX] = 'O'

            val history = mutableSetOf<Pair<Pair<Int, Int>, Direction>>()

            val guard = Guard(
                field.first { it.contains('^') }.indexOf('^'),
                field.indexOfFirst { it.contains('^') },
                Direction.NORTH
            )

            var guardInLoop = false

            while (!guardInLoop) {
                history.add((guard.x to guard.y) to guard.heading)

                val (m, n) = guard.lookingAt()
                if (m !in fieldCopy[0].indices || n !in fieldCopy.indices) {
                    break
                }

                if (history.contains((m to n) to guard.heading)) {
                    guardInLoop = true
                    break
                }

                when (fieldCopy[n][m]) {
                    '.', '^' -> {}

                    '#', 'O' -> {
                        guard.turnRight()
                        continue
                    }

                    else -> error("Invalid character at ($m, $n)")
                }

                guard.moveOneSpace()
            }

            guardInLoop
        }
    }

    val testInput = readInput("day06/Day06_test")
    check(part1(testInput) == 41)
    check(part2(testInput) == 6)

    val input = readInput("day06/Day06")
    part1(input).println()
    part2(input).println()
}

// For visualization
private fun List<List<Char>>.print() {
    forEach { line ->
        line.forEach { character -> print("$character") }
        kotlin.io.println()
    }
    kotlin.io.println()
    kotlin.io.println()
}
