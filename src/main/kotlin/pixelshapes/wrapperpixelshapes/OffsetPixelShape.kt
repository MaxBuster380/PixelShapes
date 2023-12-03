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

package pixelshapes.wrapperpixelshapes

import pixelshapes.PixelShape
import java.io.Serializable

/**
 * Adds a translation vector to an existing Shape.
 */
class OffsetPixelShape(
    private val subShape: PixelShape,
    private val offset: Pair<Int, Int>
) : PixelShape, Serializable {

    companion object {
        private fun add(a: Pair<Int, Int>, b: Pair<Int, Int>): Pair<Int, Int> {
            return Pair(a.first + b.first, a.second + b.second)
        }
    }

    private class OffsetPixelShapeIterator(private val offsetPixelShape: OffsetPixelShape) : Iterator<Pair<Int, Int>> {

        private val iterator = offsetPixelShape.getSubShape().iterator()

        override fun hasNext(): Boolean {
            return iterator.hasNext()
        }

        override fun next(): Pair<Int, Int> {
            return add(iterator.next(), offsetPixelShape.getOffset())
        }

    }

    /**
     * Returns the size of the shape, in unique coordinates.
     *
     * @return The number of unique coordinates in the shape.
     */
    override val size: Int get() = subShape.size

    override fun contains(element: Pair<Int, Int>): Boolean {
        return subShape.contains(add(element, offset))
    }

    override fun containsAll(elements: Collection<Pair<Int, Int>>): Boolean {
        return subShape.containsAll(elements.map { add(it, offset) })
    }

    /**
     * Gets the sub shape of this instance.
     *
     * @return This instance's reference Shape.
     */
    fun getSubShape(): PixelShape {
        return subShape
    }

    /**
     * Gets this shape's offset vector.
     *
     * @return The vector by which the sub Shape is moved.
     */
    fun getOffset(): Pair<Int, Int> {
        return offset
    }

    override fun iterator(): Iterator<Pair<Int, Int>> {
        return OffsetPixelShapeIterator(this)
    }
}