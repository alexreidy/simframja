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
        if (box.position.x < xMin) {
            xMin = box.position.x
        }
        if (box.position.y < yMin) {
            yMin = box.position.y
        }
        pos = box.position.x + box.width
        if (pos > xMax) {
            xMax = pos
        }
        pos = box.position.y + box.height
        if (pos > yMax) {
            yMax = pos
        }
    }
    return MutableBox(
            x = xMin,
            y = yMin,
            width = xMax - xMin,
            height = yMax - yMin)
}

/**
 * Computes the bounding box over the given spatials, returning
 * null when `spatials` is empty.
 */
fun computeBoundingBoxOver(spatials: Iterable<Spatial>): MutableBox? {
    val boxes = ArrayList<Box>()
    for (spatial in spatials) boxes.addAll(spatial.boxes)
    if (boxes.isEmpty()) return null
    return computeBoundingBoxForBoxes(boxes)
}