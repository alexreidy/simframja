package simframja

abstract class ShapeEntity<T : Entity<T>> : AbstractEntity<T>() {

    private val _boxes = ArrayList<MutableBox>()

    override var boxes: List<Box> = _boxes

    fun addBox(box: MutableBox) {
        _boxes.add(box)
    }

    fun removeBox(box: MutableBox) {
        _boxes.remove(box)
    }

    override fun setPosition(x: Double, y: Double) {
        val lastpos = getPosition().copy()
        super.setPosition(x, y)
        val pos = getPosition()
        val offset: Vector2 = pos - lastpos
        println("offset is " + offset.toString())
        for (box in _boxes) {
            println("moving by offset ${offset.x}")
            box.move(offset)
        }
    }

}