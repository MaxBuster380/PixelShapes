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

class BoxCollageShape private constructor(
    private val boxes: Set<BoxPixelShape>
) : PixelShape {

    constructor(template: PixelShape) : this(ShapeSlicer(template).use())

    override val size: Int = boxes.sumOf { it.size }

    fun boxes(): Set<BoxPixelShape> = boxes

    override fun boundingBox(): BoxPixelShape {
        TODO("Not yet implemented")
    }

    override fun contains(element: Point): Boolean {
        TODO("Not yet implemented")
    }

    override fun containsAll(elements: Collection<Point>): Boolean {
        TODO("Not yet implemented")
    }

    override fun iterator(): Iterator<Point> {
        TODO("Not yet implemented")
    }

}