import kotlin.system.measureTimeMillis

fun main() {
    val day04ExampleInput = readInput("Day04_Test")
    assertEquals(123 + 987 + 404, Day04.part1(day04ExampleInput))
//    assertEquals(4, Day04.part2(day04ExampleInput))

    val day04Input = readInput("Day04_Input")
    val exeTime = measureTimeMillis {
        val part1Output = Day04.part1(day04Input)
        val part2Output = Day04.part2(day04Input)

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

object Day04 {
    fun part1(input: List<String>) = input.sumOf { line ->
        val trip = line.indexOfFirst { it.isDigit() }.let { idx ->
            Triple(
                line.substring(0, idx).filter { it.isLetter() },
                line.substring(idx, idx + 3).toInt(),
                line.substring(idx + 4, idx + 9).toCharArray().toList()
            )
        }
        if (trip.first.toCharArray().distinct().sortedBy { it }.sortedByDescending { c -> trip.first.count { it == c } }
                .take(5) == trip.third) trip.second else 0
    }

    fun part2(input: List<String>): Int = input.firstNotNullOf { line ->
        val trip = line.indexOfFirst { it.isDigit() }.let { idx ->
            Triple(
                line.substring(0, idx),
                line.substring(idx, idx + 3).toInt(),
                line.substring(idx + 4, idx + 9).toCharArray().toList()
            )
        }
        if (trip.first.filter { it.isLetter() }.toCharArray().distinct().sortedBy { it }
                .sortedByDescending { c -> trip.first.count { it == c } }
                .take(5) != trip.third) return@firstNotNullOf null
        trip.second.takeIf {
            trip.first.toCharArray().joinToString("") {
                var a = it + (trip.second % 26)
                if (a > 'z') a -= 26
                (if (a in 'a'..'z') a else ' ').toString()
            } == "northpole object storage "
        }
    }
}
