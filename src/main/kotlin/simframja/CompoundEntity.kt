package simframja

abstract class CompoundEntity<T : Entity<T>> : AbstractEntity<T>() {

    private val constituents = ArrayList<T>()

    fun addEntity(ent: T) {
        constituents.add(ent)
        handleConstituentChange()
    }

    fun removeEntity(ent: T) {
        constituents.remove(ent)
        handleConstituentChange()
    }

    private fun handleConstituentChange() {
        clearBoundingBoxCache()
        cachedBoxes = null
    }

    private var cachedBoxes: ArrayList<Box>? = null

    override val boxes: List<Box>
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

    private val contacts = ArrayList<T>(15)

    /**
     * Note: this calls `handleCollisionsAndGetContacts()` on constituents!
     * todo
     */
    override fun findContacts(ents: Sequence<T>): Collection<T> {
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