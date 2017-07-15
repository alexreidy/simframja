package simframja

import simframja.tools.Event

interface Spatial {

    val position: Vector2

    val boundingBox: Box

    val boxes: Iterable<Box>

    fun isTouching(thing: Spatial): Boolean

}

open class BoundingBoxChangedMessage

interface MutableSpatial : Spatial {

    fun setPosition(x: Double, y: Double)

    fun setPosition(pos: Vector2)

    fun move(xOffset: Double, yOffset: Double)

    fun move(offset: Vector2)

    /**
     * Fires when the `boundingBox` can potentially return a box that is spatially
     * different from the one it returned before this event. In general this means
     * "FYI - if you were caching my bounding box, it's probably outdated now."
     */
    val boundingBoxChangedEvent: Event<BoundingBoxChangedMessage>

    /**
     * Temporarily disables the `boundingBoxChangedEvent` while the given action is executed.
     * todo: should this really be exposed here?
     */
    fun withoutFiringBoundingBoxChangedEvent(action: () -> Unit)

}