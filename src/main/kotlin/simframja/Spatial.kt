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
 * By default, if AnyMover is an allowed mover, MutableSpatials should
 * allow position-altering methods to execute regardless of the mover.
 */
object AnyMover

interface MutableSpatial : Spatial {

    fun setPosition(x: Double, y: Double, mover: Any = AnyMover)

    fun setPosition(pos: Vector2, mover: Any = AnyMover)

    fun move(xOffset: Double, yOffset: Double, mover: Any = AnyMover)

    fun move(offset: Vector2, mover: Any = AnyMover)

    /**
     * Adds or removes objects from the "allowed movers" set. By default,
     * AnyMover is in the set, so to restrict motion to specific movers,
     * first execute `setCanBeMovedBy(AnyMover, false)`.
     */
    fun setCanBeMovedBy(mover: Any, can: Boolean = true)

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
     * If the spatial is frozen, this temporarily enables mobility
     * while the action is executed.
     */
    fun overridingFrozenStatus(action: () -> Unit)

}