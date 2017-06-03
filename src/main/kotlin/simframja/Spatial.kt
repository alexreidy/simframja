package simframja

interface Spatial {

    fun getPosition(): Vector2

    val boundingBox: Box

    val boxes: List<Box>

    fun isTouching(thing: Spatial): Boolean

}

interface MutableSpatial : Spatial {

    fun setPosition(x: Double, y: Double)

    fun setPosition(pos: Vector2)

    fun move(xOffset: Double, yOffset: Double)

    fun move(offset: Vector2)

}