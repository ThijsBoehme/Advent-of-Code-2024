import kotlin.math.abs

fun main() {

    fun parse(input: List<String>) =
        input.map { it.split("\\s+".toRegex()).map(String::toLong) }

    fun List<Long>.isValidReport() =
        ((this == this.sorted() || this == this.sortedDescending()) // Valid if reports are fully ascending or descending...
                &&
                this.zipWithNext()
                    .all { (a, b) -> abs(a - b) in 1..3 }) // and differences between subsequent numbers is between 1 to 3

    fun part1(input: List<String>): Int {
        val reports = parse(input)
        return reports.count { it.isValidReport() }
    }

    fun part2(input: List<String>): Int {
        val reports = parse(input)
        return reports.count { report ->
            if (report.isValidReport()) return@count true // Count report if it is valid

            return@count report.indices
                .any {
                    report.filterIndexed { index, _ -> index != it }
                        .isValidReport()
                } // Count report if it is valid by removing a single level
        }
    }

    val testInput = readInput("Day02_test")
    check(part1(testInput) == 2)
    check(part2(testInput) == 4)

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}
