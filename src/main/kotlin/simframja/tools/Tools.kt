package simframja.tools

import simframja.Box
import simframja.MutableBox
import simframja.Spatial

internal fun <T> iterableOf(vararg iterables: Iterable<T>): Iterable<T> {
    val iterablesIterator = iterables.iterator()
    var currentIterator =
            if (iterablesIterator.hasNext()) iterablesIterator.next().iterator()
            else return emptyList()

    return object : Iterable<T> {
        override fun iterator(): Iterator<T> {
            return object : AbstractIterator<T>() {
                override fun computeNext() {
                    if (!currentIterator.hasNext()) {
                        if (iterablesIterator.hasNext()) {
                            currentIterator = iterablesIterator.next().iterator()
                        } else {
                            done()
                            return
                        }
                    }
                    setNext(currentIterator.next())
                }
            }
        }
    }
}

private fun computeBoundingBoxForBoxes(boxes: Iterable<Box>): MutableBox {
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
    return MutableBox(xMin, yMin, xMax - xMin, yMax - yMin)
}

fun computeBoundingBoxFor(spatials: Iterable<Spatial>): MutableBox {
    val boxes = ArrayList<Box>()
    for (spatial in spatials) boxes.addAll(spatial.boxes)
    return computeBoundingBoxForBoxes(boxes)
}