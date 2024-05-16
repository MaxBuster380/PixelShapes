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

package pixelshapes.wrapperpixelshapes

import Point
import pixelshapes.PixelShape
import java.io.Serializable

/**
 * Adds a translation vector to an existing Shape.
 */
class OffsetPixelShape(

    /**
     * Gets the sub shape of this instance.
     *
     * @return This instance's reference Shape.
     */
    val subShape: PixelShape,

    /**
     * Gets this shape's offset vector.
     *
     * @return The vector by which the sub Shape is moved.
     */
    val offset: Point
) : PixelShape, Serializable {

    //////////////////////////////////////// STATIC COMPONENTS /////////////////////////////////////////

    companion object {
        private fun add(a: Point, b: Point): Point {
            return Pair(a.first + b.first, a.second + b.second)
        }
    }

    /////////////////////////////////////// ACCESSOR ATTRIBUTES ////////////////////////////////////////

    /**
     * The size is the number of unique coordinates in the Shape.
     */
    override val size: Int get() = subShape.size

    ///////////////////////////////////// ITERATOR IMPLEMENTATION //////////////////////////////////////

    private class OffsetPixelShapeIterator(private val offsetPixelShape: OffsetPixelShape) : Iterator<Point> {

        private val iterator = offsetPixelShape.subShape.iterator()

        override fun hasNext(): Boolean {
            return iterator.hasNext()
        }

        override fun next(): Point {
            return add(iterator.next(), offsetPixelShape.offset)
        }

    }

    ///////////////////////////////////////// INSTANCE METHODS /////////////////////////////////////////

    override fun contains(element: Point): Boolean {
        return subShape.contains(add(element, offset))
    }

    override fun containsAll(elements: Collection<Point>): Boolean {
        return subShape.containsAll(elements.map { add(it, offset) })
    }

    override fun iterator(): Iterator<Point> {
        return OffsetPixelShapeIterator(this)
    }
}