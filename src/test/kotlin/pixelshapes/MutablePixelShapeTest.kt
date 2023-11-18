/*
 * MIT License
 *
 * Copyright (c) 2023 MaxBuster
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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