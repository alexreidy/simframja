package simframja

abstract class AbstractEntity<T : Entity<T>> : AbstractMutableSpatial(), Entity<T> {

    override var isPhantom: Boolean = false
        protected set

    private val contacts = ArrayList<T>()

    /**
     * Searches the sequence for entities that are touching this one.
     * This powers `handleCollisionsAndGetContacts()`, so this is all you need
     * to override if you want to change how contact detection works.
     */
    protected open fun findContacts(ents: Iterable<T>): Collection<T> {
        contacts.clear()
        for (ent in ents) {
            if (this.isTouching(ent)) {
                contacts.add(ent)
            }
        }
        return contacts
    }

    private val previousContacts = HashSet<T>()

    override fun handleCollisionsAndGetContacts(ents: Iterable<T>): Collection<T> {
        val potentialContacts = ents.asSequence().filter {
            this !== it && this.boundingBox.isTouching(it.boundingBox)
        }
        val contacts = findContacts(potentialContacts.asIterable())
        for (contact in contacts) {
            if (!contact.isPhantom) {
                if (contact !in previousContacts) {
                    onCollisionWith(contact)
                }
                whileTouching(contact)
            }
            previousContacts.clear()
            previousContacts.addAll(contacts)
        }
        return contacts
    }

}