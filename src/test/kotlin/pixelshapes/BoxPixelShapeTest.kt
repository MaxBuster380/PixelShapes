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

import kotlin.math.abs
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class BoxPixelShapeTest {
    @Test
    fun iteratorIteratesAllPoints() {
        val mask = 1023

        val x1 = Random.nextInt() and mask
        val x2 = Random.nextInt() and mask
        val y1 = Random.nextInt() and mask
        val y2 = Random.nextInt() and mask

        val topLeftPoint = Pair(
            if (x1 < x2) { x1 } else { x2 },
            if (y1 < y2) { y1 } else { y2 }
        )

        val bottomRightPoint = Pair(
            if (x1 >= x2) { x1 } else { x2 },
            if (y1 >= y2) { y1 } else { y2 }
        )

        val manualPointsInShape = mutableSetOf<Pair<Int, Int>>()
        for(x in topLeftPoint.first..bottomRightPoint.first) {
            for(y in topLeftPoint.second..bottomRightPoint.second) {
                manualPointsInShape += Pair(x, y)
            }
        }

        val shape = BoxPixelShape(topLeftPoint, bottomRightPoint)

        for(point in shape) {
            assertTrue(manualPointsInShape.contains(point))
            manualPointsInShape.remove(point)
        }

        assertTrue(manualPointsInShape.isEmpty())
    }

    @Test
    fun areaMatchesNbPointsIteratedMatchesSize() {
        val shape = BoxPixelShape(41, 63)

        val area = shape.getWidth() * shape.getHeight()

        var nbPoints = 0
        for (point in shape) {
            nbPoints++
        }

        assertEquals(area, nbPoints)
        assertEquals(area, shape.getSize_())
        assertEquals(nbPoints, shape.getSize_())
    }

    @Test
    fun originConstructor() {
        val x = Random.nextInt() % 1_000
        val y = Random.nextInt() % 1_000
        val origin = Pair(x, y)
        val width = abs(Random.nextInt() % 2_000)
        val height = abs(Random.nextInt() % 2_000)

        val shape = BoxPixelShape(origin, width, height)

        assertEquals(width * height, shape.getSize_())
        assertEquals(width, shape.getWidth())
        assertEquals(height, shape.getHeight())
        assertEquals(origin, shape.getTopLeftPoint())
    }

    @Test
    fun sizeMemberMatchesGetSizeMethod() {
        val shape = BoxPixelShape(14, 5)

        assertEquals(shape.getSize_(), shape.size)
    }
}