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
import java.io.Serializable

/**
 * # SetMutablePixelShape
 *
 * A SetMutablePixelShape is a PixelShape that can be modified at run-time.
 *
 * @constructor Uses the given set as reference. Does not copy with a different instance.

 * @param points If given a MutableSet, it will use this instance to operate.
 */
class SetMutablePixelShape(
    private val points: MutableSet<Point>
) : MutablePixelShape, Serializable {

    /////////////////////////////////////// ACCESSOR ATTRIBUTES ////////////////////////////////////////

    /**
     * The size is the number of unique coordinates in the Shape.
     */
    override val size: Int get() = points.size

    /////////////////////////////////////////// CONSTRUCTORS ///////////////////////////////////////////

    /**
     * Creates an empty shape.
     */
    constructor() : this(mutableSetOf())

    /**
     * Creates a copy of the `other` given shape.
     *
     * @param other
     */
    constructor(other: PixelShape) : this() {
        add(other)
    }

    /**
     * Creates a copy of the collection as a set and uses it as reference.
     *
     * @param collection
     */
    constructor(collection: Collection<Point>) : this(collection.toMutableSet())

    ///////////////////////////////////////// INSTANCE METHODS /////////////////////////////////////////

    override fun add(element: Point): Boolean {
        return points.add(element)
    }

    override fun add(shape: PixelShape) {
        for (point in shape) {
            points.add(point)
        }
    }

    override fun addAll(elements: Collection<Point>): Boolean {
        return points.addAll(elements)
    }

    override fun clear() {
        points.clear()
    }

    override fun contains(element: Point): Boolean {
        return element in points
    }

    override fun containsAll(elements: Collection<Point>): Boolean {
        return points.containsAll(elements)
    }

    override fun iterator(): MutableIterator<Point> {
        return points.iterator()
    }

    override fun remove(element: Point): Boolean {
        return points.remove(element)
    }

    override fun remove(shape: PixelShape) {
        for (point in shape) {
            points.remove(point)
        }
    }

    override fun removeAll(elements: Collection<Point>): Boolean {
        return points.removeAll(elements.toSet())
    }

    override fun retainAll(elements: Collection<Point>): Boolean {
        return points.retainAll(elements.toSet())
    }
}