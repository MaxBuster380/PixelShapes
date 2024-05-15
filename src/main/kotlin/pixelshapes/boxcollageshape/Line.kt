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

import pixelshapes.BoxPixelShape
import pixelshapes.boxcollageshape.CornerDirection.NORTHWEST
import pixelshapes.boxcollageshape.CornerDirection.SOUTHEAST

internal data class Line(
    val mainCorner: Corner,
    val secondCorner: Corner
) : Iterable<Pair<Int, Int>> {

    init {

        if (mainCorner.direction == NORTHWEST)
            throw Exception("Main Corner cannot be NORTHWEST.")

        if (secondCorner.direction == SOUTHEAST)
            throw Exception("Secondary Corner cannot be SOUTHEAST.")

        if (mainCorner.direction == secondCorner.direction)
            throw Exception("Corners cannot have the same direction.")

        if (mainCorner.coordinates == secondCorner.coordinates)
            throw Exception("Corners cannot overlap.")
    }

    fun isHorizontal(): Boolean = mainCorner.coordinates.second == secondCorner.coordinates.second

    fun isVertical(): Boolean = !isHorizontal()

    fun horizontalRange(): IntRange {
        return IntRange(mainCorner.coordinates.first, secondCorner.coordinates.first)
    }

    fun verticalRange(): IntRange {
        return IntRange(mainCorner.coordinates.second, secondCorner.coordinates.second)
    }

    fun crosses(other: Line): Boolean {

        if (this.isHorizontal() == other.isHorizontal()) return false

        val horizontal = if (this.isHorizontal()) this else other
        val vertical = if (this == horizontal) other else this

        val verticalX = vertical.mainCorner.coordinates.first
        val horizontalY = horizontal.mainCorner.coordinates.second

        return verticalX in horizontal.horizontalRange() && horizontalY in vertical.verticalRange()
    }

    override fun iterator(): Iterator<Pair<Int, Int>> {
        val box = BoxPixelShape(mainCorner.coordinates, secondCorner.coordinates)
        return box.iterator()
    }
}