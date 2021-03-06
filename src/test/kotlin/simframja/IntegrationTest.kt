package simframja

import javafx.scene.paint.Color
import simframja.graphics.SimframjaCanvas
import simframja.tools.rin
import simframja.tools.rsign

private const val WIDTH = 800.0
private const val HEIGHT = 450.0

open class Thing() : SimframjaEntity<Thing>() {

    override var isPhantom: Boolean = false
        public set

    var name = ""

    public override var isFrozen = false


    fun makeRandomBox(): MutableBox {
        val size =rin(18.0)+4
        return MutableBox(
                rsign(rin(30.0)), rsign(rin(30.0)), size, size)
    }

    val growingBox = makeRandomBox()

    override fun addEntity(ent: Thing, isContactTransitive: Boolean) {
        ent.setCanBeMovedBy(AnyMover, false)
        ent.setCanBeMovedBy(this)
        super.addEntity(ent, isContactTransitive)
    }

    constructor(x: Double, y: Double, nboxes: Int = 5) : this() {
        for (i in 1..nboxes) {
            if (i == 1) {
                addLocalBox(growingBox)
                continue
            }
            addLocalBox(makeRandomBox())
        }
        setPosition(x, y)
        localBoxColor = Color.GREEN

        collisionEvent.addHandler { println("I, $this, collided with $it") }

        contactEvent.addHandler { contact ->
            contact.localBoxColor = localBoxColor
            contact.move(rsign(rin(5.0)), rsign(rin(5.0)))
        }

    }
}

fun randomColor(): Color {
    return Color(rin(1.0), rin(1.0), rin(1.0), 1.0)
}

fun main(args: Array<String>) {
    val canvas = SimframjaCanvas.createAndDisplayInWindow("A Groovy Mosaic", WIDTH, HEIGHT)

    val mpos = MutableVector2()

    canvas.setOnMouseMoved { mouseEvent -> mpos.x = mouseEvent.x; mpos.y = mouseEvent.y }

    canvas.backgroundColor = Color.BLANCHEDALMOND

    val things = ArrayList<Thing>()
    val thing1 = Thing(150.0, 150.0)
    val thing2 = Thing(150.0, 150.0)
    val t3 = Thing( 190.0, 190.0)
    t3.renderer = canvas.renderer
    thing2.renderer = canvas.renderer
    thing2.localBoxColor = Color.CYAN
    thing1.addEntity(thing2)
    thing1.addEntity(t3)
    //thing2.isFrozen = true

    val initialPositionDiff = thing1.position - thing2.position

    val monster = Thing(400.0, 400.0)
    //thing1.setPosition(180.0, 180.0)
    monster.localBoxColor = Color.RED

    things.addAll(listOf(thing1, monster))

    //monster.move(80.0, 80.0)
    //thing2.isPhantom = true
    monster.name = "monster"
    thing1.name = "thing1"
    thing2.name = "thing2"

    for (i in 1..1) {
        val thing = Thing(rin(WIDTH), rin(HEIGHT), nboxes = 1)
        //thing.isPhantom = true
        val t = Thing(thing.position.x + 25, thing.position.y + 25, 1)
        val t1 = Thing(thing.position.x + 55, thing.position.y + 55, 1)
        //t.isPhantom = true
        //t.localBoxColor = Color.CYAN
        t.renderer = canvas.renderer
        t1.renderer = canvas.renderer
        t1.localBoxColor = Color.DARKCYAN
        thing.addEntity(t)
        thing.addEntity(t1)
        things.add(thing)
        thing.localBoxColor = randomColor()
    }

    things.forEach { it.renderer = canvas.renderer }

    var n = 0
    var up = true
    while (true) {
        things.forEach { it.handleCollisionsAndGetContacts(things) }
        things.forEach {  it.move(rsign(rin(1.0)), rsign(rin(1.0))) }

        canvas.render(things)

        canvas.renderer.render(
                listOf(thing1.boundingBox, thing2.boundingBox, monster.boundingBox), Color(1.0, .5, .5, 0.2))

        val v = thing1.position - monster.position
        monster.move(v.norm * 3.0)

        monster.setPosition(mpos)

        if (n >= 300) {
            up = false
        }
        if (n <= 0) {
            up = true
        }

        //thing2.move(.5, .5)
        if (up) {
            thing1.growingBox.width += 1
            n++
        } else {
            thing1.growingBox.width -= 1
            n--
        }


        if (rin(1.0) > 0.97) {
            val pdiff = thing1.position - thing2.position
            println("initial pos diff = $initialPositionDiff; now it is $pdiff")
        }

        Thread.sleep(30)
    }

}