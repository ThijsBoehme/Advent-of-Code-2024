fun main() {
    data class Guard(val x: Int, val y: Int, val vX: Int, val vY: Int)

    fun parse(input: List<String>): List<Guard> {
        val regex = """p=(-?\d+),(-?\d+) v=(-?\d+),(-?\d+)""".toRegex()
        return input.map { line ->
            val (x, y, vX, vY) = regex.matchEntire(line)!!.destructured
            Guard(x.toInt(), y.toInt(), vX.toInt(), vY.toInt())
        }
    }

    fun part1(guards: List<Guard>, width: Int, height: Int, t: Int = 100): Int {
        return guards
            .map { guard ->
                Guard(
                    Math.floorMod(guard.x + guard.vX * t, width),
                    Math.floorMod(guard.y + guard.vY * t, height),
                    guard.vX,
                    guard.vY
                )
            }
            .groupingBy { guard ->
                guard.x.compareTo(width / 2) to guard.y.compareTo(height / 2)
            }
            .eachCount()
            .filterKeys { (x, y) -> x != 0 && y != 0 }
            .values
            .fold(1, Int::times)
    }

    fun part2(guards: List<Guard>, width: Int, height: Int) {
        repeat(Int.MAX_VALUE) { t ->
            val positions = guards
                .map { guard ->
                    Math.floorMod(guard.x + guard.vX * t, width) to Math.floorMod(guard.y + guard.vY * t, height)
                }

            if (positions.distinct().size == guards.size) {
                println(t)

                (0..<height).forEach { y ->
                    (0..<width).forEach { x ->
                        if (positions.contains(x to y)) print('.') else print(' ')
                    }
                    println()
                }
            }
        }
    }

    val testInput = readInput("Day14_test")
    check(part1(parse(testInput), 11, 7) == 12)

    val input = readInput("Day14")
    part1(parse(input), 101, 103).println()
    part2(parse(input), 101, 103)
}
