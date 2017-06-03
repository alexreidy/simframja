package simframja.tools

import simframja.Box
import simframja.MutableBox
import simframja.Spatial

private fun computeBoundingBoxFromBoxes(boxes: Iterable<Box>): MutableBox {
    var xMin = Double.MAX_VALUE
    var yMin = Double.MAX_VALUE
    var xMax = Double.MIN_VALUE
    var yMax = Double.MIN_VALUE
    var pos: Double
    for (box in boxes) {
        val boxpos = box.getPosition()
        if (boxpos.x < xMin) {
            xMin = boxpos.x
        }
        if (boxpos.y < yMin) {
            yMin = boxpos.y
        }
        pos = boxpos.x + box.width
        if (pos > xMax) {
            xMax = pos
        }
        pos = boxpos.y + box.height
        if (pos > yMax) {
            yMax = pos
        }
    }
    return MutableBox(xMin, yMin, xMax, yMax)
}

fun computeBoundingBox(spatials: Iterable<Spatial>): MutableBox {
    val boxes = ArrayList<Box>()
    for (spatial in spatials) boxes.addAll(spatial.boxes)
    return computeBoundingBox(boxes)
}