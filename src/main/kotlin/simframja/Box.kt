package simframja

import java.util.Collections.singletonList

interface Box : Spatial {

    val width: Double

    val height: Double

}

class MutableBox : AbstractMutableSpatial, Box {

    constructor(x: Double, y: Double, width: Double, height: Double) {
        setPosition(x, y)
        this.width = width
        this.height = height
    }

    override var width: Double

    override var height: Double

    override val boxes: List<Box> = singletonList(this)

    override fun computeBoundingBox(): Box = this

    override val boundingBox: Box = this

}