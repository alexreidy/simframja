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
     * Phantoms will show up as contacts but they will never show up in
     * a collision or contact _event_.
     * The idea is: Phantoms are detectable but not acted upon
     * during the normal collision handling process.
     * If your normal reaction (in the collision event handler, say) is to attack
     * whatever touches you, phantoms won't be subject to this attack, but you
     * could still see that the phantom is a contact.
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