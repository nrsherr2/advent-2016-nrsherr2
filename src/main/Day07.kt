import kotlin.system.measureTimeMillis

fun main() {

    val day07Input = readInput("Day07_Input")
    val exeTime = measureTimeMillis {
        val part1Output = Day07.part1(day07Input)
        val part2Output = Day07.part2(day07Input)

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

object Day07 {
    fun part1(input: List<String>) = input.count { line ->
        val abbas = line.windowed(4, 1).mapIndexedNotNull { i, w ->
            if (w.take(2).reversed() == w.takeLast(2) && w[0] != w[1]) i else null
        }
        if (abbas.isEmpty()) return@count false
        abbas.none {
            val b4 = line.substring(0, it)
            val af = line.substring(it, line.length)
            b4.count { it == '[' } != b4.count { it == ']' } && af.count { it == '[' } != af.count { it == ']' }
        }
    }


    fun part2(input: List<String>) = input.count { line ->
        val abas = line.windowed(3, 1).mapIndexedNotNull { i, w ->
            if (w.first() == w.last() && w[0] != w[1]) i else null
        }
        val (outAbas, inAbas) = abas.partition {
            val b4 = line.substring(0, it)
            val af = line.substring(it, line.length)
            b4.count { it == '[' } != b4.count { it == ']' } && af.count { it == '[' } != af.count { it == ']' }
        }
        outAbas.any { o ->
            inAbas.any { i ->
                line[o] == line[i+1] && line[o+1] == line[i]
            }
        }
    }
}
