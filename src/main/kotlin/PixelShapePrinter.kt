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

import pixelshapes.PixelShape

/**
 * Tool to draw any shape in the terminal as a string.
 */
class PixelShapePrinter {
    /**
     * Writes the Shape as a string that can be displayed on screen for easy view.
     * @param shape Shape to display
     * @param xRange Range of X coordinates to display.
     * @param yRange Range of Y coordinates to display.
     * @return A string showcasing the Shape.
     */
    fun string(shape: PixelShape, xRange: IntRange, yRange: IntRange): String {
        var res = ""

        for (y in yRange) {
            for (x in xRange) {
                val point = Pair(x, y)
                res += if (shape.contains(point)) {
                    "[]"
                } else {
                    "_ "
                }
            }
            res += "\n"
        }

        return res
    }
}