package simframja.tools

class RandomNumberTool {

    fun rin(range: Double): Double = Math.random() * range // Todo: use concurrent RNG.

    fun rsign(n: Double): Double = if (rin(1.0) > 0.5) n else -n

}