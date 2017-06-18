package simframja

import simframja.tools.computeBoundingBoxFor

abstract class AbstractMutableSpatial : MutableSpatial {

    private val _position = MutableVector2()

    override fun getPosition(): Vector2 = _position

    protected open fun setPositionAndGetOffset(x: Double, y: Double): Vector2 {
        val offset = ImmutableVector2(x, y) - getPosition()

        _position.x = x
        _position.y = y

        cachedBoundingBox?.move(offset)

        return offset
    }

    final override fun setPosition(x: Double, y: Double) {
        setPositionAndGetOffset(x, y)
    }

    final override fun setPosition(pos: Vector2) {
        setPosition(pos.x, pos.y)
    }

    final override fun move(xOffset: Double, yOffset: Double) {
        setPosition(_position.x + xOffset, _position.y + yOffset)
    }

    final override fun move(offset: Vector2) {
        move(offset.x, offset.y)
    }

    private var cachedBoundingBox: MutableBox? = null

    protected fun clearBoundingBoxCache() {
        cachedBoundingBox = null
    }

    protected open fun computeBoundingBox(): MutableBox = computeBoundingBoxFor(boxes)

    override val boundingBox: Box
        get() {
            if (cachedBoundingBox != null) {
                return cachedBoundingBox!!
            }
            cachedBoundingBox = computeBoundingBox()
            return cachedBoundingBox!!
        }

    override fun isTouching(thing: Spatial): Boolean {
        if (!this.boundingBox.isTouching(thing.boundingBox)) {
            return false
        }
        for (box in boxes) {
            for (otherBox in thing.boxes) {
                if (box.isTouching(otherBox)) {
                    return true
                }
            }
        }
        return false
    }

}