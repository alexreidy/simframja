package simframja.graphics

import javafx.scene.paint.Color
import simframja.Spatial

interface VisualElement : Spatial {

    val color: Color

}

interface Visual {

    val visualElements: Iterable<VisualElement>

}