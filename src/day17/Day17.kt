package day17

import println
import readInput
import kotlin.math.max
import kotlin.math.pow

fun main() {
    val debug = false

    var a = 0L
    var b = 0L
    var c = 0L

    fun combo(operand: Int): Long {
        return when (operand) {
            in 0..3 -> operand.toLong()
            4 -> a
            5 -> b
            6 -> c
            else -> throw IllegalArgumentException("Illegal combo operand $operand")
        }
    }

    /**
     * Dividing by powers of 2 is the same as bit-shifting to the right by the exponent
     */
    fun division(operand: Int): Long {
        val exponent = combo(operand).toInt()
        if (debug) println("\tdividing $a by 2^$exponent")
        return a.shr(exponent)
    }

    fun xor(operand: Long) {
        val xor = b xor operand
        if (debug) println("\txor: $b xor $operand")
        if (debug) println("\tb <- $xor")
        b = xor
    }

    fun runProgram(instructions: List<Pair<Int, Int>>): String {
        if (debug) println("Initialized with:")
        if (debug) println("a = $a")
        if (debug) println("b = $b")
        if (debug) println("c = $c")

        var pointer = 0
        val output = mutableListOf<Long>()

        do {
            val (opcode, operand) = instructions[pointer / 2]
            pointer += 2
            if (debug) println("opcode: $opcode, operand: $operand:")
            when (opcode) {
                0 -> {
                    // adv: division
                    val division = division(operand)
                    if (debug) println("\ta <- $division")
                    a = division
                }

                1 -> {
                    // bxl: bitwise XOR
                    xor(operand.toLong())
                }

                2 -> {
                    // bst
                    val combo = combo(operand)
                    val modulo = combo % 8
                    if (debug) println("\tmodulo: $combo % 8")
                    if (debug) println("\tb <- $modulo")
                    b = modulo
                }

                3 -> {
                    // jnz
                    if (debug) println("\ta = $a")
                    if (a != 0L) {
                        if (debug) println("\tpointer <- $operand")
                        pointer = operand
                    }
                }

                4 -> {
                    // bxc
                    xor(c)
                }

                5 -> {
                    // out
                    val combo = combo(operand)
                    if (debug) println("\tmodulo: $combo % 8")
                    val element = (combo % 8)
                    if (debug) println("\tout <- $element")
                    output.add(element)
                }

                6 -> {
                    // bdv
                    val division = division(operand)
                    if (debug) println("\tb <- $division")
                    b = division
                }

                7 -> {
                    // cdv
                    val division = division(operand)
                    if (debug) println("\tc <- $division")
                    c = division
                }
            }

            if (debug) println("pointer = $pointer")
        } while ((pointer / 2) < instructions.size)

        return output.joinToString(",")
    }

    fun parse(input: List<String>): List<Pair<Int, Int>> {
        a = input[0].split(": ")[1].toLong()
        b = input[1].split(": ")[1].toLong()
        c = input[2].split(": ")[1].toLong()

        return input[4]
            .split(": ")[1]
            .split(",")
            .map { it.toInt() }
            .windowed(2, 2)
            .map { (a, b) -> a to b }
    }

    fun part1(input: List<String>): String {
        check(input.size == 5) { "Incorrect number of lines in input" }

        val instructions = parse(input)

        return runProgram(instructions)
    }

    fun part2(input: List<String>): Long {
        check(input.size == 5) { "Incorrect number of lines in input" }

        val instructions = parse(input)
        var matchingAValue = 0L

        // Part of this is deducted by hand and _might_ not work for all inputs
        // Since there's only 1 'out' command in my input, we will have to loop n (=number of digits in input) times
        // through the program to output the full instruction set
        val loops = instructions.size * 2

        repeat(loops) { n ->
            // Going from right to left (lowest a to larger a), we can search for the smallest possible a that outputs
            // the last n parts of the instructions between some bounds
            // As a is divided by 8 every loop, we have to expand the bounds by factor 8 every loop
            // Since the division by 8 needs to be at least the result of the previous loop, the lower bound can be that
            // multiplied by 8.
            // This constrains the bounds enough that a solution is quickly found
            val lowerBound = max(matchingAValue * 8, 8.0.pow(n).toLong())
            val upperBound = 8.0.pow(n + 1).toLong()
            (lowerBound..<upperBound).forEach {
                matchingAValue = it
                a = it
                b = 0
                c = 0

                val output = runProgram(instructions)

                // Check if the output matches the end of the instruction set
                if (instructions.joinToString(",") { (a, b) -> "$a,$b" }.endsWith(output)) {
                    if (debug) println("Found solution for loop $n: $matchingAValue")
                    return@repeat
                }
            }
        }

        return matchingAValue
    }

    val testInput1 = readInput("day17/Day17_test1")
    check(part1(testInput1) == "4,6,3,5,6,3,5,2,1,0")
    val testInput2 = readInput("day17/Day17_test2")
    check(part2(testInput2) == 117440L)

    val input = readInput("day17/Day17")
    part1(input).println()
    part2(input).println()
}
