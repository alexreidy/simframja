package simframja

import simframja.tools.computeBoundingBoxOver

abstract class CompoundEntity<T : CompoundEntity<T>> : AbstractEntity<T>() {

    private val _directConstituents = ArrayList<T>()

    protected val directConstituents: Iterable<T> = _directConstituents

    protected val allConstituents: Iterable<T> get() = _directConstituents +
            _directConstituents.map { it.allConstituents }
            .flatten()

    /**
     * When this is true, this entity will consider itself a contact by extension
     * if any of its constituents return anything from partsInContactWith().
     */
    protected var isContactTransitive = false

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
            for (constituent in _directConstituents) {
                cachedConstituentBoxes!!.addAll(constituent.boxes)
            }
            return cachedConstituentBoxes!!
        }

    override val boxes: Iterable<Box> get() = localBoxes + constituentBoxes // todo: cache?

    private fun computeBoundingBoxOverConstituents(): MutableBox {
        val bb: MutableBox? = computeBoundingBoxOver(_directConstituents.map { it.boundingBox })
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

    /**
     * @param isContactTransitive Sets the ent's contact transitivity property.
     * When true, if the ent's constituents find contacts with partsInContactWith(),
     * it considers itself a contact by extension even if it's not a contact per se.
     */
    open fun addEntity(ent: T, isContactTransitive: Boolean = false) {
        ent.isContactTransitive = isContactTransitive
        _directConstituents.add(ent)
        ent.boundingBoxChangedEvent.addHandler(this::onConstituentBoundingBoxChanged)
        handleConstituentChange()
    }

    open fun removeEntity(ent: T) {
        _directConstituents.remove(ent)
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
        for (localBox in _localBoxes) {
            localBox.withoutFiringBoundingBoxChangedEvent {
                localBox.move(offset, mover = this)
            }
        }
        for (constituent in _directConstituents) {
            constituent.withoutFiringBoundingBoxChangedEvent {
                constituent.move(offset, mover = this)
            }
        }
    }

    /**
     * Recycled by `handleCollisionsAndGetContacts()`.
     */
    protected val contacts = ArrayList<T>()

    override fun handleCollisionsAndGetContacts(ents: Iterable<T>): Collection<T> {
        contacts.clear()
        val potentialContacts = findPotentialContactsAmong(ents)
        for (potentialContact in potentialContacts) {
            contacts.addAll(potentialContact.partsInContactWith(localBoxes))
        }
        for (constituent in _directConstituents) {
            contacts.addAll(
                    constituent.handleCollisionsAndGetContacts(
                            potentialContacts.asIterable()))
        }
        handleCollisionsWith(contacts)
        return contacts
    }

    protected inline fun anyLocalBoxesTouch(thing: Spatial) = localBoxes.any { it.isTouching(thing) }

    protected inline fun anyLocalBoxesTouchAny(things: Iterable<Spatial>): Boolean {
        for (thing in things) {
            if (anyLocalBoxesTouch(thing)) {
                return true
            }
        }
        return false
    }

    override fun partsInContactWith(spatials: Iterable<Spatial>): Set<T> {
        val contacts = HashSet<T>()
        for (constituent in _directConstituents) {
            contacts.addAll(constituent.partsInContactWith(spatials))
        }
        if (this.isContactTransitive && !contacts.isEmpty()) {
            contacts.add(this as T)
        } else if (anyLocalBoxesTouchAny(spatials)) {
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
        _directConstituents.forEach { if (it.isTouching(thing)) return true }
        return false
    }

}