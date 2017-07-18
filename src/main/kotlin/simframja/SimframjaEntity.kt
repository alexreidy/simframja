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

    var localBoxColor: Color = Color.BLACK

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

    override fun partsInContactWith(thing: Spatial): Set<T> {
        if (!thing.boundingBox.isTouching(this.boundingBox)) return emptySet()
        val contacts = findPartsInContactWith(thing)
        if (contacts.isEmpty() && anyLocalBoxesTouch(thing)) {
            contacts.add(this as T)
        }
        return contacts
    }

    override fun isTouching(thing: Spatial): Boolean {
        if (!this.boundingBox.isTouching(thing.boundingBox)) {
            return false
        }
        return anyLocalBoxesTouch(thing) || isTouching(thing, boundingBoxShortCircuit = false)
    }

    override fun render() {
        if (!visible) return

        renderer?.render(localBoxes, localBoxColor)
        for (constituent in constituents) {
            constituent.render()
        }
    }

}