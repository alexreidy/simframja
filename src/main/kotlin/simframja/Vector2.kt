package simframja

/**
 * Represents a vector of two Doubles. This interface is read-only but
 * implementations may be mutable.
 */
interface Vector2 {

    val x: Double

    val y: Double

    val magnitude: Double

    fun copy(): Vector2

    fun makeMutableCopy(): MutableVector2

    operator fun plus(v: Vector2): Vector2

    operator fun minus(v: Vector2): Vector2

    operator fun times(scalar: Double): Vector2

    operator fun unaryMinus(): Vector2

}

class ImmutableVector2(x: Double, y: Double) : Vector2 by MutableVector2(x, y)

class MutableVector2(x: Double = 0.0, y: Double = 0.0) : Vector2 {

    override var x: Double = x

    override var y: Double = y

    override val magnitude: Double get() = Math.sqrt(x*x + y*y)

    override fun toString(): String = "($x, $y)"

    override fun copy(): MutableVector2 = MutableVector2(x, y)

    override fun makeMutableCopy(): MutableVector2 = copy()

    override operator fun plus(v: Vector2): MutableVector2 = MutableVector2(x + v.x, y + v.y)

    override operator fun minus(v: Vector2): MutableVector2 = MutableVector2(x - v.x, x - v.y)

    override operator fun times(scalar: Double): MutableVector2 = MutableVector2(x * scalar, y * scalar)

    override operator fun unaryMinus(): MutableVector2 = MutableVector2(-x, -y)

    operator fun plusAssign(v: Vector2) {
        x += v.x
        y += v.y
    }

    operator fun minusAssign(v: Vector2) {
        x -= v.x
        y -= v.y
    }

    operator fun timesAssign(v: Vector2) {
        x *= v.x
        y *= v.y
    }

    operator fun divAssign(v: Vector2) {
        x /= v.x
        y /= v.y
    }

}