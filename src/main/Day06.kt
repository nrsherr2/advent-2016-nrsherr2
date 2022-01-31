import kotlin.system.measureTimeMillis

fun main() {
    val day06ExampleInput = readInput("Day06_Test")
    assertEquals("easter", Day06.part1(day06ExampleInput))
    assertEquals("advent", Day06.part2(day06ExampleInput))

    val day06Input = readInput("Day06_Input")
    val exeTime = measureTimeMillis {
        val part1Output = Day06.part1(day06Input)
        val part2Output = Day06.part2(day06Input)

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

object Day06 {
    fun part1(input: List<String>): String {
        return (0 until input.first().length).joinToString("") { idx ->
            input.map { it[idx] }.groupingBy { it }.eachCount().maxByOrNull { it.value }!!.key.toString()
        }
    }


    fun part2(input: List<String>): String {
        return (0 until input.first().length).joinToString("") { idx ->
            input.map { it[idx] }.groupingBy { it }.eachCount().minByOrNull { it.value }!!.key.toString()
        }
    }


}
