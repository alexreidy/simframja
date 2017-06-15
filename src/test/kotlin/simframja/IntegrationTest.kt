package simframja

import javafx.scene.paint.Color
import simframja.graphics.SimframjaCanvas
import simframja.tools.RandomNumberTool

private const val WIDTH = 500.0
private const val HEIGHT = 400.0

open class Thing(x: Double, y: Double) : SimframjaEntity<Thing>() {
    override fun onCollisionWith(other: Thing) {}

    override fun whileTouching(other: Thing) {}

    init {
        addLocalBox(MutableBox(0.0,0.0,10.0,10.0))
        addLocalBox(MutableBox(10.0, 10.0, 10.0, 10.0))
        val rn = RandomNumberTool()
        for (i in 1..100) {
            addLocalBox(MutableBox(
                    rn.rsign(rn.rin(30.0)), rn.rsign(rn.rin(30.0)), rn.rsign(rn.rin(30.0)), rn.rsign(rn.rin(30.0))))
        }
        setPosition(x, y)
        localBoxColor = Color.GREEN
    }
}

fun main(args: Array<String>) {
    val canvas = SimframjaCanvas.createAndDisplayInWindow("Simframja", WIDTH, HEIGHT)

    canvas.backgroundColor = Color.BLANCHEDALMOND

    val things = ArrayList<Thing>()
    val thing1 = Thing(10.0, 10.0)
    val thing2 = Thing(50.0, 50.0)
    thing2.localBoxColor = Color.CYAN
    thing1.addEntity(thing2)

    val monster = Thing(100.0, 100.0)
    monster.localBoxColor = Color.RED

    things.addAll(listOf(thing1, thing2, monster))

    things.forEach { it.renderer = canvas.renderer }

    monster.move(80.0, 80.0)

    val rn = RandomNumberTool()

    while (true) {
        canvas.render(things)
        thing1.move(0.5, 0.5)
        monster.move(rn.rsign(rn.rin(1.0)), rn.rsign(rn.rin(1.0)))
        Thread.sleep(30)
    }

}