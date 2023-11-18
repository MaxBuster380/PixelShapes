package pixelshapes

import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class BoxPixelShapeTest {
    @Test
    fun iteratorIteratesAllPoints() {
        val mask = 1023

        val x1 = Random.nextInt() and mask
        val x2 = Random.nextInt() and mask
        val y1 = Random.nextInt() and mask
        val y2 = Random.nextInt() and mask

        val topLeftPoint = Pair(
            if (x1 < x2) { x1 } else { x2 },
            if (y1 < y2) { y1 } else { y2 }
        )

        val bottomRightPoint = Pair(
            if (x1 >= x2) { x1 } else { x2 },
            if (y1 >= y2) { y1 } else { y2 }
        )

        val manualPointsInShape = mutableSetOf<Pair<Int, Int>>()
        for(x in topLeftPoint.first..bottomRightPoint.first) {
            for(y in topLeftPoint.second..bottomRightPoint.second) {
                manualPointsInShape += Pair(x, y)
            }
        }

        val shape = BoxPixelShape(topLeftPoint, bottomRightPoint)

        for(point in shape) {
            assertTrue(manualPointsInShape.contains(point))
            manualPointsInShape.remove(point)
        }

        assertTrue(manualPointsInShape.isEmpty())
    }

    @Test
    fun areaMatchesNbPointsIteratedMatchesSize() {
        val shape = BoxPixelShape(41, 63)

        val area = shape.getWidth() * shape.getHeight()

        var nbPoints = 0
        for (point in shape) {
            nbPoints++
        }

        assertEquals(area, nbPoints)
        assertEquals(area, shape.getSize())
        assertEquals(nbPoints, shape.getSize())
    }
}