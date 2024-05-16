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

package shapes

import Point
import pixelshapes.BoxPixelShape
import pixelshapes.PixelShape
import pixelshapes.mutablepixelshapes.SetMutablePixelShape

fun bee(): PixelShape {

    val res = SetMutablePixelShape()

    res += Point(1, 0)

    res += BoxPixelShape(1, 1, 8, 1)

    res += BoxPixelShape(0, 2, 4, 2)
    res += BoxPixelShape(6, 2, 4, 2)

    res += BoxPixelShape(1, 4, 2, 1)
    res += BoxPixelShape(4, 4, 2, 1)
    res += BoxPixelShape(7, 4, 2, 1)

    res += BoxPixelShape(3, 5, 4, 2)

    res += BoxPixelShape(4, 7, 2, 1)

    res += Point(5, 8)

    return res
}

fun file(): PixelShape {
    val res = SetMutablePixelShape()
    res += BoxPixelShape(2, 4)
    res += BoxPixelShape(3, 3)
    return res
}