package simframja

abstract class CompoundEntity<T : Entity<T>> : AbstractEntity<T>() {

    private val _constituents = ArrayList<T>()

    protected val constituents: Iterable<T> = _constituents

    fun addEntity(ent: T) {
        _constituents.add(ent)
        handleConstituentChange()
    }

    fun removeEntity(ent: T) {
        _constituents.remove(ent)
        handleConstituentChange()
    }

    private fun handleConstituentChange() {
        clearBoundingBoxCache()
        cachedBoxes = null
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

    protected fun setPositionAndGetOffset(x: Double, y: Double): Vector2 {
        val offset = ImmutableVector2(x, y) - getPosition()

        super.setPosition(x, y)

        for (constituent in constituents) {
            constituent.move(offset)
        }

        return offset
    }

    override fun setPosition(x: Double, y: Double) {
        setPositionAndGetOffset(x, y)
    }

    private val contacts = ArrayList<T>(15)

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

    override fun isTouching(thing: Spatial): Boolean {
        // todo:
        // is this actually faster in most cases than the
        // default "check every box" impl?
        if (!this.boundingBox.isTouching(thing.boundingBox)) {
            return false
        }
        for (constituent in constituents) {
            if (constituent.isTouching(thing)) {
                return true
            }
        }
        return false
    }

}