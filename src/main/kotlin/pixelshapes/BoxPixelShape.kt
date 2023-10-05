package pixelshapes

import kotlin.math.max
import kotlin.math.min

/**
 * Rectangular area between two points.
 */
class BoxPixelShape(point1 : Pair<Int, Int>, point2 : Pair<Int, Int>) : PixelShape {
    private val topLeftPoint : Pair<Int, Int>
    private val bottomRightPoint : Pair<Int, Int>

    private class BoxPixelShapeIterator(private val shape : BoxPixelShape) : Iterator<Pair<Int, Int>> {
        private var currentX = shape.topLeftPoint.first
        private var currentY = shape.topLeftPoint.second

        override fun hasNext(): Boolean {
            return currentY <= shape.bottomRightPoint.second
        }

        override fun next(): Pair<Int, Int> {
            val res = Pair(currentX, currentY)

            currentX += 1
            if (currentX > shape.bottomRightPoint.first) {
                currentX = shape.topLeftPoint.first
                currentY += 1
            }

            return res
        }
    }

    init {
        topLeftPoint = Pair(min(point1.first, point2.first), min(point1.second, point2.second))
        bottomRightPoint = Pair(max(point1.first, point2.first), max(point1.second, point2.second))
    }

    override fun contains(point: Pair<Int, Int>): Boolean {
        return point.first in topLeftPoint.first..bottomRightPoint.first &&
                point.second in topLeftPoint.second..bottomRightPoint.second
    }

    override fun iterator(): Iterator<Pair<Int, Int>> {
        return BoxPixelShapeIterator(this)
    }

    override fun getBox(): BoxPixelShape {
        return this
    }
}