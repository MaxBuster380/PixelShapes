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

/**
 * Collections of PixelShapes are Shapes used for quick and efficient grouping of existing Shapes.
 * While not editable per coordinate, like MutablePixelShapes, they can add and remove instances of shapes post-creation.
 * @param T The collection's type can be specified. Use "PixelShape" for any.
 */
interface CollectionPixelShape<T : PixelShape> : PixelShape {

    /**
     * Ads a shape to the current list of member Shapes.
     * Does nothing if the Shape instance is already included.
     * @param shape Shape to add.
     */
    fun add(shape: T)

    /**
     * Returns all the current member Shapes.
     * @return A set of all Shape instances added to the collection.
     */
    fun getAllShapes(): Set<T>

    /**
     * Removes a shape from the current list of member Shapes.
     * Does nothing if the Shape instance is already excluded.
     * @param shape Shape to remove.
     */
    fun remove(shape: T)
}