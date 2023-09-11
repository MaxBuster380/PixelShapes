package pixelshapes.mutablepixelshapes

import kotlin.math.abs
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
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

        // Add a bunch of points to the shape
        val points = mutableListOf<Pair<Int, Int>>()
        for(i in 1..RANDOM_CHECKS_COUNT) {
            val new = randomPoint()
            shape.add(new)
            points += new
        }

        // Check if they are all included in the shape
        for(point in points) {
            assertTrue(shape.contains(point))
        }
    }

    /**
     * Checks if a removed point gives Fale when running .contains
     */
    @Test
    fun containsExcludesRemovedPoints() {
        val NB_POINTS_REMOVED = RANDOM_CHECKS_COUNT / 2

        val shape = SetShape()

        // Add a bunch of points to the set
        // "Status" is an external check for if the point remains in the shape.
        val points = mutableListOf<Pair<Int, Int>>()
        val pointStatus = mutableListOf<Boolean>()
        for(i in 1..RANDOM_CHECKS_COUNT) {
            val new = randomPoint()
            shape.add(new)
            points += new
            pointStatus += true
        }

        // Remove at most NB_POINTS_REMOVED points
        for(i in 1..NB_POINTS_REMOVED) {
            val indexToExclude = abs(Random.nextInt()) % points.size

            shape.remove(points[indexToExclude])
            pointStatus[indexToExclude] = false
        }

        // Check that all the points' status match
        for(i in points.indices) {
            assertEquals(shape.contains(points[i]), pointStatus[i])
        }
    }

    /**
     * Returns a new instance of a point with arbitrary coordinates.
     */
    private fun randomPoint() : Pair<Int, Int> {
        return Pair(Random.nextInt(), Random.nextInt())
    }
}