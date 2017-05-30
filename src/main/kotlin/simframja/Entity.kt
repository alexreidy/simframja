package simframja

interface Entity<T : Entity<T>> : Spatial {

    fun onCollisionWith(other: T)

    fun whileTouching(other: T)

    fun handleCollisionsAndGetContacts(context: Iterable<T>): List<T>

}