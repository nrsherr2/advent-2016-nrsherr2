import kotlin.system.measureTimeMillis

fun main() {
//    val day03ExampleInput = readInput("Day03_Test")
//    assertEquals(1985, Day03.part1(day03ExampleInput))
//    assertEquals(4, Day03.part2(listOf("R8, R4, R4, R8")))

    val day03Input = readInput("Day03_Input")
    val exeTime = measureTimeMillis {
        val part1Output = Day03.part1(day03Input)
        val part2Output = Day03.part2(day03Input)

        println(
            """
                *** PART 1 ***
                $part1Output
                *** PART 2 ***
                $part2Output
                ***  END  ***
            """.trimIndent()
        )
    }
    println("Processing Time: ${exeTime}ms")
}

object Day03 {
    fun part1(input: List<String>) = input.count {
        val (num1, num2, num3) = it.split("  ").filter { it.isNotBlank() }.map { it.trim().toInt() }
        (num2 + num3 > num1) && (num1 + num3 > num2) && (num1 + num2 > num3)
    }


    fun part2(input: List<String>): Int {
        return part1(input.map { it.split("  ").filter { it.isNotBlank() }.map { it.trim() } }.chunked(3).flatMap { chunk ->
            (0..2).map { i -> (0..2).map { j -> chunk[j][i] } }
        }.map { it.joinToString("  ") { it } })
    }
}
