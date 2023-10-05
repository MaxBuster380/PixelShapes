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
     * Adds all the points in a shape.
     * @param shape Shape whose points you want to add to the current shape.
     */
    fun add(shape: PixelShape)

    /**
     * Removes a point from the shape.
     * @param point (X, Y) coordinate to exclude from the shape.
     */
    fun remove(point : Pair<Int, Int>)

    /**
     * Removes all the points in a shape from the current shape.
     * @param shape Shape whose points you want to remove to the current shape.
     */
    fun remove(shape: PixelShape)
}