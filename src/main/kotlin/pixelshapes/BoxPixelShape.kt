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

import kotlin.math.max
import kotlin.math.min

/**
 * Rectangular area between two points.
 */
class BoxPixelShape(point1 : Pair<Int, Int>, point2 : Pair<Int, Int>) : PixelShape {
    private val topLeftPoint : Pair<Int, Int>
    private val bottomRightPoint : Pair<Int, Int>

    private class BoxPixelShapeIterator(private val shape : BoxPixelShape) : Iterator<Pair<Int, Int>> {
        private var currentX = shape.topLeftPoint.first
        private var currentY = shape.topLeftPoint.second

        override fun hasNext(): Boolean {
            return currentY <= shape.bottomRightPoint.second
        }

        override fun next(): Pair<Int, Int> {
            val res = Pair(currentX, currentY)

            currentX += 1
            if (currentX > shape.bottomRightPoint.first) {
                currentX = shape.topLeftPoint.first
                currentY += 1
            }

            return res
        }
    }

    constructor(x: Int, y: Int) : this(
        Pair(0, 0),
        Pair(x - 1, y - 1)
    )

    constructor(origin: Pair<Int, Int>, width: Int, height: Int) : this(
        origin, Pair(origin.first + width - 1, origin.second + height - 1)
    )

    init {
        topLeftPoint = Pair(min(point1.first, point2.first), min(point1.second, point2.second))
        bottomRightPoint = Pair(max(point1.first, point2.first), max(point1.second, point2.second))
    }

    override fun contains(point: Pair<Int, Int>): Boolean {
        return point.first in topLeftPoint.first..bottomRightPoint.first &&
                point.second in topLeftPoint.second..bottomRightPoint.second
    }

    override fun iterator(): Iterator<Pair<Int, Int>> {
        return BoxPixelShapeIterator(this)
    }

    override fun getBox(): BoxPixelShape {
        return this
    }

    override fun getSize(): Int {
        return getWidth() * getHeight()
    }

    /**
     * Gets the top-left-most point of the box shape.
     *
     * @return The bottom-right corner of the box shape.
     */
    fun getTopLeftPoint() : Pair<Int, Int> {
        return topLeftPoint
    }

    /**
     * Gets the bottom-right-most point of the box shape.
     *
     * @return The bottom-right corner of the box shape.
     */
    fun getBottomRightPoint() : Pair<Int, Int> {
        return bottomRightPoint
    }

    /**
     * Gets the width of the box.
     *
     * @return The X difference between the left-most point and the right-most point.
     */
    fun getWidth() : Int {
        return bottomRightPoint.first - topLeftPoint.first + 1
    }

    /**
     * Gets the height of the box.
     *
     * @return The Y difference between the top-most point and the bottom-most point.
     */
    fun getHeight() : Int {
        return bottomRightPoint.second - topLeftPoint.second + 1
    }
}