package simframja

interface Spatial {

    val position: Vector2

    fun setPosition(x: Double, y: Double)

    fun setPosition(pos: Vector2)

    fun move(xOffset: Double, yOffset: Double)

    fun move(offset: Vector2)

    val boundingBox: Box

    val boxes: List<Box>

    fun isTouching(other: Spatial): Boolean

}