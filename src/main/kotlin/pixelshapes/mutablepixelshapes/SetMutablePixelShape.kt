package pixelshapes.mutablepixelshapes

import pixelshapes.PixelShape

/**
 * Implementation of MutablePixelShape using a set.
 */
class SetMutablePixelShape : MutablePixelShape {
    private var points = mutableListOf<Pair<Int, Int>>()
    override fun add(point: Pair<Int, Int>) {
        points.add(point)
    }

    override fun add(shape: PixelShape) {
        for (point in shape) {
            points.add(point)
        }
    }

    override fun remove(point: Pair<Int, Int>) {
        points.remove(point)
    }

    override fun remove(shape: PixelShape) {
        for (point in shape) {
            points.remove(point)
        }
    }

    override fun contains(point: Pair<Int, Int>): Boolean {
        return point in points
    }

    override fun iterator(): Iterator<Pair<Int, Int>> {
        return points.iterator()
    }
}