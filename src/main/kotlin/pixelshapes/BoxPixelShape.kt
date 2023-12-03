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

import java.io.Serializable
import kotlin.math.max
import kotlin.math.min

/**
 * Rectangular area between two points.
 */
class BoxPixelShape(
    point1: Pair<Int, Int>,
    point2: Pair<Int, Int>
) : PixelShape, Serializable {
    private val topLeftPoint : Pair<Int, Int>
    private val bottomRightPoint : Pair<Int, Int>

    /**
     * Returns the size of the shape, in unique coordinates.
     *
     * @return The number of unique coordinates in the shape.
     */
    override val size: Int
        get() = getWidth() * getHeight()

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

    /**
     * Top left edge will have (0,0) coordinates.
     * @param width Width of the box.
     * @param height Height of the box.
     */
    constructor(width: Int, height: Int) : this(
        Pair(0, 0),
        Pair(width - 1, height - 1)
    )

    /**
     * @param topLeftPoint Coordinates of the top left edge.
     * @param width Width of the box.
     * @param height Height of the box.
     */
    constructor(topLeftPoint: Pair<Int, Int>, width: Int, height: Int) : this(
        topLeftPoint, Pair(topLeftPoint.first + width - 1, topLeftPoint.second + height - 1)
    )

    /**
     * @param x X coordinate of the top left edge.
     * @param y Y coordinate of the top left edge.
     * @param width Width of the box.
     * @param height Height of the box.
     */
    constructor(x: Int, y: Int, width: Int, height: Int) : this(
        Pair(x, y), width, height
    )

    /**
     * @param xRange Range of values for the X coordinate.
     * @param yRange Range of values for the Y coordinate.
     */
    constructor(xRange: IntRange, yRange: IntRange) : this(
        Pair(xRange.first, yRange.first), Pair(xRange.last, yRange.last)
    ) {
        if (xRange.step != 1) {
            throw IllegalArgumentException("xRange should have a step of 1, found ${xRange.step}")
        }
        if (yRange.step != 1) {
            throw IllegalArgumentException("yRange should have a step of 1, found ${yRange.step}")
        }
    }

    init {
        topLeftPoint = Pair(min(point1.first, point2.first), min(point1.second, point2.second))
        bottomRightPoint = Pair(max(point1.first, point2.first), max(point1.second, point2.second))
    }

    override fun contains(element: Pair<Int, Int>): Boolean {
        return element.first in topLeftPoint.first..bottomRightPoint.first &&
                element.second in topLeftPoint.second..bottomRightPoint.second
    }

    override fun containsAll(elements: Collection<Pair<Int, Int>>): Boolean {
        for (point in elements) {
            if (!contains(point)) {
                return false
            }
        }
        return true
    }

    override fun getBox(): BoxPixelShape {
        return this
    }

    /**
     * Gets the bottom-right-most point of the Shape.
     *
     * @return The bottom-right corner of the Shape.
     */
    fun getBottomRightPoint() : Pair<Int, Int> {
        return bottomRightPoint
    }

    /**
     * Gets the height of the box.
     *
     * @return The Y difference between the top-most point and the bottom-most point.
     */
    fun getHeight() : Int {
        return bottomRightPoint.second - topLeftPoint.second + 1
    }

    /**
     * Gets the top-left-most point of the Shape.
     *
     * @return The top-left corner of the Shape.
     */
    fun getTopLeftPoint(): Pair<Int, Int> {
        return topLeftPoint
    }

    /**
     * Gets the width of the box.
     *
     * @return The X difference between the left-most point and the right-most point.
     */
    fun getWidth(): Int {
        return bottomRightPoint.first - topLeftPoint.first + 1
    }

    /**
     * Gets the range of X values.
     *
     * @return The range of X values of the Shape.
     */
    fun getXRange(): IntRange {
        return topLeftPoint.first..bottomRightPoint.first
    }

    /**
     * Gets the range of Y values.
     *
     * @return The range of Y values of the Shape.
     */
    fun getYRange(): IntRange {
        return topLeftPoint.second..bottomRightPoint.second
    }

    override fun iterator(): Iterator<Pair<Int, Int>> {
        return BoxPixelShapeIterator(this)
    }
}