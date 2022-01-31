import kotlin.system.measureTimeMillis

fun main() {
    val day02ExampleInput = readInput("Day02_Test")
    assertEquals(1985, Day02.part1(day02ExampleInput))
    assertEquals("5DB3", Day02.part2(day02ExampleInput))
//    assertEquals(4, Day02.part2(listOf("R8, R4, R4, R8")))

    val day02Input = readInput("Day02_Input")
    val exeTime = measureTimeMillis {
        val part1Output = Day02.part1(day02Input)
        val part2Output = Day02.part2(day02Input)

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

object Day02 {
    fun part1(input: List<String>): Int {
        val kp = KeyPad()
        val nums = input.joinToString("") { line ->
            line.forEach { kp.move(it) }
            kp.boop().toString()
        }
        return nums.toInt()
    }


    fun part2(input: List<String>): String {
        val kp = KeyPad2()
        val nums = input.joinToString("") { line ->
            line.forEach { kp.move(it) }
            kp.boop().toString(16)
        }.uppercase()
        return nums
    }

    class KeyPad(val location: Point = Point(1, 1)) {

        fun move(dir: Char) {
            when {
                dir == 'L' && location.colNum > 0 -> location.colNum -= 1
                dir == 'R' && location.colNum < 2 -> location.colNum += 1
                dir == 'U' && location.rowNum > 0 -> location.rowNum -= 1
                dir == 'D' && location.rowNum < 2 -> location.rowNum += 1
            }
        }

        fun boop() = (location.rowNum * 3) + (location.colNum + 1)
    }

    class KeyPad2(var location: Point = Point(2, 0)) {
        private fun futurePoint(dir: Char) = when (dir) {
            'U' -> Point(location.rowNum - 1, location.colNum)
            'D' -> Point(location.rowNum + 1, location.colNum)
            'L' -> Point(location.rowNum, location.colNum - 1)
            else -> Point(location.rowNum, location.colNum + 1)
        }

        private val validPoints = (listOf(0, 4).map { Point(it, 2) } +
                listOf(1, 3).flatMap { o -> listOf(1, 2, 3).map { Point(o, it) } } +
                (0..4).map { Point(2, it) }).sortedBy { "${it.rowNum} ${it.colNum}" }

        fun move(dir: Char) {
            futurePoint(dir).takeIf { it in validPoints }?.let { location = it }
        }

        fun boop() = validPoints.indexOf(location) + 1
    }
}
