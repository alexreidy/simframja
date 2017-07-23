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

    var localBoxColor: Color = Color.GREEN

    override fun render() {
        if (!visible) return
        renderer?.render(localBoxes, localBoxColor)
        for (constituent in directConstituents) {
            constituent.render()
        }
    }

    private var _products = ArrayList<T>()

    protected fun addProduct(product: T) {
        _products.add(product)
    }

    protected fun clearProducts() {
        _products.clear()
    }

    /**
     * Things the entity has produced. Each time you access products, you're taking
     * ownership of the container of products; the entity will let go and subsequently
     * add products to a new container until _it_ gets taken.
     * Typically this is a way for an entity to offer things up to be added to the
     * pool of entities in the simulation. For example, if it's launching projectiles,
     * this is where they will first show up.
     */
    val products: Collection<T>
        get() {
            if (_products.isEmpty()) return emptyList()
            val retval = _products
            _products = ArrayList()
            return retval
        }

    open fun update() {}

}