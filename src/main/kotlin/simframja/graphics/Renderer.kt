package simframja.graphics

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import simframja.Box

interface Renderer {

    fun render(boxes: Iterable<Box>, color: Color)

}

class JavaFxCanvasRenderer(var graphicsContext: GraphicsContext) : Renderer {

    override fun render(boxes: Iterable<Box>, color: Color) {
        graphicsContext.fill = color
        for (box in boxes) {
            graphicsContext.fillRect(box.position.x, box.position.y, box.width, box.height)
        }
    }

}