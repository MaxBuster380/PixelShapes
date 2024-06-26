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
import pixelshapes.PixelShape

/**
 * # MutablePixelShape
 *
 * A MutablePixelShape is a PixelShape that can be modified at run-time.
 */
interface MutablePixelShape : PixelShape, MutableSet<Point> {
    /**
     * Adds a point to the Shape.
     * @param element (X, Y) coordinate to include in the Shape.
     * @return false only if the point was already included in the Shape.
     */
    override fun add(element: Point): Boolean

    /**
     * Adds all the points in a shape.
     * @param shape Shape whose points you want to add to the current shape.
     */
    fun add(shape: PixelShape)

    /**
     * Removes a point from the Shape.
     * @param element (X, Y) coordinate to exclude from the Shape.
     * @return false only if the point was already excluded from the Shape.
     */
    override fun remove(element: Point): Boolean

    /**
     * Removes all the points in a shape from the current shape.
     * @param shape Shape whose points you want to remove to the current shape.
     */
    fun remove(shape: PixelShape)
}