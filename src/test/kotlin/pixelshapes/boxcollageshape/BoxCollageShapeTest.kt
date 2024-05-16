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

package pixelshapes.boxcollageshape

import PixelShapePrinter
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import pixelshapes.BoxPixelShape
import pixelshapes.EmptyPixelShape
import pixelshapes.PixelShape
import pixelshapes.mutablepixelshapes.SetMutablePixelShape
import shapes.file
import kotlin.math.abs
import kotlin.random.Random

class BoxCollageShapeTest {

    @Test
    fun givenAnEmptyShape_whenCreatingABoxCollageShape_thenTheCreatedShapeIsAlsoEmpty() {

        val expected = EmptyPixelShape()
        val actual = BoxCollageShape(expected)

        assertTrue(
            resultIdValid(expected, actual),
            PixelShapePrinter().write(actual, -10..10, -10..10)
        )
    }

    @Test
    fun givenAShapeWithNoGoodDiagonal_whenCreatingABoxCollageShape_thenTheCreatedShapeMatchesTheOriginal() {

        val expected = file()

        val actual = BoxCollageShape(expected)

        assertTrue(
            resultIdValid(expected, actual),
            "\n" + PixelShapePrinter().write(expected, 0..5, 0..5)
                    + "\n\n" + PixelShapePrinter().write(actual, 0..5, 0..5)
        )
    }

    @Test
    fun givenARandomShapes_whenCreatingABoxCollageShape_thenMatchesTheOriginal() {

        val originalShape = createRandomShape()

        val collageShape = BoxCollageShape(originalShape)

        assertTrue(
            resultIdValid(originalShape, collageShape)
        )
    }

    @Test
    fun given100RandomShapes_whenCreatingABoxCollageShapeForEach_thenAllMatchTheOriginal() {

        // val logger = CollageSvgLogger()

        for (i in 1..100) {

            // logger.reset()

            val originalShape = createRandomShape()

            val collageShape = BoxCollageShape(originalShape) // logger

            assertTrue(
                resultIdValid(originalShape, collageShape),
                /*"See logger.svg ${logger.build()}"*/
            )
        }
    }

    fun resultIdValid(a: PixelShape, b: BoxCollageShape): Boolean {

        if (a.isEmpty() && b.isEmpty()) return true

        if (!a.containsAll(b)) return false

        if (!b.containsAll(a)) return false

        for (box1 in b.boxes()) {
            for (box2 in b.boxes()) {

                if (box1 == box2) continue

                if ((box1 intersect box2).isNotEmpty())
                    return false
            }
        }

        return true
    }

    fun createRandomShape(): PixelShape {

        val res = SetMutablePixelShape()

        for (i in 1..100) {

            val box = BoxPixelShape(
                x = Random.nextInt() % 10,
                y = Random.nextInt() % 10,
                width = abs(Random.nextInt() % 5),
                height = abs(Random.nextInt() % 5),
            )

            res += box
        }

        return res
    }
}