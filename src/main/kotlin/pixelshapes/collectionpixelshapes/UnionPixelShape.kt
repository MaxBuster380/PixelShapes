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

package pixelshapes.collectionpixelshapes

import pixelshapes.PixelShape
import java.io.Serializable

/**
 * Collection of Shapes where any of any current member Shape is considered "inside the Shape".
 * @param T The collection's type can be specified. Use "PixelShape" for any.
 */
open class UnionPixelShape<T : PixelShape>(
    inputList: List<T>
) : CollectionPixelShape<T>, Serializable {
    private val list: MutableList<T>

    constructor() : this(listOf())

    init {
        list = inputList.toMutableList()
        list.sortByDescending { it.getSize() }
    }

    override fun add(shape: T) {
        list += shape
        list.sortByDescending { it.getSize() }
    }

    override fun contains(point: Pair<Int, Int>): Boolean {
        for (shape in list) {
            if (shape.contains(point)) {
                return true
            }
        }
        return false
    }

    override fun getAllShapes(): Set<T> {
        return list.toSet()
    }

    override fun getSize(): Int {
        val points = compileToSet()

        return points.size
    }

    override fun iterator(): Iterator<Pair<Int, Int>> {
        val points = compileToSet()

        return points.iterator()
    }

    override fun remove(shape: T) {
        list -= shape
    }

    private fun compileToSet(): Set<Pair<Int, Int>> {
        val res = mutableSetOf<Pair<Int, Int>>()
        for (shape in list) {
            for (point in shape) {
                res += point
            }
        }
        return res
    }

}