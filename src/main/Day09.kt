import kotlin.system.measureTimeMillis

fun main() {
    val day09ExampleInput = readInput("Day09_Test")
//    assertEquals(6 + 7 + 9 + 11 + 6 + 18, Day09.part1(day09ExampleInput))
    assertEquals(9 + 20 + 241920 + 445L, Day09.part2(day09ExampleInput))

    val day09Input = readInput("Day09_Input")
    val exeTime = measureTimeMillis {
        val part1Output = Day09.part1(day09Input)
        val part2Output = Day09.part2(day09Input)

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

object Day09 {
    fun part1(input: List<String>) = input.sumOf { line ->
        var pointer = 0
        var newString = ""
        while (pointer < line.length) {
            val c = line[pointer]
            if (c in 'A'..'Z') {
                newString += c
                pointer++
            } else if (c == '(') {
                var block = c.toString()
                while (block.last() != ')') {
                    block += line[++pointer]
                }
                pointer++
                val (amt, reps) = block.filter { it.isLetterOrDigit() }.split("x").map { it.toInt() }
                for (i in 0 until reps) {
                    newString += line.substring(pointer, pointer + amt)
                }
                pointer += amt
            }
        }
        newString.length
    }


    fun part2(input: List<String>) = input.sumOf { recurseParse(it, 1).values.sum() }

    fun recurseParse(line: String, repetitions: Int): MutableMap<Char, Long> {
        val map = mutableMapOf<Char, Long>()
        var pointer = 0
        while (pointer < line.length) {
            val c = line[pointer]
            if (c.isLetter()) {
                map[c] = (map[c] ?: 0) + repetitions
                pointer++
            } else {
                var b = c.toString()
                while (b.last() != ')') {
                    b += line[++pointer]
                }
                pointer++
                val (amt, reps) = b.filter { it.isLetterOrDigit() }.split("x").map { it.toInt() }
                val newMap = recurseParse(line.substring(pointer, pointer + amt), reps)
                newMap.entries.forEach { (k, v) ->
                    map[k] = (map[k] ?: 0) + (v * repetitions)
                }
                pointer += amt
            }
        }
        return map
    }
}
