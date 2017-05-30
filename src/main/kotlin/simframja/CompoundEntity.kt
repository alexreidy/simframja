package simframja

abstract class CompoundEntity<T : Entity<T>> : Entity<T> {

    override val position: Vector2
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override fun onCollisionWith(other: T) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setPosition(x: Double, y: Double) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun whileTouching(other: T) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setPosition(pos: Vector2) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun handleCollisionsAndGetContacts(context: Iterable<T>): List<T> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun move(xOffset: Double, yOffset: Double) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun move(offset: Vector2) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override val boundingBox: Box
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override val boxes: List<Box>
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override fun isTouching(other: Spatial): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}