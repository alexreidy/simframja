package simframja

import simframja.tools.Event

interface Entity<T : Entity<T>> : MutableSpatial {

    /**
     * Fired by `handleCollisionsAndGetContacts()` when this entity collides with another.
     * A collision occurs when an entity comes into contact with this one after
     * having not been in contact during the last check.
     */
    val collisionEvent: Event<T>

    /**
     * Fired by `handleCollisionsAndGetContacts()` for every entity that this is touching.
     */
    val contactEvent: Event<T>

    /**
     * Phantoms do not show up as contacts even when they are physically in contact.
     */
    val isPhantom: Boolean

    /**
     * Returns any constituents of this entity (including itself) that
     * are touching any of the given `spatials`.
     */
    fun partsInContactWith(spatials: Iterable<Spatial>): Set<T>

    /**
     * Finds ents that are in contact and fires appropriate collision events.
     */
    fun handleCollisionsAndGetContacts(ents: Iterable<T>): Collection<T>

}