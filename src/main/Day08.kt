import kotlin.system.measureTimeMillis

fun main() {
    val day08ExampleInput = readInput("Day08_Test")
    assertEquals(6, Day08.part1(day08ExampleInput))
//    assertEquals("advent", Day08.part2(day08ExampleInput))

    val day08Input = readInput("Day08_Input")
    val exeTime = measureTimeMillis {
        val part1Output = Day08.part1(day08Input)
        val part2Output = Day08.part2(day08Input)

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

object Day08 {
    fun part1(input: List<String>): Int {
        val g = Grid()
        input.forEach { line ->
            if (line.startsWith("rect")) {
                val (width, length) = line.split(" ")[1].split("x").map { it.toInt() }
                g.rect(width, length)
            }
            if (line.startsWith("rotate row y=")) {
                val (rowNum, amt) = line.split("=")[1].split(" by ").map { it.toInt() }
                g.rotateRow(rowNum, amt)
            }
            if (line.startsWith("rotate column x=")) {
                val (colNum, amt) = line.split("=")[1].split(" by ").map { it.toInt() }
                g.rotateColumn(colNum, amt)
            }
        }
        println(g.display())
        return g.grid.flatten().count{it == true}
    }


    fun part2(input: List<String>): Int {
        return 0
    }


    class Grid {
        var grid = MutableList(6) { MutableList(50) { false } }
        fun rect(width: Int, height: Int) {
            for (rowNum in 0 until height) {
                for (colNum in 0 until width) {
                    grid[rowNum][colNum] = true
                }
            }
        }

        fun rotateRow(rowNo: Int, amt: Int) {
            for (i in 0 until amt) {
                grid[rowNo] = rotRow(rowNo).toMutableList()
            }
        }

        private fun rotRow(rowNo: Int): List<Boolean> {
            return listOf(grid[rowNo][49]) + (0..48).map { grid[rowNo][it] }
        }

        fun rotateColumn(colNo: Int, amt: Int) {
            for (i in 0 until amt) {
                val newCol = rotCol(colNo)
                for (j in 0..5) grid[j][colNo] = newCol[j]
            }
        }

        private fun rotCol(colNo: Int): List<Boolean> {
            return listOf(grid[5][colNo]) + (0..4).map { grid[it][colNo] }
        }

        fun display() = grid.joinToString("\n") { it.joinToString("") { if (it) "▓" else "░" } }
    }
}
