package simframja

import javafx.scene.paint.Color
import simframja.graphics.SimframjaCanvas
import simframja.tools.RandomNumberTool

private const val WIDTH = 800.0
private const val HEIGHT = 450.0

open class Thing() : SimframjaEntity<Thing>() {
    override fun onCollisionWith(other: Thing) {
        //println(name + " has collided with " + other.name)
    }

    override var isPhantom: Boolean = false

    override fun whileTouching(other: Thing) {
        other.localBoxColor = localBoxColor
        move(rn.rsign(rn.rin(5.0)), rn.rsign(rn.rin(5.0)))
    }

    var name = ""

    constructor(x: Double, y: Double, nboxes: Int = 5) : this() {
        val rn = RandomNumberTool()
        for (i in 1..nboxes) {
            val size = rn.rin(18.0)+4
            addLocalBox(MutableBox(
                    rn.rsign(rn.rin(30.0)), rn.rsign(rn.rin(30.0)), size, size))
        }
        setPosition(x, y)
        localBoxColor = Color.GREEN
    }
}

private val rn = RandomNumberTool()
fun randomColor(): Color {
    return Color(rn.rin(1.0), rn.rin(1.0), rn.rin(1.0), 1.0)
}

fun main(args: Array<String>) {
    val canvas = SimframjaCanvas.createAndDisplayInWindow("Simframja", WIDTH, HEIGHT)

    canvas.backgroundColor = Color.BLANCHEDALMOND

    val things = ArrayList<Thing>()
    val thing1 = Thing(10.0, 10.0)
    val thing2 = Thing(20.0, 20.0)
    thing2.renderer = canvas.renderer
    thing2.localBoxColor = Color.CYAN
    thing1.addEntity(thing2)

    val monster = Thing(400.0, 400.0)
    thing1.setPosition(180.0, 180.0)
    monster.localBoxColor = Color.RED

    things.addAll(listOf(thing1, monster))


    monster.move(80.0, 80.0)
    //thing2.isPhantom = true
    monster.name = "monster"
    thing1.name = "thing1"
    thing2.name = "thing2"

    val rn = RandomNumberTool()

    for (i in 1..1000) {
        val thing = Thing(rn.rin(WIDTH), rn.rin(HEIGHT), nboxes = 1)
        things.add(thing)
        thing.localBoxColor = randomColor()
    }

    things.forEach { it.renderer = canvas.renderer }

    while (true) {
        things.forEach { it.handleCollisionsAndGetContacts(things) }
        things.forEach {  it.move(rn.rsign(rn.rin(1.0)), rn.rsign(rn.rin(1.0))) }

        canvas.render(things)

        canvas.renderer.render(listOf(thing1.boundingBox, thing2.boundingBox, monster.boundingBox), Color(1.0, .5, .5, 0.2))

        val v = thing1.getPosition() - monster.getPosition()
        monster.move(v.norm * 3.0)

        Thread.sleep(30)
    }

}