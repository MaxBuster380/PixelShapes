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

package pixelshapes.mutablepixelshapes

import pixelshapes.PixelShape
import kotlin.math.floor

/**
 * @Deprecated
 * Implementation of MutablePixelShape using a grid.
 */
class GridMutablePixelShape(
    private val origin : Pair<Int, Int>,
    private val width : Int,
    private val height : Int
) : MutablePixelShape {

    private class GridShapeIterator(private val gridMutablePixelShape : GridMutablePixelShape) : Iterator<Pair<Int, Int>> {
        private var nextIndex = 0

        init {
            findNextIndex()
        }

        override fun hasNext(): Boolean {
            return !gridMutablePixelShape.indexOverLimit(nextIndex)
        }

        override fun next(): Pair<Int, Int> {
            val resIndex = nextIndex
            findNextIndex()
            return gridMutablePixelShape.pointOf(resIndex)
        }

        private fun findNextIndex() {
            do {
                nextIndex += 1
            }while(!gridMutablePixelShape.indexOverLimit(nextIndex) && !gridMutablePixelShape.table[nextIndex])
        }
    }

    private val table : MutableList<Boolean>

    private var size = 0

    constructor(width : Int, height : Int) : this(Pair(0, 0), width, height)

    init {
        if (width <= 0) { throw IllegalArgumentException("width must be strictly positive, found $width") }
        if (height <= 0) { throw IllegalArgumentException("height must be strictly positive, found $height") }

        table = MutableList(width * height) { false }
    }

    override fun add(point: Pair<Int, Int>) {
        if (!isPointInGrid(point)) { throw IndexOutOfBoundsException("$point is outside the grid's boundary.") }

        if (!table[indexOf(point)]) {
            table[indexOf(point)] = true
            size += 1
        }
    }

    override fun add(shape: PixelShape) {
        for (point in shape) {
            add(point)
        }
    }

    override fun remove(point: Pair<Int, Int>) {
        if (!isPointInGrid(point)) { throw IndexOutOfBoundsException("$point is outside the grid's boundary.") }

        if (table[indexOf(point)]) {
            table[indexOf(point)] = false
            size -= 1
        }
    }

    override fun remove(shape: PixelShape) {
        for (point in shape) {
            remove(point)
        }
    }

    override fun contains(point: Pair<Int, Int>): Boolean {
        if (!isPointInGrid(point)) { throw IndexOutOfBoundsException("$point is outside the grid's boundary.") }

        return table[indexOf(point)]
    }

    override fun iterator(): Iterator<Pair<Int, Int>> {
        return GridShapeIterator(this)
    }

    override fun getSize(): Int {
        return size
    }

    // PRIVATE INSTANCE METHODS

    /**
     * Gives the same point relative to the origin point if the latter was (0, 0)
     */
    private fun relativeToOrigin(point : Pair<Int, Int>) : Pair<Int, Int> {
        return Pair(
            point.first - origin.first,
            point.second - origin.second
        )
    }

    /**
     * Checks if the given point is inside the defined grid.
     */
    private fun isPointInGrid(point : Pair<Int, Int>) : Boolean {
        val relative = relativeToOrigin(point)
        return relative.first in 0..<width && relative.second in 0..<height
    }

    /**
     * Returns the index of a given point in the internal list.
     * Assumes the point is in the grid
     * @see pointOf
     */
    private fun indexOf(point : Pair<Int, Int>) : Int {
        val relative = relativeToOrigin(point)
        return relative.second * width + relative.first
    }

    /**
     * Returns the point associated with the given index in the list.
     * Assumes the point is in the grid.
     * @see indexOf
     */
    private fun pointOf(index : Int) : Pair<Int, Int> {
        return Pair(
            index % width + origin.first,
            floor(index.toDouble() / width).toInt() + origin.second
        )
    }

    /**
     * Returns True only if the given index is greater than the maximum index.
     */
    private fun indexOverLimit(index : Int) : Boolean {
        return index >= width * height
    }
}