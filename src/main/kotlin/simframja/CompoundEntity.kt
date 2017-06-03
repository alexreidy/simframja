package simframja

abstract class CompoundEntity<T : Entity<T>> : AbstractEntity<T>() {

    private val constituents = ArrayList<T>()

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

    override fun whileTouching(other: T) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCollisionWith(other: T) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun handleCollisionsAndGetContacts(context: Iterable<T>): Collection<T> {

    }

}