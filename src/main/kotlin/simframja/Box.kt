package simframja

import java.util.Collections.singletonList

interface Box : Spatial {

    val width: Double

    val height: Double

}

class MutableBox(
        x: Double,
        y: Double,
        override var width: Double,
        override var height: Double
) : AbstractMutableSpatial(), Box {

    init {
        require(width > 0 && height > 0, { "Width and height must be greater than zero" })
        setPosition(x, y)
    }

    override val boxes: Iterable<Box> = singletonList(this)

    override val boundingBox: Box = this

    override fun computeBoundingBox(): MutableBox = this

    private fun isTouchingBox(box: Box): Boolean {
        val mypos = getPosition()
        val otherpos = box.getPosition()
        if (otherpos.x > mypos.x + width) return false
        if (otherpos.y > mypos.y + height) return false
        if (otherpos.x + box.width < mypos.x) return false
        if (otherpos.y + box.height < mypos.y) return false
        return true
    }

    override fun isTouching(thing: Spatial): Boolean {
        if (!this.isTouchingBox(thing.boundingBox)) {
            return false
        }
        for (box in thing.boxes) {
            if (this.isTouchingBox(box)) {
                return true
            }
        }
        return false
    }

}