package pixelshapes.mutablepixelshapes

import pixelshapes.MutablePixelShapeTest
import kotlin.test.Test
import kotlin.test.assertTrue

class SetMutablePixelShapeTest {

    companion object {
        private val LARGE_INTS = IntRange(Int.MIN_VALUE / 2, Int.MAX_VALUE / 2)
    }

    /**
     * Check if, when only using .add, all the points give True when running .contains
     */
    @Test
    fun containsKeepsAddedPoints() {
        assertTrue(MutablePixelShapeTest().containsKeepsAddedPoints(
            SetMutablePixelShape(),
            LARGE_INTS,
            LARGE_INTS
        ))
    }

    /**
     * Checks if a removed point gives False when running .contains
     */
    @Test
    fun containsExcludesRemovedPoints() {
        assertTrue(MutablePixelShapeTest().containsExcludesRemovedPoints(
            SetMutablePixelShape(),
            LARGE_INTS,
            LARGE_INTS
        ))
    }

    /**
     * Checks if the iterator gives all and only the points included in the shape.
     */
    @Test
    fun iteratorCheck() {
        assertTrue(MutablePixelShapeTest().iteratorCheck(
            SetMutablePixelShape(),
            LARGE_INTS,
            LARGE_INTS
        ))
    }
}