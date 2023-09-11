package pixelshapes.mutablepixelshapes

import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertTrue

class SetShapeTest {
    companion object {
        /**
         * When using arbitrary values, this is how many times you re-do the check.
         */
        const val RANDOM_CHECKS_COUNT = 100
    }

    /**
     * Check if, when only using .add, all the points give True when running .contains
     */
    @Test
    fun containsKeepsAddedPoints() {
        val shape = SetShape()

        val points = mutableListOf<Pair<Int, Int>>()
        for(i in 1..100) {
            val new = randomPoint()
            shape.add(new)
            points += new
        }

        for(point in points) {
            assertTrue(shape.contains(point))
        }
    }

    /**
     * Returns a new instance of a point with arbitrary coordinates.
     */
    private fun randomPoint() : Pair<Int, Int> {
        return Pair(Random.nextInt(), Random.nextInt())
    }
}