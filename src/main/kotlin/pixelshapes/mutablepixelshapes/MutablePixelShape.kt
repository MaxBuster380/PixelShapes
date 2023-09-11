package pixelshapes.mutablepixelshapes

import pixelshapes.PixelShape

/**
 * Set of integer coordinates. Can be modified.
 */
interface MutablePixelShape : PixelShape {
    /**
     * Adds a point to the shape.
     * @param point (X, Y) coordinate to include in the shape.
     */
    fun add(point : Pair<Int, Int>)

    /**
     * Removes a point from the shape.
     * @param point (X, Y) coordinate to exclude from the shape.
     */
    fun remove(point : Pair<Int, Int>)
}