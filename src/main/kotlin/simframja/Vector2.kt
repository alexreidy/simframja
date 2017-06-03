package simframja

open class Vector2(x: Double, y: Double) {

    constructor() : this(0.0, 0.0)

    open val x: Double = x

    open val y: Double = y

    val magnitude: Double get() = Math.sqrt(x*x + y*y)

    operator fun plus(v: Vector2): Vector2 {
        return Vector2(x + v.x, y + v.y)
    }

    operator fun minus(v: Vector2): Vector2 {
        return Vector2(x - v.x, x - v.y)
    }

    operator fun times(scalar: Float): Vector2 {
        return Vector2(x * scalar, y * scalar)
    }

    operator fun unaryMinus(): Vector2 = Vector2(-x, -y)

}

class MutableVector2(x: Double, y: Double) : Vector2() {

    constructor() : this(0.0, 0.0)

    override var x: Double = x

    override var y: Double = y

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