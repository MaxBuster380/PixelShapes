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
import java.io.Serializable
import kotlin.math.max
import kotlin.math.min

/**
 * # BoxPixelShape
 *
 * A BoxPixelShape is a set of coordinates forming a rectangle.
 */
class BoxPixelShape(
    point1: Point,
    point2: Point
) : PixelShape, Serializable {

    /////////////////////////////////////// INSTANCE ATTRIBUTES ////////////////////////////////////////

    /**
     * The top left point is the point of the Shape with the lowest X and lowest Y.
     */
    val topLeftPoint: Point

    /**
     * The bottom right point is the point of the Shape with the highest X and highest Y.
     */
    val bottomRightPoint: Point

    /////////////////////////////////////////// CONSTRUCTORS ///////////////////////////////////////////

    /**
     * The top left point will have the coordinates (0, 0).
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
    constructor(topLeftPoint: Point, width: Int, height: Int) : this(
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

    /////////////////////////////////////// ACCESSOR ATTRIBUTES ////////////////////////////////////////

    /**
     * The top right point is the point of the Shape with the highest X and lowest Y.
     */
    val topRightPoint: Point get() = Pair(bottomRightPoint.first, topLeftPoint.second)

    /**
     * The bottom left point is the point of the Shape with the lowest X and highest Y.
     */
    val bottomLeftPoint: Point get() = Pair(topLeftPoint.first, bottomRightPoint.second)

    /**
     * The width is the length of the horizontal side of the Shape.
     */
    val width: Int get() = bottomRightPoint.first - topLeftPoint.first + 1

    /**
     * The height is the length of the vertical side of the Shape.
     */
    val height: Int get() = bottomRightPoint.second - topLeftPoint.second + 1

    /**
     * xLeft is the lowest X value of any point in the Shape.
     */
    val xLeft: Int get() = topLeftPoint.first

    /**
     * xRight is the highest X value of any point in the Shape.
     */
    val xRight: Int get() = bottomRightPoint.first

    /**
     * yTop is the lowest Y value of any point in the Shape.
     */
    val yTop: Int get() = topLeftPoint.second

    /**
     * yBottom is the highest Y value of any point in the Shape.
     */
    val yBottom: Int get() = bottomRightPoint.second

    /**
     * xRange is the range of X values of the Shape.
     */
    val xRange: IntRange get() = topLeftPoint.first..bottomRightPoint.first


    /**
     * yRange is the range of Y values of the Shape.
     */
    val yRange: IntRange get() = topLeftPoint.second..bottomRightPoint.second

    /**
     * The size is the number of unique coordinates in the Shape.
     */
    override val size: Int get() = width * height

    ///////////////////////////////////// ITERATOR IMPLEMENTATION //////////////////////////////////////

    private class BoxPixelShapeIterator(private val shape: BoxPixelShape) : Iterator<Point> {
        private var currentX = shape.topLeftPoint.first
        private var currentY = shape.topLeftPoint.second

        override fun hasNext(): Boolean {
            return currentY <= shape.bottomRightPoint.second
        }

        override fun next(): Point {
            val res = Pair(currentX, currentY)

            currentX += 1
            if (currentX > shape.bottomRightPoint.first) {
                currentX = shape.topLeftPoint.first
                currentY += 1
            }

            return res
        }
    }

    ///////////////////////////////////////// INSTANCE METHODS /////////////////////////////////////////

    override fun contains(element: Point): Boolean {
        return element.first in topLeftPoint.first..bottomRightPoint.first &&
                element.second in topLeftPoint.second..bottomRightPoint.second
    }

    override fun containsAll(elements: Collection<Point>): Boolean {
        for (point in elements) {
            if (!contains(point)) {
                return false
            }
        }
        return true
    }

    override fun boundingBox(): BoxPixelShape = this

    override fun iterator(): Iterator<Point> {
        return BoxPixelShapeIterator(this)
    }
}