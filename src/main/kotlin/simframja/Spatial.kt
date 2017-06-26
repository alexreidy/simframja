package simframja

import simframja.tools.Event

interface Spatial {

    fun getPosition(): Vector2

    val boundingBox: Box

    val boxes: Iterable<Box>

    fun isTouching(thing: Spatial): Boolean

}

interface MutableSpatial : Spatial {

    fun setPosition(x: Double, y: Double)

    fun setPosition(pos: Vector2)

    fun move(xOffset: Double, yOffset: Double)

    fun move(offset: Vector2)

    // todo: the following stuff is leaky.
    // Wouldn't want users to depend on that event since we manipulate when it's enabled.

    /**
     * Fires when the `boundingBox` changes in size or position.
     */
    val boundingBoxChangedEvent: Event<Box>

    /**
     * Temporarily disables the `boundingBoxChangedEvent` while the given action is executed.
     */
    fun withoutFiringBoundingBoxChangedEvent(action: () -> Unit)

}