package pixelshapes.mutablepixelshapes

/**
 * Implementation of MutablePixelShape using a grid.
 */
class GridShape(
    private val origin : Pair<Int, Int>,
    private val width : Int,
    private val height : Int
) : MutablePixelShape {

    class GridShapeIterator(private val gridShape : GridShape) : Iterator<Pair<Int, Int>> {
        private var currentIndex = 0
        override fun hasNext(): Boolean {
            return currentIndex >= gridShape.width * gridShape.height
        }

        override fun next(): Pair<Int, Int> {
            do {
                currentIndex += 1
            }while(!gridShape.table[currentIndex])

            return gridShape.pointOf(currentIndex)
        }

    }

    private val table : MutableList<Boolean>

    constructor(width : Int, height : Int) : this(Pair(0, 0), width, height)

    init {
        if (width <= 0) { throw IllegalArgumentException("width must be strictly positive, found $width") }
        if (height <= 0) { throw IllegalArgumentException("height must be strictly positive, found $height") }

        table = MutableList(width * height) { false }
    }

    override fun add(point: Pair<Int, Int>) {
        if (!isPointInGrid(point)) { throw IndexOutOfBoundsException("$point is outside the grid's boundary.") }

        table[indexOf(point)] = true
    }

    override fun remove(point: Pair<Int, Int>) {
        if (!isPointInGrid(point)) { throw IndexOutOfBoundsException("$point is outside the grid's boundary.") }

        table[indexOf(point)] = false
    }

    override fun contains(point: Pair<Int, Int>): Boolean {
        if (!isPointInGrid(point)) { throw IndexOutOfBoundsException("$point is outside the grid's boundary.") }

        return table[indexOf(point)]
    }

    override fun iterator(): Iterator<Pair<Int, Int>> {
        return GridShapeIterator(this)
    }

    // PRIVATE INSTANCE METHODS

    /**
     * Gives the same point relative to the origin point if the latter was (0, 0)
     */
    private fun relativeToOrigin(point : Pair<Int, Int>) : Pair<Int, Int> {
        return Pair(point.first - origin.first, point.second - origin.second)
    }

    /**
     * Checks if the given point is inside the defined grid.
     */
    private fun isPointInGrid(point : Pair<Int, Int>) : Boolean {
        val relative = relativeToOrigin(point)
        return relative.first in 0..<width && relative.second in 0..<height
    }

    /**
     * Returns the index of a given point in the internal list.
     * Assumes the point is in the grid
     * @see pointOf
     */
    private fun indexOf(point : Pair<Int, Int>) : Int {
        val relative = relativeToOrigin(point)
        return relative.second * width + relative.first
    }

    /**
     * Returns the point associated with the given index in the list.
     * Assumes the point is in the grid.
     * @see indexOf
     */
    private fun pointOf(index : Int) : Pair<Int, Int> {
        return Pair(index % width + origin.first, index / width + origin.second)
    }
}