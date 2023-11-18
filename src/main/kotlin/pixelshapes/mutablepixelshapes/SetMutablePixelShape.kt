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

/**
 * Implementation of MutablePixelShape using a set.
 */
class SetMutablePixelShape(
    private var points: MutableSet<Pair<Int, Int>>
) : MutablePixelShape {

    /**
     * Creates an empty shape.
     */
    constructor() : this(mutableSetOf())

    /**
     * Creates a copy of the given shape.
     */
    constructor(other: PixelShape) : this() {
        add(other)
    }

    override fun add(point: Pair<Int, Int>) {
        points.add(point)
    }

    override fun add(shape: PixelShape) {
        for (point in shape) {
            points.add(point)
        }
    }

    override fun remove(point: Pair<Int, Int>) {
        points.remove(point)
    }

    override fun remove(shape: PixelShape) {
        for (point in shape) {
            points.remove(point)
        }
    }

    override fun contains(point: Pair<Int, Int>): Boolean {
        return point in points
    }

    override fun iterator(): Iterator<Pair<Int, Int>> {
        return points.iterator()
    }

    override fun getSize(): Int {
        return points.size
    }
}