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

package pixelshapes.boxcollageshape

import Point
import pixelshapes.BoxPixelShape
import pixelshapes.PixelShape

/**
 * # BoxCollageShape
 *
 * A BoxCollageShape is a PixelShape that can be decomposed into a collection of BoxPixelShapes.
 * This collection is the smallest set of BoxPixelShapes that don't overlap and add up to the original PixelShape.
 */
class BoxCollageShape private constructor(
    private val boxes: Set<BoxPixelShape>
) : PixelShape {

    /**
     * @param template PixelShape to compute from.
     */
    constructor(template: PixelShape) : this(ShapeSlicer(template).use())

    override val size: Int = boxes.sumOf { it.size }

    /**
     * Gets the set of BoxPixelShapes.
     * That set is the smallest set of BoxPixelShapes that don't overlap and add up to the original PixelShape.
     *
     * @return The set of BoxPixelShapes that make up the original Shape.
     */
    fun boxes(): Set<BoxPixelShape> = boxes

    override fun contains(element: Point): Boolean {
        for (box in boxes)
            if (box.contains(element))
                return true

        return false
    }

    override fun containsAll(elements: Collection<Point>): Boolean {
        for (element in elements)
            if (!contains(element))
                return false

        return true
    }

    override fun iterator(): Iterator<Point> {

        if (boxes.isEmpty()) return listOf<Point>().iterator()

        return object : Iterator<Point> {

            private val boxesList = boxes.toList()
            private var currentBoxIndex = 0
            private var currentBoxIterator = boxesList.first().iterator()

            override fun hasNext(): Boolean {

                val isFinished = !currentBoxIterator.hasNext() && currentBoxIndex != boxesList.lastIndex

                return !isFinished
            }

            override fun next(): Point {

                if (!currentBoxIterator.hasNext()) {

                    currentBoxIndex += 1
                    currentBoxIterator = boxesList[currentBoxIndex].iterator()
                }

                return currentBoxIterator.next()
            }
        }
    }

}