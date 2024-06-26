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

package pixelshapes

import Point
import pixelshapes.mutablepixelshapes.MutablePixelShape

/**
 * # PixelShape
 *
 * A PixelShape is a set of integer coordinates.
 *
 * Unless specified with the MutablePixelShape interface,
 * an implementation is read-only.
 *
 * @see MutablePixelShape
 */
interface PixelShape : Set<Point> {

    /**
     * Checks if the point is inside the shape.
     * @param element (X, Y) coordinates to check for.
     * @return True only if a given coordinate is a part of the shape.
     */
    override fun contains(element: Point): Boolean

    /**
     * Gets the rectangular convex hull of the shape, meaning the smallest rectangle that contains all its points.
     *
     * @return The smallest BoxPixelShape that contains all the points in the shape.
     */
    fun boundingBox(): BoxPixelShape {
        if (size == 0) {
            return BoxPixelShape(IntRange.EMPTY, IntRange.EMPTY)
        }

        val minX = this.minBy { it.first }.first
        val minY = this.minBy { it.second }.second
        val maxX = this.maxBy { it.first }.first
        val maxY = this.maxBy { it.second }.second

        return BoxPixelShape(
            point1 = Pair(minX, minY),
            point2 = Pair(maxX, maxY)
        )
    }

    /**
     * Checks if the Shape is empty.
     *
     * @return true only if the Shape has exactly 0 unique coordinates.
     */
    override fun isEmpty(): Boolean = size == 0
}