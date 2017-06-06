package simframja.graphics

import javafx.application.Application
import javafx.application.Platform
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.paint.Color
import javafx.stage.Stage
import kotlin.concurrent.thread

private const val DEFAULT_TITLE = "Simframja App"

private var canvasForJfxApp: SimframjaCanvas? = null

class SimframjaCanvas constructor(
        var title: String = DEFAULT_TITLE,
        width: Double,
        height: Double
) : Canvas(width, height) {

    companion object Factory {
        /**
         * A helper for when you're too lazy to deal with JavaFX.
         */
        fun createAndDisplayInWindow(title: String = DEFAULT_TITLE, width: Double, height: Double): SimframjaCanvas {
            canvasForJfxApp = SimframjaCanvas(title, width, height)
            thread {
                Application.launch(JfxApp::class.java)
            }
            return canvasForJfxApp!!
        }
    }

    var backgroundColor: Color = Color.WHITE

    fun render(visuals: Iterable<Visual>) {
        graphicsContext2D.fill = backgroundColor
        graphicsContext2D.fillRect(0.0, 0.0, width, height)
        for (visual in visuals) {
            for (visualElement in visual.visualElements) {
                graphicsContext2D.fill = visualElement.color
                for (box in visualElement.boxes) {
                    val pos = box.getPosition()
                    graphicsContext2D.fillRect(pos.x, pos.y, box.width, box.height)
                }
            }
        }
    }

}

class JfxApp : Application() {

    override fun start(primaryStage: Stage) {
        val root = Group()
        root.children.add(canvasForJfxApp)
        primaryStage.scene = Scene(root)
        primaryStage.show()
        primaryStage.setOnCloseRequest {
            Platform.exit()
            System.exit(0)
        }
    }

}