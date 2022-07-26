import kotlin.system.measureTimeMillis

fun main() {

    val day12Input = readInput("Day12_Input")
    val day12ExampleInput = readInput("Day12_Test")

    val exeTime = measureTimeMillis {
        val exampleA = Day12.part1(day12ExampleInput)
        assertEquals(42, exampleA)

        val part1Output = Day12.part1(day12Input)
        val part2Output = Day12.part2(day12Input)

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

object Day12 {
    fun part1(input: List<String>): Int {

return part(input,Cpt(1))
    }

    fun part2(input: List<String>): Int {
        return part(input,Cpt(2))
    }

    fun part(input: List<String>, computer: Cpt): Int {
        var idx = 0
        while (idx < input.size) run {
            val split = input[idx].split(" ")
            when (split[0]) {
                "cpy" -> {
                    val num = try {
                        split[1].toInt()
                    } catch (e: NumberFormatException) {
                        computer.registers[split[1]]!!
                    }
                    computer.cpy(split[2], num)
                }
                "inc" -> computer.inc(split[1])
                "dec" -> computer.dec(split[1])
                "jnz" -> if (computer.registers[split[1]] != 0) {
                    idx += split[2].toInt()
                    return@run
                }
            }
            idx++
        }
        return computer.registers["a"]!!
    }

    class Cpt(part: Int) {
        val registers = mutableMapOf("a" to 0, "b" to 0, "c" to part - 1, "d" to 0)

        fun cpy(register: String, value: Int) {
            registers[register] = value
        }

        fun inc(register: String) {
            val reg = registers[register]!!
            registers[register] = reg + 1
        }

        fun dec(register: String) {
            val reg = registers[register]!!
            registers[register] = reg - 1
        }
    }
}
