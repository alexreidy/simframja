package simframja

import org.junit.Test
import org.junit.Assert.*
import simframja.tools.computeBoundingBoxOver

class BoxTests {

    @Test
    fun computeBoundingBox() {
        val b1 = MutableBox(100.0, 100.0, 10.0, 20.0)
        val b2 = MutableBox(110.0, 110.0, 20.0, 20.0)
        val bb = computeBoundingBoxOver(listOf(b1, b2))
        assertTrue(bb!!.width == 30.0)
        assertTrue(bb.height == 30.0)
    }

    @Test
    fun canCheckIfBoxesAreTouching() {
        val b1 = MutableBox(0.0, 0.0, 10.0, 10.0)
        val b2 = MutableBox(-5.0, 1.0, 100.0, 1.0)
        assertTrue(b1.isTouching(b2))
        assertTrue(b2.isTouching(b1))
        val b3 = MutableBox(-10.0, -10.0, 100.0, 100.0)
        val zb = MutableBox(0.0, 0.0, 0.0, 0.0)
        assertTrue(b3.isTouching(zb))
        assertTrue(zb.isTouching(b3))
    }

}