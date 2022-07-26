import kotlin.system.measureTimeMillis

fun main() {

    //I'm too lazy to parse the plain-english input

    val day11Input = readInput("Day11_Input")
    val exeTime = measureTimeMillis {
        val part1Output = Day12.part1(day11Input)
        val part2Output = Day12.part2(day11Input)

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

object Day11 {
    fun part1(input: List<String>): Int {
        var lowestSolution = Int.MAX_VALUE

        return 0
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    sealed interface Movable {
        var element: String
    }

    data class Microchip(override var element: String) : Movable
    data class Generator(override var element: String) : Movable

    class GameState(
        val initialGameState: Map<Movable, Int>
    ) {
        val moves: MutableList<Move> = mutableListOf()

        fun calculateGameState(mvs: List<Move> = moves): Map<Movable, Int> {
            val s = initialGameState.toMutableMap()
            mvs.forEach { mv ->
                if (s[mv.item1] != mv.prevFloor || (mv.item2 != null && s[mv.item2] != mv.prevFloor)) {
                    throw IllegalArgumentException("Move calculation is bad!")
                }
                s[mv.item1] = mv.nextFloor
                if (mv.item2 != null) s[mv.item2] = mv.nextFloor
            }
            return s
        }

        fun fry() = calculateGameState().invert().any { (level, movables) ->
            val microchips: List<Microchip> = movables.filter { it is Microchip } as List<Microchip>
            if (microchips.isEmpty()) false
            val generators: List<Generator> = movables.filter { it is Generator } as List<Generator>
            if (generators.isEmpty()) false
            microchips.any { chip -> generators.none { gen -> gen.element == chip.element } }
        }

        fun satisfied() = calculateGameState().all { it.value == 4 }

        fun uniqueGameState(incomingMove: Move): Boolean {
            val newGameState = calculateGameState(moves + incomingMove)
            return (1..moves.size).none {
                val sl = moves.subList(0, it)
                val ngs = calculateGameState(sl)
                repeatGameState(ngs, newGameState)
            }
        }

        private fun repeatGameState(old: Map<Movable, Int>, new: Map<Movable, Int>) =
            old.entries.all { it.value == new[it.key] }

        private fun Map<Movable, Int>.invert(): Map<Int, List<Movable>> {
            return entries.groupBy { it.value }.mapValues { it.value.map { it.key } }
        }
    }

    data class Move(val item1: Movable, val item2: Movable?, val prevFloor: Int, val nextFloor: Int)

    val testInput = listOf("H-1", "Li-1", "HG-2", "LiG-3")
    val inputInput = listOf("Tm-1", "TmG-1", "PuG-1", "SrG-1", "Pu-2", "Sr-2", "PmG-3", "Pm-3", "RuG-3", "Ru-3")
}
