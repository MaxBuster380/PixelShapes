package pixelshapes.mutablepixelshapes

import pixelshapes.MutablePixelShapeTest
import kotlin.test.Test
import kotlin.test.assertTrue

class SetShapeTest {
    /**
     * Check if, when only using .add, all the points give True when running .contains
     */
    @Test
    fun containsKeepsAddedPoints() {
        assertTrue(MutablePixelShapeTest().containsKeepsAddedPoints(
            SetShape()
        ))
    }

    /**
     * Checks if a removed point gives False when running .contains
     */
    @Test
    fun containsExcludesRemovedPoints() {
        assertTrue(MutablePixelShapeTest().containsExcludesRemovedPoints(
            SetShape()
        ))
    }

    /**
     * Checks if the iterator gives all and only the points included in the shape.
     */
    @Test
    fun iteratorCheck() {
        assertTrue(MutablePixelShapeTest().iteratorCheck(
            SetShape()
        ))
    }
}