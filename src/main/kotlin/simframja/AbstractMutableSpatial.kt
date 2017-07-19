package simframja

import simframja.tools.Event
import simframja.tools.StandardEvent
import simframja.tools.computeBoundingBoxOver

abstract class AbstractMutableSpatial : MutableSpatial {

    private val _position = MutableVector2()

    override val position: Vector2 = _position

    /**
     * When this is true, position changes will not work unless called from within
     * an `overridingFrozenStatus` block.
     */
    protected open var isFrozen = false

    private val _boundingBoxChangedEvent = StandardEvent<BoundingBoxChangedMessage>()

    override val boundingBoxChangedEvent: Event<BoundingBoxChangedMessage> = _boundingBoxChangedEvent

    private val BOUNDS_CHANGED_MESSAGE = BoundingBoxChangedMessage()

    protected fun fireBoundingBoxChangedEvent() {
        _boundingBoxChangedEvent.fireWith(BOUNDS_CHANGED_MESSAGE)
    }

    final override fun withoutFiringBoundingBoxChangedEvent(action: () -> Unit) {
        if (!_boundingBoxChangedEvent.isEnabled) {
            action.invoke()
            return
        }
        _boundingBoxChangedEvent.isEnabled = false
        action.invoke()
        _boundingBoxChangedEvent.isEnabled = true
    }

    final override fun overridingFrozenStatus(action: () -> Unit) {
        if (!isFrozen) {
            action.invoke()
            return
        }
        isFrozen = false
        action.invoke()
        isFrozen = true
    }

    /**
     * Called from `setPosition()` after the position vector has been updated but before
     * `finishSetPosition()`. Override this when "setting position" means more than
     * just changing coordinate values.
     * @param x The new and current x coordinate.
     * @param y The new and current y coordinate.
     * @param offset The change required to move from the previous position to the current position.
     */
    protected open fun handleSetPosition(x: Double, y: Double, offset: Vector2) {}

    /**
     * Called last in `setPosition()`. Moves the cached bounding box, fires events, and
     * generally finalizes the operation. Override with caution.
     * @param x The new and current x coordinate.
     * @param y The new and current y coordinate.
     * @param offset The change required to move from the previous position to the current position.
     */
    protected open fun finishSetPosition(x: Double, y: Double, offset: Vector2) {
        cachedBoundingBox?.move(offset)
        fireBoundingBoxChangedEvent()
    }

    final override fun setPosition(x: Double, y: Double) {
        if (isFrozen) return
        val offset = ImmutableVector2(x, y) - _position
        _position.x = x
        _position.y = y
        handleSetPosition(x, y, offset)
        finishSetPosition(x, y, offset)
    }

    final override fun setPosition(pos: Vector2) {
        setPosition(pos.x, pos.y)
    }

    final override fun move(xOffset: Double, yOffset: Double) {
        setPosition(position.x + xOffset, position.y + yOffset)
    }

    final override fun move(offset: Vector2) {
        move(offset.x, offset.y)
    }

    private var cachedBoundingBox: MutableBox? = null

    protected fun clearBoundingBoxCache() {
        cachedBoundingBox = null
    }

    /**
     * This is used by the `boundingBox` property, which caches the result.
     */
    protected open fun computeBoundingBox(): MutableBox {
        return computeBoundingBoxOver(boxes) ?:
                MutableBox(position.x, position.y, width = 0.0, height = 0.0)
    }

    override val boundingBox: Box
        get() {
            if (cachedBoundingBox != null) {
                return cachedBoundingBox!!
            }
            cachedBoundingBox = computeBoundingBox()
            return cachedBoundingBox!!
        }

    override fun isTouching(thing: Spatial): Boolean {
        if (!this.boundingBox.isTouching(thing.boundingBox)) {
            return false
        }
        for (box in boxes) {
            for (otherBox in thing.boxes) {
                if (box.isTouching(otherBox)) {
                    return true
                }
            }
        }
        return false
    }

}