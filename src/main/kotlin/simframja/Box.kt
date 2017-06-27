package simframja

import java.util.Collections.singletonList

interface Box : Spatial {

    val width: Double

    val height: Double

}

class MutableBox : AbstractMutableSpatial, Box {

    constructor(x: Double, y: Double, width: Double, height: Double) {
        require(width >= 0 && height >= 0, { "Width and height must be at least zero; got ($width, $height)" })
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
        val otherpos = box.position
        if (otherpos.x > position.x + width) return false
        if (otherpos.y > position.y + height) return false
        if (otherpos.x + box.width < position.x) return false
        if (otherpos.y + box.height < position.y) return false
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

    override fun toString(): String {
        return "MutableBox(x = ${position.x}, y = ${position.y}, width = $width, height = $height)"
    }

}