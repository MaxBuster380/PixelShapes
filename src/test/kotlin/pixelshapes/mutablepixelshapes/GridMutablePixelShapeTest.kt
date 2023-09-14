package pixelshapes.mutablepixelshapes

import pixelshapes.MutablePixelShapeTest
import kotlin.test.Test
import kotlin.test.assertTrue

class GridMutablePixelShapeTest {
    companion object {
        private const val GRID_WIDTH = 100
        private const val GRID_HEIGHT = 75
    }

    /**
     * Check if, when only using .add, all the points give True when running .contains
     */
    @Test
    fun containsKeepsAddedPoints() {
        assertTrue(MutablePixelShapeTest().containsKeepsAddedPoints(
            GridMutablePixelShape(GRID_WIDTH, GRID_HEIGHT),
            0..<GRID_WIDTH,
            0..<GRID_HEIGHT
        ))
    }

    /**
     * Checks if a removed point gives False when running .contains
     */
    @Test
    fun containsExcludesRemovedPoints() {
        assertTrue(MutablePixelShapeTest().containsExcludesRemovedPoints(
            GridMutablePixelShape(GRID_WIDTH, GRID_HEIGHT),
            0..<GRID_WIDTH,
            0..<GRID_HEIGHT
        ))
    }

    /**
     * Checks if the iterator gives all and only the points included in the shape.
     */
    @Test
    fun iteratorCheck() {
        assertTrue(MutablePixelShapeTest().iteratorCheck(
            GridMutablePixelShape(GRID_WIDTH, GRID_HEIGHT),
            0..<GRID_WIDTH,
            0..<GRID_HEIGHT
        ))
    }
}