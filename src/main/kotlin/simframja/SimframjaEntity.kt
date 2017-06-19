package simframja

import javafx.scene.paint.Color
import simframja.graphics.Renderer
import simframja.graphics.Visual

abstract class SimframjaEntity<T : SimframjaEntity<T>> : CompoundEntity<T>(), Visual {

    var renderer: Renderer? = null

    /**
     * If false, this will not be rendered.
     */
    var visible = true

    var _localBoxes = ArrayList<MutableBox>()

    /**
     * The boxes attached directly to this entity (as distinguished from boxes that are considered
     * part of this entity, but which come from constituents).
     */
    val localBoxes: Iterable<Box> = _localBoxes

    var localBoxColor: Color = Color.BLACK

    fun addLocalBox(box: MutableBox) {
        _localBoxes.add(box)
    }

    fun removeLocalBox(box: MutableBox) {
        _localBoxes.remove(box)
    }

    override val boxes: Iterable<Box> get() = localBoxes + super.boxes

    override fun setPositionAndGetOffset(x: Double, y: Double): Vector2 {
        val offset = super.setPositionAndGetOffset(x, y)
        for (localBox in _localBoxes) {
            localBox.move(offset)
        }
        return offset
    }

    override fun isTouching(thing: Spatial): Boolean {
        if (!this.boundingBox.isTouching(thing.boundingBox)) {
            return false
        }
        return isTouching(thing, boundingBoxShortCircuit = false) ||
                boxes.any { it.isTouching(thing) }
    }

    override fun render() {
        if (!visible) {
            return
        }
        renderer?.render(localBoxes, localBoxColor)
        for (constituent in constituents) {
            constituent.render()
        }
    }

}