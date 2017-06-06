package simframja

import javafx.animation.AnimationTimer
import javafx.application.Application
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.paint.Color
import javafx.stage.Stage
import simframja.graphics.SimframjaCanvas
import simframja.graphics.Visual
import simframja.graphics.VisualElement
import kotlin.concurrent.thread

private const val WIDTH = 500.0
private const val HEIGHT = 400.0

class Thing(x: Double, y: Double) : ShapeEntity<Thing>(), Visual, VisualElement {
    override fun onCollisionWith(other: Thing) {}

    override fun whileTouching(other: Thing) {}

    init {
        addBox(MutableBox(0.0,0.0,10.0,10.0))
        addBox(MutableBox(10.0, 10.0, 10.0, 10.0))
        setPosition(x, y)
    }

    override val color = Color.GREEN

    override val visualElements = listOf(this)

}

fun main(args: Array<String>) {
    val canvas = SimframjaCanvas.createAndDisplayInWindow("Simframja", WIDTH, HEIGHT)

    canvas.backgroundColor = Color.BLANCHEDALMOND

    val things = ArrayList<Thing>()
    val thing1 = Thing(10.0, 10.0)
    val thing2 = Thing(50.0, 50.0)
    things.addAll(listOf(thing1, thing2))

    while (true) {
        canvas.render(things)
        thing1.move(0.5, 0.5)
        Thread.sleep(30)
    }

}