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

package pixelshapes.collectionpixelshapes

import pixelshapes.BoxPixelShape
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class UnionPixelShapeTest {
    @Test
    fun containsTest() {
        val shape = UnionPixelShape(
            mutableListOf(
                BoxPixelShape(Pair(0, 0), 4, 5), BoxPixelShape(Pair(6, 2), 3, 2)
            )
        )

        assertTrue { shape.contains(Pair(0, 0)) }
        assertTrue { shape.contains(Pair(6, 2)) }
        assertFalse { shape.contains(Pair(4, 5)) }
        assertTrue { shape.contains(Pair(8, 3)) }
        assertTrue { shape.contains(Pair(2, 3)) }
        assertTrue { shape.contains(Pair(7, 3)) }
    }

    @Test
    fun iteratorCheck() {
        val box1 = BoxPixelShape(Pair(0, 0), 4, 5)
        val box2 = BoxPixelShape(Pair(5, 1), 3, 2)
        val shape = UnionPixelShape(
            mutableListOf(
                box1, box2
            )
        )

        val points = mutableSetOf<Pair<Int, Int>>()

        for (point in shape) {
            assertTrue { box1.contains(point) || box2.contains(point) }
            assertTrue { !points.contains(point) }
            points += point
        }

        assertEquals(points.size, shape.size)
    }
}