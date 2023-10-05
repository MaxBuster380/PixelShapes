package pixelshapes

/**
 * Set of integer coordinates. Read-only.
 */
interface PixelShape : Iterable<Pair<Int, Int>> {
    /**
     * Checks if the point is inside the shape.
     * @param point (X, Y) coordinates to check for.
     * @return True only if a given coordinate is a part of the shape.
     */
    fun contains(point : Pair<Int, Int>) : Boolean

    /**
     * Gets the rectangular convex hull of the shape, meaning the smallest rectangle that contains all its points.
     *
     * @return The smallest BoxPixelShape that contains all the points in the shape.
     */
    fun getBox() : BoxPixelShape {
        val minX = this.minBy { it.first }.first
        val minY = this.minBy { it.second }.second
        val maxX = this.maxBy { it.first }.first
        val maxY = this.maxBy { it.second }.second

        return BoxPixelShape(
            point1 = Pair(minX, minY),
            point2 = Pair(maxX, maxY)
        )
    }
}