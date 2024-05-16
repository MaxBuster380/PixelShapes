/*
 * MIT License
 *
 * Copyright (c) 2024 MaxBuster
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

package pixelshapes.mutablepixelshapes

import Point
import pixelshapes.BoxPixelShape
import pixelshapes.MutablePixelShapeTest
import pixelshapes.PixelShape
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
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

    @Test
    fun eachPointIteratedIsUnique() {

        val shape = SetMutablePixelShape()
        for (i in 1..1000) {
            val newPoint = Pair(Random.nextInt(), Random.nextInt())
            shape.add(newPoint)
            if (Random.nextInt() % 2 == 0) {
                shape.add(newPoint)
            }
        }

        val iteratedPoints = mutableSetOf<Point>()
        for (point in shape) {
            assertFalse(iteratedPoints.contains(point))
            iteratedPoints += point
        }

        assertEquals(iteratedPoints.size, shape.size)
    }

    @Test
    fun checkCopyConstructor() {
        val otherShape: PixelShape = BoxPixelShape(31, 97)

        val setShape = SetMutablePixelShape(otherShape)

        assertEquals(otherShape.size, setShape.size)

        for (boxPoint in otherShape) {
            assertTrue { setShape.contains(boxPoint) }
        }
    }

    @Test
    fun emptyConstructor() {
        val shape = SetMutablePixelShape()

        assertTrue { shape.size == 0 }
    }
}