import kotlin.system.measureTimeMillis

fun main() {

    val day13Input = readInput("Day13_Input")
    val day13ExampleInput = readInput("Day13_Test")


    val exeTime = measureTimeMillis {
        val exampleA = Day13.part1(day13ExampleInput)
        assertEquals(11, exampleA)

        val part1Output = Day13.part1(day13Input)
        val part2Output = Day13.part2(day13Input)

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

object Day13 {
    fun part1(input: List<String>): Int {
        val fNum = input[0].toInt()
        val destX = input[1].toInt()
        val destY = input[2].toInt()
        var nodes = listOf(Node(1, 1))
        while (nodes.none { it.x == destX && it.y == destY } && nodes.isNotEmpty()) {
            nodes = nodes.flatMap { it.neighbors(fNum) }
        }
        return nodes.first { it.x == destX && it.y == destY }.pathLen()
    }

    fun part2(input: List<String>): Int {
        val fNum = input[0].toInt()
        var nodes = listOf(Node(1, 1))
        val finalCoords = mutableSetOf(Pair(1, 1))
        (1..50).forEach {
            val neighborinos = nodes.flatMap { it.neighbors(fNum) }
            val coords = neighborinos.map { Pair(it.x, it.y) }
            finalCoords.addAll(coords)
            nodes = neighborinos
        }
        return finalCoords.size
    }

    private fun isOpen(x: Int, y: Int, fNum: Int): Boolean {
        val equation: Long = x * x + 3L * x + 2 * x * y + y + y * y
        val add = equation + fNum
        val binStr = add.toString(2)
        return binStr.count { it == '1' } % 2 == 0
    }

    private class Node(val x: Int, val y: Int, val prev: Node? = null) {
        fun pathContains(x: Int, y: Int): Boolean = when {
            this.x == x && this.y == y -> true
            prev == null -> false
            else -> prev.pathContains(x, y)
        }

        fun neighbors(fNum: Int): List<Node> {
            val coords = listOf(
                x - 1 to y,
                x + 1 to y,
                x to y - 1,
                x to y + 1
            )
                .filter { (a, b) -> a >= 0 && b >= 0 }
                .filter { (a, b) -> isOpen(a, b, fNum) }
                .filter { (a, b) -> !pathContains(a, b) }
            return coords.map { (a, b) -> Node(a, b, this) }
        }

        fun pathLen(i: Int = 0): Int {
            return prev?.pathLen(i + 1) ?: i
        }
    }
}
