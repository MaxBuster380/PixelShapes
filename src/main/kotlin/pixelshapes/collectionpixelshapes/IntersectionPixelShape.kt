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

package pixelshapes.collectionpixelshapes

import Point
import pixelshapes.PixelShape
import java.io.Serializable

/**
 * # IntersectionPixelShape
 *
 * An IntersectionPixelShape is a collection of Shapes where only the shared coordinates of all current member Shapes are considered "inside the Shape".
 * @param T The collection's type can be specified. Use "PixelShape" for any.
 */
class IntersectionPixelShape<T : PixelShape>(
    collection: Collection<T>
) : CollectionPixelShape<T>, Serializable {
    private val list: MutableList<T>

    /**
     * The size is the number of unique coordinates in the Shape.
     */
    override val size: Int
        get() = calculateSize()

    /**
     * Creates an instance with no shapes attached.
     */
    constructor() : this(listOf())

    init {
        list = collection.toMutableList()
        list.sortByDescending { it.size }
    }

    override fun add(shape: T) {
        list += shape
        list.sortByDescending { it.size }
    }

    override fun contains(element: Point): Boolean {
        for (shape in list) {
            if (!shape.contains(element)) {
                return false
            }
        }
        return true
    }

    override fun containsAll(elements: Collection<Point>): Boolean {
        val points = compileSet()

        return points.containsAll(elements)
    }

    override fun getAllShapes(): Set<T> {
        return list.toSet()
    }

    override fun iterator(): Iterator<Point> {
        if (list.isEmpty()) {
            return listOf<Point>().iterator()
        }

        val points = compileSet()

        return points.iterator()
    }

    override fun remove(shape: T) {
        list -= shape
    }

    private fun compileSet(): Set<Point> {
        val res = mutableSetOf<Point>()
        val smallestShape = list.last()
        for (point in smallestShape) {
            if (contains(point)) {
                res += point
            }
        }
        return res
    }

    private fun calculateSize(): Int {
        if (list.isEmpty()) {
            return 0
        }

        val points = compileSet()

        return points.size
    }

}