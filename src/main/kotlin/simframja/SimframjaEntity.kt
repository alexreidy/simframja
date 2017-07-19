package simframja

import javafx.scene.paint.Color
import simframja.graphics.Renderer
import simframja.graphics.Visual

abstract class SimframjaEntity<T : SimframjaEntity<T>> : CompoundEntity<T>(), Visual {

    var renderer: Renderer? = null

    /**
     * If false, this will not be rendered.
     */
    var visible = true

    var localBoxColor: Color = Color.BLACK

    override fun render() {
        if (!visible) return
        renderer?.render(localBoxes, localBoxColor)
        for (constituent in constituents) {
            constituent.render()
        }
    }

}