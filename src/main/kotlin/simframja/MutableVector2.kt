package simframja

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