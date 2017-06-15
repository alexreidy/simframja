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

    override val boxes: Iterable<Box> get() = super.boxes + localBoxes

    override fun move(xOffset: Double, yOffset: Double) {
        super.move(xOffset, yOffset)
        for (localBox in _localBoxes) {
            localBox.move(xOffset, yOffset)
        }
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