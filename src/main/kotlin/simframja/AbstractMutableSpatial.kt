package simframja

import simframja.tools.computeBoundingBox

open abstract class AbstractMutableSpatial : MutableSpatial {

    private val _position: MutableVector2 = MutableVector2()

    override fun getPosition(): Vector2 = _position

    override fun setPosition(x: Double, y: Double) {
        _position.x = x
        _position.y = y
    }

    override fun setPosition(pos: Vector2) {
        setPosition(pos.x, pos.y)
    }

    override fun move(xOffset: Double, yOffset: Double) {
        _position.x += xOffset
        _position.y += yOffset
    }

    override fun move(offset: Vector2) {
        move(offset.x, offset.y)
    }

    private var cachedBoundingBox: Box? = null

    protected fun clearBoundingBoxCache() {
        cachedBoundingBox = null
    }

    protected open fun computeBoundingBox(): Box = computeBoundingBox(boxes)

    override val boundingBox: Box
        get() {
            if (cachedBoundingBox != null) {
                return cachedBoundingBox!!
            }
            cachedBoundingBox = computeBoundingBox()
            return cachedBoundingBox!!
        }

    override fun isTouching(other: Spatial): Boolean {
        if (!this.boundingBox.isTouching(other.boundingBox)) {
            return false
        }
        for (box in boxes) {
            for (otherBox in other.boxes) {
                if (box.isTouching(otherBox)) {
                    return true
                }
            }
        }
        return false
    }

}