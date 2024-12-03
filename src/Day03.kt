private const val DO = "do()"
private const val DONT = "don't()"

fun main() {
    val regex = """mul\((?<a>\d{1,3}),(?<b>\d{1,3})\)""".toRegex()

    fun part1(input: List<String>): Long {
        val line = input.joinToString("")
        val results = regex.findAll(line)
        return results.sumOf { result ->
            val a = result.groups["a"]!!.value.toLong()
            val b = result.groups["b"]!!.value.toLong()
            a * b
        }
    }

    fun part2(input: List<String>): Long {
        val line = input.joinToString("")

        val doParts = mutableListOf<String>()

        var remainder = line
        while (remainder != "") {
            val dontIndex = remainder.indexOf(DONT)
            if (dontIndex == -1) {
                doParts.add(remainder)
                break
            }
            val partToAdd = remainder.substring(0..<dontIndex)
            doParts += partToAdd
            remainder = remainder.removeRange(0..<dontIndex + DONT.length)

            val doIndex = remainder.indexOf(DO)
            if (doIndex == -1) break
            remainder = remainder.removeRange(0..<doIndex + DO.length)
        }

        return doParts.sumOf { part ->
            part1(listOf(part))
        }
    }

    check(part1(listOf("xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))")) == 161L)
    check(part2(listOf("xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))")) == 48L)

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}
