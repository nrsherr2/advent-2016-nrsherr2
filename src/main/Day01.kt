import kotlin.math.abs
import kotlin.system.measureTimeMillis

fun main() {
    val day01ExampleInput = readInput("Day01_Test")
    assertEquals(12, Day01.part1(day01ExampleInput))
    assertEquals(4, Day01.part2(listOf("R8, R4, R4, R8")))

    val day01Input = readInput("Day01_Input")
    val exeTime = measureTimeMillis {
        val part1Output = Day01.part1(day01Input)
        val part2Output = Day01.part2(day01Input)

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

object Day01 {
    fun part1(input: List<String>): Int = Mover().apply {
        input.first().split(",").map { a ->
            Move(
                when (a.trim().first()) {
                    'R' -> MoveDir.RIGHT
                    else -> MoveDir.LEFT
                }, a.filter { it.isDigit() }.toInt()
            )
        }.forEach { move(it) }
    }.getManhattan()


    fun part2(input: List<String>): Int {
        val moves = input.first().split(",").map { a ->
            Move(
                when (a.trim().first()) {
                    'R' -> MoveDir.RIGHT
                    else -> MoveDir.LEFT
                }, a.filter { it.isDigit() }.toInt()
            )
        }
        val m = Mover()
        val locs = mutableListOf<Pair<Int, Int>>()
        moves.forEach {
            m.turn(it.dir)
            for (i in 0 until it.magnitude) {
                m.step()
                if (locs.contains(m.getLocation())) return m.getManhattan()
                else locs.add(m.getLocation())
            }
        }
        return 0
    }

    private class Mover {
        val movesInDir = mutableListOf(0, 0, 0, 0)
        var currentDirection: Int = 0
        fun move(move: Move) {
            turn(move.dir)
            for (i in 0 until move.magnitude) step()
        }

        fun turn(dir: MoveDir) {
            currentDirection = when (dir) {
                MoveDir.RIGHT -> (currentDirection + 1) % 4
                MoveDir.LEFT -> (currentDirection - 1).let { if (it < 0) 3 else it }
            }
        }

        fun step() {
            movesInDir[currentDirection] += 1
        }

        fun getLocation() = movesInDir[0] - movesInDir[2] to movesInDir[1] - movesInDir[3]

        fun getManhattan() = abs(movesInDir[0] - movesInDir[2]) + abs(movesInDir[1] - movesInDir[3])
    }

    private data class Move(val dir: MoveDir, val magnitude: Int)
    private enum class MoveDir { RIGHT, LEFT }
}
