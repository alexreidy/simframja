package simframja

import javafx.scene.paint.Color
import simframja.graphics.SimframjaCanvas
import simframja.tools.RandomNumberTool

private const val WIDTH = 800.0
private const val HEIGHT = 450.0

open class Thing() : SimframjaEntity<Thing>() {

    override var isPhantom: Boolean = false

    var name = ""

    public override var isFrozen = false


    fun makeRandomBox(): MutableBox {
        val size = rn.rin(18.0)+4
        return MutableBox(
                rn.rsign(rn.rin(30.0)), rn.rsign(rn.rin(30.0)), size, size)
    }

    val growingBox = makeRandomBox()

    override fun addEntity(ent: Thing) {
        ent.allowedMovers.remove(AnyMover)
        ent.allowedMovers.add(this)
        super.addEntity(ent)
    }

    constructor(x: Double, y: Double, nboxes: Int = 5) : this() {
        val rn = RandomNumberTool()
        for (i in 1..nboxes) {
            if (i == 1) {
                addLocalBox(growingBox)
                continue
            }
            addLocalBox(makeRandomBox())
        }
        setPosition(x, y)
        localBoxColor = Color.GREEN

        contactEvent.addHandler { contact ->
            contact.localBoxColor = localBoxColor
            contact.move(simframja.rn.rsign(simframja.rn.rin(5.0)), simframja.rn.rsign(simframja.rn.rin(5.0)))
        }

    }
}

private val rn = RandomNumberTool()
fun randomColor(): Color {
    return Color(rn.rin(1.0), rn.rin(1.0), rn.rin(1.0), 1.0)
}

fun main(args: Array<String>) {
    val canvas = SimframjaCanvas.createAndDisplayInWindow("A Groovy Mosaic", WIDTH, HEIGHT)

    canvas.backgroundColor = Color.BLANCHEDALMOND

    val things = ArrayList<Thing>()
    val thing1 = Thing(150.0, 150.0)
    val thing2 = Thing(150.0, 150.0)

    thing2.renderer = canvas.renderer
    thing2.localBoxColor = Color.CYAN
    thing1.addEntity(thing2)
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

    val rn = RandomNumberTool()

    for (i in 1..350) {
        val thing = Thing(rn.rin(WIDTH), rn.rin(HEIGHT), nboxes = 1)
        val t = Thing(thing.position.x, thing.position.y, 3)
        t.localBoxColor = Color.CYAN
        t.renderer = canvas.renderer
        thing.addEntity(t)
        things.add(thing)
        thing.localBoxColor = randomColor()
    }

    things.forEach { it.renderer = canvas.renderer }

    var n = 0
    var up = true
    while (true) {
        things.forEach { it.handleCollisionsAndGetContacts(things) }
        things.forEach {  it.move(rn.rsign(rn.rin(1.0)), rn.rsign(rn.rin(1.0))) }

        canvas.render(things)

        canvas.renderer.render(
                listOf(thing1.boundingBox, thing2.boundingBox, monster.boundingBox), Color(1.0, .5, .5, 0.2))

        val v = thing1.position - monster.position
        monster.move(v.norm * 3.0)

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


        if (rn.rin(1.0) > 0.97) {
            val pdiff = thing1.position - thing2.position
            println("initial pos diff = $initialPositionDiff; now it is $pdiff")
        }

        Thread.sleep(30)
    }

}