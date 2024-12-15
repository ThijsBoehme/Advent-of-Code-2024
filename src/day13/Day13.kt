package day13

import println
import readInput
import kotlin.math.round

fun main() {
    data class ClawMachine(val aX: Long, val aY: Long, val bX: Long, val bY: Long, val pX: Long, val pY: Long)

    fun parse(input: String, offset: Long = 0L): List<ClawMachine> {
        return input.split("\n\n").map { block ->
            val (machineAString, machineBString, prizeString) = block.split("\n")

            val machineRegex = """Button [AB]: X\+(?<x>\d+), Y\+(?<y>\d+)""".toRegex()
            val (aX, aY) = machineRegex.find(machineAString)!!.destructured
            val (bX, bY) = machineRegex.find(machineBString)!!.destructured

            val prizeRegex = """Prize: X=(?<x>\d+), Y=(?<y>\d+)""".toRegex()
            val (prizeX, prizeY) = prizeRegex.find(prizeString)!!.destructured

            ClawMachine(
                aX.toLong(), aY.toLong(),
                bX.toLong(), bY.toLong(),
                prizeX.toLong() + offset, prizeY.toLong() + offset
            )
        }
    }

    fun solve(clawMachines: List<ClawMachine>, checkBounds: Boolean): Long {
        return clawMachines.sumOf {
            val determinantDenominator = it.aX * it.bY - it.bX * it.aY
            if (determinantDenominator != 0L) {
                val m =
                    round(it.bY * it.pX / determinantDenominator.toDouble() - it.bX * it.pY / determinantDenominator.toDouble()).toLong()
                val n =
                    round(-it.aY * it.pX / determinantDenominator.toDouble() + it.aX * it.pY / determinantDenominator.toDouble()).toLong()

                if (checkBounds) {
                    if (m !in 0..100 && n !in 0..100) return@sumOf 0
                }

                // Check if m and n were integer solutions (no error due to rounding)
                if (m * it.aX + n * it.bX == it.pX
                    && m * it.aY + n * it.bY == it.pY
                ) {
                    return@sumOf (m * 3) + n
                }
            }
            return@sumOf 0
        }
    }

    fun part1(input: List<String>): Long {
        val clawMachines = parse(input.joinToString("\n"))

        return solve(clawMachines, true)
    }

    fun part2(input: List<String>): Long {
        val clawMachines = parse(input.joinToString("\n"), 10000000000000L)
        return solve(clawMachines, false)
    }

    val testInput = readInput("day13/Day13_test")
    check(part1(testInput) == 480L)

    val input = readInput("day13/Day13")
    part1(input).println()
    part2(input).println()
}
