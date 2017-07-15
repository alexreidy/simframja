package simframja

import simframja.tools.Event

interface Entity<T : Entity<T>> : MutableSpatial {

    /**
     * Called by `handleCollisionsAndGetContacts()` when this entity collides with another.
     * A collision occurs when an entity comes into contact with this one after
     * having not been in contact during the last check.
     */
    val collisionEvent: Event<T>

    /**
     * Called by `handleCollisionsAndGetContacts()` for every entity that this is touching.
     */
    val contactEvent: Event<T>

    /**
     * Finds ents that are in contact and calls appropriate collision functions.
     * See `onCollisionWith()` and `whileTouching()`.
     */
    fun handleCollisionsAndGetContacts(ents: Iterable<T>): Collection<T>

    /**
     * Phantoms are detected during collision handling and considered contacts, but
     * `whileTouching(phantom)` and `onCollisionWith(phantom)` are never called.
     * You're aware of the phantom, and when in contact it can affect you,
     * but you can't affect it.
     */
    val isPhantom: Boolean

}