package pixelshapes

import pixelshapes.mutablepixelshapes.MutablePixelShape
import kotlin.math.abs
import kotlin.random.Random

/**
 * Tests for all of MutablePixelShape's subclasses.
 */
class MutablePixelShapeTest {

    companion object {
        /**
         * When using arbitrary values, this is how many times you re-do the check.
         * @return True if the test passed
         */
        const val RANDOM_CHECKS_COUNT = 100
    }

    /**
     * Check if, when only using .add, all the points give True when running .contains
     * @return True if the test passed
     */
    fun containsKeepsAddedPoints(shape : MutablePixelShape, xRange : IntRange, yRange : IntRange) : Boolean {
        var res = true

        // Add a bunch of points to the shape
        val points = mutableListOf<Pair<Int, Int>>()
        for(i in 1..RANDOM_CHECKS_COUNT) {
            val new = randomPoint(xRange, yRange)
            shape.add(new)
            points += new
        }

        // Check if they are all included in the shape
        for(point in points) {
            res = res && shape.contains(point)
        }

        return res
    }

    /**
     * Checks if a removed point gives False when running .contains
     * @return True if the test passed
     */
    fun containsExcludesRemovedPoints(shape : MutablePixelShape, xRange : IntRange, yRange : IntRange) : Boolean {
        var res = true

        val NB_POINTS_REMOVED = RANDOM_CHECKS_COUNT / 2

        // Add a bunch of points to the set
        // "Status" is an external check for if the point remains in the shape.
        val points = mutableListOf<Pair<Int, Int>>()
        val pointStatus = mutableListOf<Boolean>()
        for(i in 1..RANDOM_CHECKS_COUNT) {
            val new = randomPoint(xRange, yRange)

            if (!points.contains(new)) {
                shape.add(new)
                points += new
                pointStatus += true
            }
        }

        // Remove at most NB_POINTS_REMOVED points
        for(i in 1..NB_POINTS_REMOVED) {
            val indexToExclude = abs(Random.nextInt()) % points.size

            shape.remove(points[indexToExclude])
            pointStatus[indexToExclude] = false
        }

        // Check that all the points' status match
        for(i in points.indices) {
            res = res && shape.contains(points[i]) == pointStatus[i]
        }

        return res
    }

    /**
     * Checks if the iterator gives all and only the points included in the shape.
     */
    fun iteratorCheck(shape : MutablePixelShape, xRange : IntRange, yRange : IntRange) : Boolean {
        var res = true

        val NB_POINTS_REMOVED = RANDOM_CHECKS_COUNT / 2

        // Add a bunch of points to the set
        val points = mutableListOf<Pair<Int, Int>>()
        for(i in 1..RANDOM_CHECKS_COUNT) {
            val new = randomPoint(xRange, yRange)

            if (!points.contains(new)) {
                shape.add(new)
                points += new
            }
        }
        println(points.size)
        // Remove at most NB_POINTS_REMOVED points
        for(i in 1..NB_POINTS_REMOVED) {
            val indexToExclude = abs(Random.nextInt()) % points.size

            shape.remove(points[indexToExclude])
            points.removeAt(indexToExclude)
        }

        /* For all points iterated on, check if they are :
             1. Actually inside the shape
             2. If the list of remaining points contains it
           ... and removes the point from the list of remaining points.
           At the end, check if that list is empty.
        */
        for(point in shape) {
            res = res && shape.contains(point)
            res = res && points.contains(point)

            points.remove(point)
        }
        res = res && points.isEmpty()

        return res
    }

    /**
     * Returns a new instance of a point with arbitrary coordinates within a given range.
     */
    private fun randomPoint(xRange : IntRange, yRange : IntRange) : Pair<Int, Int> {
        return Pair(
            randomValueInRange(xRange),
            randomValueInRange(yRange)
        )
    }

    private fun randomValueInRange(range : IntRange) : Int {
        val random = abs(Random.nextInt())
        val size = range.last - range.first
        return (random % size) - range.first
    }
}