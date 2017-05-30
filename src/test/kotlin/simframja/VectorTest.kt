package simframja

import org.junit.Test
import org.junit.Assert.*

class VectorTest {

    @Test
    fun canAddVectors() {
        val a = Vector2(10.0, 10.0)
        val b = Vector2(5.0, 0.0)
        val c = a + b
        assertTrue(c.x == 15.0)
        assertTrue(c.y == 10.0)
    }

}