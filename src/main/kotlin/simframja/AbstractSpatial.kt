package simframja

open abstract class AbstractSpatial : Spatial {

    private val _position: MutableVector2 = MutableVector2()

    override val position: Vector2
        get() = _position

    override fun setPosition(x: Double, y: Double) {
        _position.x = x
        _position.y = y
    }

    override fun setPosition(pos: Vector2) {
        setPosition(pos.x, pos.y)
    }

    override fun move(xOffset: Double, yOffset: Double) {
        _position.x += xOffset
        _position.y += yOffset
    }

    override fun move(offset: Vector2) {
        move(offset.x, offset.y)
    }

    private var cachedBoundingBox: Box? = null

    override val boundingBox: Box
        get() {
            if (cachedBoundingBox != null) {
                return cachedBoundingBox!!
            }
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
            cachedBoundingBox = Box(xMin, yMin, xMax, yMax)
            return cachedBoundingBox!!
        }

    override fun isTouching(other: Spatial): Boolean {
        if (!this.boundingBox.isTouching(other.boundingBox)) {
            return false
        }
        for (box in boxes) {
            for (otherBox in other.boxes) {
                if (box.isTouching(otherBox)) {
                    return true
                }
            }
        }
        return false
    }

}