package simframja

import javafx.scene.paint.Color
import simframja.graphics.SimframjaCanvas
import simframja.tools.RandomNumberTool
import simframja.tools.computeBoundingBoxFor

private const val WIDTH = 500.0
private const val HEIGHT = 400.0

open class Thing() : SimframjaEntity<Thing>() {
    override fun onCollisionWith(other: Thing) {
        println("collision!")
    }

    override fun whileTouching(other: Thing) {
        println("yay")
        other.localBoxColor = Color.HOTPINK
    }

    constructor(x: Double, y: Double) : this() {
        addLocalBox(MutableBox(0.0,0.0,10.0,10.0))
        addLocalBox(MutableBox(10.0, 10.0, 10.0, 10.0))
        val rn = RandomNumberTool()
        for (i in 1..5) {
            addLocalBox(MutableBox(
                    rn.rsign(rn.rin(30.0)), rn.rsign(rn.rin(30.0)), (rn.rin(30.0)+4),(rn.rin(30.0)+4)))
        }
        setPosition(x, y)
        localBoxColor = Color.GREEN
    }
}

class BoundingBox : Thing() {

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

    things.forEach { it.renderer = canvas.renderer }

    monster.move(80.0, 80.0)

    val rn = RandomNumberTool()

    while (true) {
        things.forEach { it.handleCollisionsAndGetContacts(things) }
        println("touching monster? " + thing1.isTouching(monster))
        thing1.move(rn.rsign(rn.rin(1.0)), rn.rsign(rn.rin(1.0)))

        canvas.render(things)

        canvas.renderer.render(listOf(thing1.boundingBox, thing2.boundingBox, monster.boundingBox), Color(1.0, .5, .5, 0.2))

        val v = thing1.getPosition() - monster.getPosition()
        monster.move(v.norm * 3.0)

        Thread.sleep(30)
    }

}