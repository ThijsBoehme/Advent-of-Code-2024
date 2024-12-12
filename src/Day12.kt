fun main() {
    data class Plot(val x: Int, val y: Int, val plant: Char, var visited: Boolean = false)

    fun List<Plot>.neighbours(plot: Plot): List<Plot> {
        return filter { other ->
            (other.x in (plot.x - 1..plot.x + 1) && other.y == plot.y)
                    || (other.x == plot.x && other.y in (plot.y - 1..plot.y + 1))
        }
    }

    fun List<Plot>.regionContaining(plot: Plot): Set<Plot> {
        val region = mutableSetOf(plot)
        val samePlants = filter { it.plant == plot.plant }

        var neighbours = (samePlants - region)
            .neighbours(plot)
        while (neighbours.isNotEmpty()) {
            region.addAll(neighbours)
            neighbours = region.flatMap { (samePlants - region).neighbours(it) }
        }

        return region.toSet()
    }

    fun parse(input: List<String>): List<Plot> {
        return input.flatMapIndexed { y: Int, line: String -> line.mapIndexed { x, c -> Plot(x, y, c) } }
    }

    fun perimeter(region: Set<Plot>): Int {
        val directions = listOf(-1 to 0, 0 to -1, 1 to 0, 0 to 1)
        return region.sumOf { plot ->
            directions.count { (dx, dy) ->
                region.none { other -> plot.x + dx == other.x && plot.y + dy == other.y }
            }
        }
    }

    fun part1(input: List<String>): Int {
        val plots = parse(input)

        var sum = 0
        plots.forEach { plot ->
            if (!plot.visited) {
                val region = plots.regionContaining(plot)
                region.forEach { it.visited = true }

                val area = region.size
                val perimeter = perimeter(region)
                sum += area * perimeter
            }
        }

        return sum
    }

    fun List<Plot>.numberOfGroups(): Int {
        val groups = mutableListOf<Set<Plot>>()

        forEach { plot ->
            if (groups.none { it.contains(plot) }) {
                groups.add(regionContaining(plot))
            }
        }

        return groups.size
    }

    fun sides(region: Set<Plot>): Int {
        var horizontalSides = 0
        val yIndices = IntRange(region.minOf { it.y }, region.maxOf { it.y } + 1)
        yIndices.forEach { y ->
            val rowM = region.filter { it.y == y - 1 }
            val rowN = region.filter { it.y == y }

            val mGroups = rowM.filter { m -> rowN.none { n -> m.x == n.x } }
                .numberOfGroups()
            val nGroups = rowN.filter { n -> rowM.none { m -> m.x == n.x } }
                .numberOfGroups()
            horizontalSides += mGroups + nGroups
        }

        var verticalSides = 0
        val xIndices = IntRange(region.minOf { it.x }, region.maxOf { it.x } + 1)
        xIndices.forEach { x ->
            val columnM = region.filter { it.x == x - 1 }
            val columnN = region.filter { it.x == x }

            val mGroups = columnM.filter { m -> columnN.none { n -> m.y == n.y } }
                .numberOfGroups()
            val nGroups = columnN.filter { n -> columnM.none { m -> m.y == n.y } }
                .numberOfGroups()
            verticalSides += mGroups + nGroups
        }


        return horizontalSides + verticalSides
    }

    fun part2(input: List<String>): Int {
        val plots = parse(input)

        var sum = 0
        plots.forEach { plot ->
            if (!plot.visited) {
                val region = plots.regionContaining(plot)
                region.forEach { it.visited = true }

                val area = region.size
                val sides = sides(region)
                sum += area * sides
            }
        }

        return sum
    }

    val testInput1 = readInput("Day12_test1")
    check(part1(testInput1) == 140)
    val testInput2 = readInput("Day12_test2")
    check(part1(testInput2) == 772)
    val testInput3 = readInput("Day12_test3")
    check(part1(testInput3) == 1930)

    check(part2(testInput1) == 80)
    check(part2(testInput2) == 436)
    val testInput4 = readInput("Day12_test4")
    check(part2(testInput4) == 236)
    val testInput5 = readInput("Day12_test5")
    check(part2(testInput5) == 368)
    check(part2(testInput3) == 1206)

    val input = readInput("Day12")
    part1(input).println()
    part2(input).println()
}
