import kotlin.system.measureTimeMillis

fun main() {


    val day11Test = readInput("Day11_Test")
    val day11Input = readInput("Day11_Input")

    val exeTime = measureTimeMillis {
        val part1Example = Day11.part1(day11Test)
        assertEquals(11,part1Example)

        val part1Output = Day11.part1(day11Input)
        val part2Output = Day11.part2(day11Input)

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

/**
 * pairs - one Generator, one Microchip
 * chips can generate shield when they have their corresponding generator
 * if chip is not powered and room contains another generator, chip dies
 * goal - bring all chips and generators up
 * elevator - can only hold 2 things with you
 * need to have at least one thing with you
 * cannot skip floors
 *
 * safe - chipX, chipY
 * safe - chipX, geneX
 * safe - chipX, geneX, geneY
 * safe - geneX, geneY
 */
object Day11 {
    fun part1(input: List<String>): Int {
        var lowestSolution = Int.MAX_VALUE
        val initialGameState = readInput(input)
        var states = listOf(initialGameState)
        while (states.isNotEmpty()) {
            val (finished, newCandidates) = states.flatMap { gs -> gs.possibleMoves().map { gs.applyMove(it) } }
                .filter { it.moves.size <= lowestSolution }
                .partition { it.finished() }
            finished.forEach { lowestSolution = minOf(lowestSolution, it.moves.size) }
            states = newCandidates
            println("${states.size} $lowestSolution")
        }
        return lowestSolution
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    sealed interface Movable {
        var element: String
    }

    data class Microchip(override var element: String) : Movable
    data class Generator(override var element: String) : Movable
    data class Move(val item1: Movable, val item2: Movable?, val prevFloor: Int, val nextFloor: Int) {
        fun isValid(state: GameState): Boolean {
            val allItems = (state.floors[nextFloor].items + item1 + item2).filterNotNull()
            val allChips = allItems.filterIsInstance<Microchip>()
            if (allChips.isEmpty()) return true
            val allGenerators = allItems.filterIsInstance<Generator>()
            if (allGenerators.isEmpty()) return true
            allChips.forEach { c -> if (allGenerators.none { it.element == c.element }) return false }
            state.moves.indexOfFirst { it == this }.takeIf { it > 2 }?.let { matchIndex ->
                if (state.moves.last() == state.moves[matchIndex - 1] && state.moves[state.moves.lastIndex - 1] == state.moves[matchIndex - 2])
                    return false
            }
            return true
        }
    }

    data class Floor(val items: MutableList<Movable> = mutableListOf())
    class GameState(
        val moves: MutableList<Move> = mutableListOf(),
        var floors: MutableList<Floor> = mutableListOf(),
        var currentFloor: Int = 0
    ) {
        fun finished() = (0..2).all { floors[it].items.isEmpty() }

        fun applyMove(move: Move): GameState {
            this.let { originalState ->
                val floors = originalState.floors.apply {
                    this[move.prevFloor].items.remove(move.item1)
                    this[move.nextFloor].items.add(move.item1)
                    move.item2?.let {
                        this[move.prevFloor].items.remove(it)
                        this[move.nextFloor].items.add(it)
                    }
                }
                return GameState(
                    moves = (originalState.moves + move).toMutableList(),
                    floors = floors,
                    currentFloor = move.nextFloor
                )
            }
        }

        fun possibleMoves(): List<Move> {
            return floors[currentFloor].items.flatMap { item1 ->
                (floors[currentFloor].items - item1 + null).flatMap { item2 ->
                    listOf(-1, 1).map { Move(item1, item2, currentFloor, currentFloor + it) }
                }
            }
                .filter { it.nextFloor in 0..3 }
                .filter { it.isValid(this) }
        }
    }

    fun readInput(input: List<String>): GameState {
        val floors = input.map { line ->
            val (_, items) = line.split("contains")
            val movables = items.split(",", "and").mapNotNull {
                if (it.contains("microchip")) {
                    Microchip(it.substringAfter("a ").substringBefore("-compatible"))
                } else if (it.contains("generator")) {
                    Generator(it.substringAfter("a ").substringBefore(" generator"))
                } else null
            }
            Floor(movables.toMutableList())
        }
        return GameState(floors = floors.toMutableList())
    }
}
