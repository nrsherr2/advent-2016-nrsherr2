import kotlin.system.measureTimeMillis

fun main() {
//    val day05ExampleInput = readInput("Day05_Test")
    assertEquals("18f47a30", Day05.part1("abc"))
    assertEquals("05ace8e3", Day05.part2("abc"))
//    assertEquals("5DB3", Day05.part2(day05ExampleInput))
//    assertEquals(4, Day05.part2(listOf("R8, R4, R4, R8")))

    val day05Input = "wtnhxymk"
    val exeTime = measureTimeMillis {
        val part1Output = Day05.part1(day05Input)
        val part2Output = Day05.part2(day05Input)

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

object Day05 {
    fun part1(input: String): String {
        var str = ""
        var idx = 0
        while (str.length < 8) {
            val md5 = "$input$idx".md5().padStart(32, '0')
            if (md5.startsWith("00000")) {
                println(idx)
                println(md5)
                str += md5[5]
            }
            idx++
        }
        return str
    }

    fun part2(input: String): String {
        val str = MutableList(8) { '_' }
        var idx = 0
        while (str.any { it == '_' }) {
            val md5 = "$input$idx".md5().padStart(32, '0')
            if (md5.startsWith("00000")) {
                println(idx)
                println(md5)
                md5[5].takeIf { it.isDigit() && it in '0'..'7' && str[it.digitToInt()] == '_' }
                    ?.let { str[it.digitToInt()] = md5[6] }
            }
            idx++
        }
        return str.joinToString("") { it.toString() }
    }
}
