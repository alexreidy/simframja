package simframja

import simframja.tools.Event
import simframja.tools.StandardEvent

abstract class AbstractEntity<T : Entity<T>> : AbstractMutableSpatial(), Entity<T> {

    override var isPhantom: Boolean = false
        protected set

    private val _collisionEvent = StandardEvent<T>()

    override val collisionEvent: Event<T> = _collisionEvent

    private val _contactEvent = StandardEvent<T>()

    override val contactEvent: Event<T> = _contactEvent

    private val contacts = ArrayList<T>()

    /**
     * Searches the sequence for entities that are touching this one.
     * This powers `handleCollisionsAndGetContacts()`, so this is all you need
     * to override if you want to change how contact detection works.
     */
    protected open fun findContacts(ents: Iterable<T>): Collection<T> {
        contacts.clear()
        for (ent in ents) {
            if (this.isTouching(ent) && !ent.isPhantom) {
                contacts.add(ent)
            }
        }
        return contacts
    }

    private val previousContacts = HashSet<T>()

    //todo
    // should we break this up? findContacts(), collideWith...?
    // or maybe have recursive findContacts() as readonly alt to this:??

    override fun handleCollisionsAndGetContacts(ents: Iterable<T>): Collection<T> {
        val potentialContacts = ents.asSequence().filter {
            this !== it && this.boundingBox.isTouching(it.boundingBox)
        }
        val contacts = findContacts(potentialContacts.asIterable())
        for (contact in contacts) {
            if (!contact.isPhantom) { // todo remove?
                if (contact !in previousContacts) {
                    _collisionEvent.fireWith(contact)
                }
                _contactEvent.fireWith(contact)
            }
            previousContacts.clear()
            previousContacts.addAll(contacts)
        }
        return contacts
    }

}