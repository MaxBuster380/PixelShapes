package pixelshapes

/**
 * Set of integer coordinates.
 */
interface PixelShape {
    /**
     * Checks if the point is inside the shape.
     * @param point (X, Y) coordinates to check for.
     * @return True only if a given coordinate is a part of the shape.
     */
    fun contains(point : Pair<Int, Int>) : Boolean
}