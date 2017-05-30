package simframja

import java.util.Collections.singletonList

class Box : AbstractSpatial {

    constructor(x: Double, y: Double, width: Double, height: Double) {
        setPosition(x, y)
        this.width = width
        this.height = height
    }

    var width: Double

    var height: Double

    private val thisBox = singletonList(this)

    override val boxes: List<Box> = thisBox

}