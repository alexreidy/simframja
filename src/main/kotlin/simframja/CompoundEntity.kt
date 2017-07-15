package simframja

import simframja.tools.computeBoundingBoxOver

abstract class CompoundEntity<T : Entity<T>> : AbstractEntity<T>() {

    private val _constituents = ArrayList<T>()

    protected val constituents: Iterable<T> = _constituents

    override fun computeBoundingBox(): MutableBox {
        val bb: MutableBox? = computeBoundingBoxOver(constituents.map { it.boundingBox })
        if (bb != null) return bb

        return MutableBox(position.x, position.y, 0.0, 0.0)
    }

    protected fun onConstituentBoundingBoxChanged(message: BoundingBoxChangedMessage) {
        handleConstituentChange()
    }

    open fun addEntity(ent: T) {
        _constituents.add(ent)
        ent.boundingBoxChangedEvent.addHandler(this::onConstituentBoundingBoxChanged)
        handleConstituentChange()
    }

    open fun removeEntity(ent: T) {
        _constituents.remove(ent)
        ent.boundingBoxChangedEvent.removeHandler(this::onConstituentBoundingBoxChanged)
        handleConstituentChange()
    }

    private fun handleConstituentChange() {
        clearBoundingBoxCache()
        cachedBoxes = null
        fireBoundingBoxChangedEvent()
    }

    private var cachedBoxes: ArrayList<Box>? = null

    override val boxes: Iterable<Box>
        get() {
            if (cachedBoxes != null) {
                return cachedBoxes!!
            }
            cachedBoxes = ArrayList<Box>()
            for (ent in constituents) {
                cachedBoxes!!.addAll(ent.boxes)
            }
            return cachedBoxes!!
        }

    override fun handleSetPosition(x: Double, y: Double, offset: Vector2) {
        for (constituent in constituents) {
            constituent.withoutFiringBoundingBoxChangedEvent {
                constituent.move(offset)
            }
        }
    }

    protected val contacts = ArrayList<T>()

    /**
     * Note: this calls `handleCollisionsAndGetContacts()` on constituents!
     * todo
     */
    override fun findContacts(ents: Iterable<T>): Collection<T> {
        contacts.clear()
        for (constituent in constituents) {
            contacts.addAll(constituent.handleCollisionsAndGetContacts(ents))
        }
        return contacts
    }

    /**
     * @param boundingBoxShortCircuit
     * If true, immediately returns false if the thing.boundingBox doesn't touch this.boundingBox.
     */
    protected fun isTouching(thing: Spatial, boundingBoxShortCircuit: Boolean): Boolean {
        if (boundingBoxShortCircuit && !this.boundingBox.isTouching(thing.boundingBox)) {
            return false
        }
        // todo:
        // is this actually faster in most cases than the
        // default "check every box" impl?
        for (constituent in constituents) {
            if (constituent.isTouching(thing)) {
                return true
            }
        }
        return false
    }

    override fun isTouching(thing: Spatial): Boolean {
        return isTouching(thing, boundingBoxShortCircuit = true)
    }

}