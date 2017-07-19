package simframja

import simframja.tools.computeBoundingBoxOver

abstract class CompoundEntity<T : Entity<T>> : AbstractEntity<T>() {

    private val _constituents = ArrayList<T>()

    protected val constituents: Iterable<T> = _constituents

    private var _localBoxes = ArrayList<MutableBox>()

    /**
     * The boxes attached directly to this entity (as distinguished from boxes that are considered
     * part of this entity, but which come from constituents).
     */
    val localBoxes: Iterable<Box> = _localBoxes

    fun addLocalBox(box: MutableBox) {
        _localBoxes.add(box)
        box.boundingBoxChangedEvent.addHandler(this::onConstituentBoundingBoxChanged)
    }

    fun removeLocalBox(box: MutableBox) {
        _localBoxes.remove(box)
        box.boundingBoxChangedEvent.removeHandler(this::onConstituentBoundingBoxChanged)
    }

    private var cachedConstituentBoxes: ArrayList<Box>? = null

    private val constituentBoxes: Iterable<Box>
        get() {
            if (cachedConstituentBoxes != null) {
                return cachedConstituentBoxes!!
            }
            cachedConstituentBoxes = ArrayList<Box>()
            for (ent in constituents) {
                cachedConstituentBoxes!!.addAll(ent.boxes)
            }
            return cachedConstituentBoxes!!
        }

    override val boxes: Iterable<Box> get() = localBoxes + constituentBoxes // todo: cache?

    private fun computeBoundingBoxOverConstituents(): MutableBox {
        val bb: MutableBox? = computeBoundingBoxOver(constituents.map { it.boundingBox })
        if (bb != null) return bb
        return MutableBox(position.x, position.y, width = 0.0, height = 0.0)
    }

    override fun computeBoundingBox(): MutableBox {
        val boxlist = ArrayList<Box>(_localBoxes.size + 1)
        boxlist.addAll(_localBoxes)
        boxlist.add(computeBoundingBoxOverConstituents())
        return computeBoundingBoxOver(boxlist) ?:
                MutableBox(position.x, position.y, width = 0.0, height = 0.0)
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

    protected fun onConstituentBoundingBoxChanged(message: BoundingBoxChangedMessage) {
        handleConstituentChange()
    }

    private fun handleConstituentChange() {
        clearBoundingBoxCache()
        cachedConstituentBoxes = null
        fireBoundingBoxChangedEvent()
    }

    override fun handleSetPosition(x: Double, y: Double, offset: Vector2) {
        _localBoxes.forEach { it.withoutFiringBoundingBoxChangedEvent { it.move(offset) } }
        _constituents.forEach { it.withoutFiringBoundingBoxChangedEvent { it.move(offset) } }
    }

    /**
     * Recycled by `findContacts()`.
     */
    protected val contacts = ArrayList<T>()

    override fun handleCollisionsAndGetContacts(ents: Iterable<T>): Collection<T> {
        contacts.clear()
        val potentialContacts = findPotentialContactsAmong(ents)
        for (potentialContact in potentialContacts) {
            contacts.addAll(potentialContact.partsInContactWith(localBoxes))
        }
        for (constituent in constituents) {
            contacts.addAll(
                    constituent.handleCollisionsAndGetContacts(
                            potentialContacts.asIterable()))
        }
        handleCollisionsWith(contacts)
        return contacts
    }

    private inline fun anyLocalBoxesTouch(thing: Spatial) = localBoxes.any { it.isTouching(thing) }

    override fun partsInContactWith(spatials: Iterable<Spatial>): Set<T> {
        val contacts = HashSet<T>()
        for (constituent in constituents) {
            contacts.addAll(constituent.partsInContactWith(spatials))
        }
        if (contacts.isEmpty()) {
            for (spatial in spatials) {
                if (anyLocalBoxesTouch(spatial)) {
                    contacts.add(this as T)
                }
            }
        } else {
            contacts.add(this as T)
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
        constituents.forEach { if (it.isTouching(thing)) return true }
        return false
    }

    override fun isTouching(thing: Spatial): Boolean {
        if (!this.boundingBox.isTouching(thing.boundingBox)) {
            return false
        }
        return anyLocalBoxesTouch(thing) || isTouching(thing, boundingBoxShortCircuit = false)
    }

}