package simframja

import org.junit.Test
import org.junit.Assert.*

class VectorTests {

    @Test
    fun canAddVectors() {
        val a = ImmutableVector2(10.0, 10.0)
        val b = ImmutableVector2(5.0, 0.0)
        val c = a + b
        assertTrue(c.x == 15.0)
        assertTrue(c.y == 10.0)
    }

    @Test
    fun canSubtractVectors() {
        
    }

}