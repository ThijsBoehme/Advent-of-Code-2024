fun main() {
    fun part1(input: String): Long {
        val disk = ArrayDeque<Int>()

        input.forEachIndexed { index, c ->
            if (index % 2 == 0) {
                repeat(c.digitToInt()) { disk.add(index / 2) }
            } else {
                repeat(c.digitToInt()) { disk.add(-1) }
            }
        }

        var position = 0
        var sum = 0L

        while (disk.isNotEmpty()) {
            var fileId = disk.removeFirstOrNull()
            while (fileId == -1) {
                fileId = disk.removeLastOrNull()
            }
            sum += position * (fileId ?: break)
            position++
        }

        return sum
    }

    fun part2(input: String): Int {
        TODO()
    }

    val testInput = readInput("Day09_test").first()
    check(part1(testInput) == 1928L)
    // check(part2(testInput) == 2858)

    val input = readInput("Day09").first()
    part1(input).println()
    // part2(input).println()
}
