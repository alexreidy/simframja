package simframja

import javafx.scene.paint.Color
import simframja.graphics.Renderer
import simframja.graphics.Visual
import simframja.tools.computeBoundingBoxOver

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
        box.boundingBoxChangedEvent.addHandler(this::onConstituentBoundingBoxChanged)
    }

    fun removeLocalBox(box: MutableBox) {
        _localBoxes.remove(box)
        box.boundingBoxChangedEvent.removeHandler(this::onConstituentBoundingBoxChanged)
    }

    override val boxes: Iterable<Box> get() = localBoxes + super.boxes // todo: cache?

    override fun computeBoundingBox(): MutableBox {
        val boxlist = ArrayList<Box>(_localBoxes.size + 1)
        boxlist.add(super.computeBoundingBox())
        boxlist.addAll(localBoxes)
        return computeBoundingBoxOver(boxlist) ?: MutableBox(position.x, position.y, 0.0, 0.0)
    }

    override fun handleSetPosition(x: Double, y: Double, offset: Vector2) {
        super.handleSetPosition(x, y, offset)
        for (localBox in _localBoxes) {
            localBox.withoutFiringBoundingBoxChangedEvent {
                localBox.move(offset)
            }
        }
    }

    private inline fun anyLocalBoxesTouch(thing: Spatial) = localBoxes.any { it.isTouching(thing) }

    override fun findContacts(ents: Iterable<T>): Collection<T> {
        super.findContacts(ents)
        for (ent in ents) {
            if (anyLocalBoxesTouch(ent)) {
                contacts.add(ent)
            }
        }
        return contacts
    }

    override fun isTouching(thing: Spatial): Boolean {
        if (!this.boundingBox.isTouching(thing.boundingBox)) {
            return false
        }
        return isTouching(thing, boundingBoxShortCircuit = false) || anyLocalBoxesTouch(thing)
    }

    override fun render() {
        if (!visible) return

        renderer?.render(localBoxes, localBoxColor)
        for (constituent in constituents) {
            constituent.render()
        }
    }

}