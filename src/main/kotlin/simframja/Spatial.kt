package simframja

import simframja.tools.Event

interface Spatial {

    val position: Vector2

    val boundingBox: Box

    val boxes: Iterable<Box>

    fun isTouching(thing: Spatial): Boolean

}

open class BoundingBoxChangedMessage

/**
 * The default mover (see `move` and `setPosition` methods).
 */
object AnyMover

interface MutableSpatial : Spatial {

    fun setPosition(x: Double, y: Double, mover: Any = AnyMover)

    fun setPosition(pos: Vector2, mover: Any = AnyMover)

    fun move(xOffset: Double, yOffset: Double, mover: Any = AnyMover)

    fun move(offset: Vector2, mover: Any = AnyMover)

    /**
     * Fires when the `boundingBox` can potentially return a box that is spatially
     * different from the one it returned before this event. In general this means
     * "FYI - if you were caching my bounding box, it's probably outdated now."
     */
    val boundingBoxChangedEvent: Event<BoundingBoxChangedMessage>

    /**
     * Temporarily disables the `boundingBoxChangedEvent` while the given action is executed.
     */
    fun withoutFiringBoundingBoxChangedEvent(action: () -> Unit)

    /**
     * Temporarily enables mobility while the action is executed.
     */
    fun overridingFrozenStatus(action: () -> Unit)

}