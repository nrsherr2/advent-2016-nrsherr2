import kotlin.system.measureTimeMillis

fun main() {


    val day10Input = readInput("Day10_Input")
    val exeTime = measureTimeMillis {
        val part1Output = Day10.part1(day10Input)
        val part2Output = Day10.part2(day10Input)

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

object Day10 {
    fun part1(input: List<String>): Int {
        val bots = mutableListOf<Robot>()
        val outputs = mutableListOf<Output>()
        val (setup, action) = input.partition { it.startsWith("bot") }
        setup.forEach {
            val (l, r) = it.substring(4).split(" gives low to ")
            val baseBotNum = l.toInt()
            val baseBot = bots.firstOrNull { it.num == baseBotNum } ?: Robot(baseBotNum).also { bots.add(it) }
            val (lrr, rrr) = r.split(" and high to ")
            val ln = lrr.split(" ")[1].toInt()
            val lRec: Receiver = if (lrr.startsWith("bot")) {
                bots.firstOrNull { it.num == ln } ?: Robot(ln).also { bots.add(it) }
            } else {
                outputs.firstOrNull { it.num == ln } ?: Output(ln).also { outputs.add(it) }
            }
            if (baseBot.giveLow != null) throw IllegalArgumentException("Overwriting Give Low!")
            else baseBot.giveLow = lRec
            val rn = rrr.split(" ")[1].toInt()
            val rRec: Receiver = if (rrr.startsWith("bot")) {
                bots.firstOrNull { it.num == rn } ?: Robot(rn).also { bots.add(it) }
            } else {
                outputs.firstOrNull { it.num == rn } ?: Output(rn).also { outputs.add(it) }
            }
            if (baseBot.giveHigh != null) throw IllegalArgumentException("Overwriting Give High!")
            else baseBot.giveHigh = rRec
        }
        action.forEach { line ->
            val (value, botNum) = line.substring(6).split(" goes to bot ").map { it.toInt() }
            val bot = bots.firstOrNull { it.num == botNum } ?: Robot(botNum).also { bots.add(it) }
            try {
                bot.deposit(Chip(value))
            } catch (e: IllegalStateException) {
                return e.message!!.toInt()
            }
        }
        return 0
    }

    fun part2(input: List<String>): Int {
        val bots = mutableListOf<Robot>()
        val outputs = mutableListOf<Output>()
        val (setup, action) = input.partition { it.startsWith("bot") }
        setup.forEach {
            val (l, r) = it.substring(4).split(" gives low to ")
            val baseBotNum = l.toInt()
            val baseBot =
                bots.firstOrNull { it.num == baseBotNum } ?: Robot(baseBotNum, false).also { bots.add(it) }
            val (lrr, rrr) = r.split(" and high to ")
            val ln = lrr.split(" ")[1].toInt()
            val lRec: Receiver = if (lrr.startsWith("bot")) {
                bots.firstOrNull { it.num == ln } ?: Robot(ln, false).also { bots.add(it) }
            } else {
                outputs.firstOrNull { it.num == ln } ?: Output(ln).also { outputs.add(it) }
            }
            if (baseBot.giveLow != null) throw IllegalArgumentException("Overwriting Give Low!")
            else baseBot.giveLow = lRec
            val rn = rrr.split(" ")[1].toInt()
            val rRec: Receiver = if (rrr.startsWith("bot")) {
                bots.firstOrNull { it.num == rn } ?: Robot(rn, false).also { bots.add(it) }
            } else {
                outputs.firstOrNull { it.num == rn } ?: Output(rn).also { outputs.add(it) }
            }
            if (baseBot.giveHigh != null) throw IllegalArgumentException("Overwriting Give High!")
            else baseBot.giveHigh = rRec
        }
        action.forEach { line ->
            val (value, botNum) = line.substring(6).split(" goes to bot ").map { it.toInt() }
            val bot = bots.firstOrNull { it.num == botNum } ?: Robot(botNum, false).also { bots.add(it) }
            bot.deposit(Chip(value))
        }
        return outputs.filter { it.num in 0..2 }.flatMap { it.deposited.map { it.value } }.reduce { a, b -> a * b }
    }

    data class Chip(val value: Int)

    interface Receiver {
        fun deposit(chip: Chip)
        fun name(): String
    }

    class Robot(val num: Int, val part1: Boolean = true) : Receiver {
        val chips = mutableListOf<Chip>()
        var giveLow: Receiver? = null
        var giveHigh: Receiver? = null
        override fun deposit(chip: Chip) {
            chips.add(chip)
            if (part1 && chips.map { it.value }.sorted() == listOf(17, 61))
                throw IllegalStateException("$num")
            if (chips.size == 2) {
                val (hi, lo) = chips.sortedByDescending { it.value }
                if (giveHigh == null) throw IllegalArgumentException("Give High Is Null") else {
                    chips.remove(hi)
                    giveHigh!!.deposit(Chip(hi.value))
                }
                if (giveLow == null) throw IllegalArgumentException("Give Low Is Null") else {
                    chips.remove(lo)
                    giveLow!!.deposit(Chip(lo.value))
                }
            }
        }

        override fun name() = "Bot $num"
    }

    class Output(val num: Int) : Receiver {
        val deposited = mutableListOf<Chip>()
        override fun deposit(chip: Chip) {
            deposited.add(chip)
        }

        override fun name() = "Output $num"
    }
}
