package simframja

interface Entity<T : Entity<T>> : MutableSpatial {

    fun onCollisionWith(other: T)

    fun whileTouching(other: T)

    fun handleCollisionsAndGetContacts(context: Iterable<T>): Collection<T>

}