package simframja

import java.util.Collections.singletonList

interface Box : Spatial {

    val width: Double

    val height: Double

}

class MutableBox : AbstractMutableSpatial, Box {

    constructor(x: Double, y: Double, width: Double, height: Double) {
        require(width > 0 && height > 0, { "Width and height must be greater than zero" })
        this.width = width
        this.height = height
        setPosition(x, y)
    }

    override var width: Double = 1.0
        get() {
            return field
        }
        set(value) {
            field = value
            _boundingBoxChangedEvent.fireWith(boundingBox)
        }

    override var height: Double = 1.0
        get() {
            return field
        }
        set(value) {
            field = value
            _boundingBoxChangedEvent.fireWith(boundingBox)
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