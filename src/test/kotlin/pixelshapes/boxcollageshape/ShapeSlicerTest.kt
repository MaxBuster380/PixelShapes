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

import Point
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import pixelshapes.EmptyPixelShape
import pixelshapes.boxcollageshape.ShapeSlicer.CornerDirection.*
import pixelshapes.boxcollageshape.ShapeSlicer.Line
import pixelshapes.collectionpixelshapes.UnionPixelShape
import shapes.bee
import shapes.file

class ShapeSlicerTest {

    @Test
    fun givenTheBeeShape_whenFindingInnerCorners_thenFindsTheExpectedCorners() {

        val shape = bee()

        val slicer = ShapeSlicer(shape)

        val innerCorners = slicer.findInnerCorners()

        assertTrue(innerCorners[SOUTHEAST] != null)
        assertEquals(
            setOf(Point(1, 2), Point(4, 5)),
            innerCorners[SOUTHEAST]!!
        )

        assertTrue(innerCorners[NORTHEAST] != null)
        assertEquals(
            setOf(Point(1, 4), Point(6, 2), Point(7, 4), Point(4, 7), Point(5, 8)),
            innerCorners[NORTHEAST]!!
        )

        assertTrue(innerCorners[SOUTHWEST] != null)
        assertEquals(
            setOf(Point(9, 2), Point(6, 5), Point(2, 1)),
            innerCorners[SOUTHWEST]!!
        )

        assertTrue(innerCorners[NORTHWEST] != null)
        assertEquals(
            setOf(Point(3, 4), Point(4, 2), Point(9, 4), Point(6, 7)),
            innerCorners[NORTHWEST]!!
        )
    }

    @Test
    fun givenTheBeeShape_whenFindingPotentialGoodDiagonals_thenFindsTheExpectedCorners() {

        val shape = bee()

        val slicer = ShapeSlicer(shape)

        val innerCorners = slicer.findInnerCorners()

        val potentialGoodDiagonals = slicer.findPotentialGoodDiagonals(innerCorners)

        assertEquals(
            setOf(
                Line(Point(1, 2), Point(4, 2)),
                Line(Point(4, 5), Point(6, 5)),
                Line(Point(6, 2), Point(9, 2)),
                Line(Point(1, 4), Point(3, 4)),
                Line(Point(7, 4), Point(9, 4)),
                Line(Point(4, 7), Point(6, 7)),
                Line(Point(1, 2), Point(1, 4)),
                Line(Point(4, 5), Point(4, 7)),
                Line(Point(9, 2), Point(9, 4)),
                Line(Point(6, 5), Point(6, 7)),
            ),
            potentialGoodDiagonals
        )


    }

    @Test
    fun givenTheBeeShape_whenChoosingGoodDiagonals_thenExpects6LinesThatDontIntersectWithEachOther() {

        val shape = bee()

        val slicer = ShapeSlicer(shape)
        val innerCorners = slicer.findInnerCorners()
        val potentialGoodDiagonals = slicer.findPotentialGoodDiagonals(innerCorners)
        val goodDiagonals = slicer.chooseGoodDiagonals(potentialGoodDiagonals)

        assertEquals(6, goodDiagonals.size)
        assertTrue(potentialGoodDiagonals.containsAll(goodDiagonals))

        for (lineA in goodDiagonals) {
            for (lineB in goodDiagonals) {
                assertFalse(lineA.touches(lineB))
            }
        }
    }

    @Test
    fun givenTheBeeShape_whenRemovingTheCornersOfTheChosenGoodDiagonals_thenNoDiagonalCornerRemainsInTheHashMap() {

        val shape = bee()

        val slicer = ShapeSlicer(shape)
        val innerCorners = slicer.findInnerCorners()
        val potentialGoodDiagonals = slicer.findPotentialGoodDiagonals(innerCorners)
        val goodDiagonals = slicer.chooseGoodDiagonals(potentialGoodDiagonals)
        slicer.removeInnerCornersOfGoodDiagonals(goodDiagonals, innerCorners)

        assertEquals(0, innerCorners[SOUTHEAST]!!.size)
        assertEquals(0, innerCorners[NORTHWEST]!!.size)

        assertEquals(
            setOf(Point(5, 8)),
            innerCorners[NORTHEAST]
        )

        assertEquals(
            setOf(Point(2, 1)),
            innerCorners[SOUTHWEST]
        )
    }

    @Test
    fun givenTheBeeShape_whenCreatingLeftoverLines_thenExpectsTwoLinesThatDontCrossEachOtherOrTheGoodDiagonals() {

        val shape = bee()

        val slicer = ShapeSlicer(shape)
        val innerCorners = slicer.findInnerCorners()
        val potentialGoodDiagonals = slicer.findPotentialGoodDiagonals(innerCorners)
        val goodDiagonals = slicer.chooseGoodDiagonals(potentialGoodDiagonals)
        slicer.removeInnerCornersOfGoodDiagonals(goodDiagonals, innerCorners)

        val leftoverLines = slicer.traceLinesFromInnerCorners(innerCorners, goodDiagonals)

        assertEquals(2, leftoverLines.size)

        val allLines = leftoverLines union goodDiagonals

        for (lineA in allLines) {
            for (lineB in allLines) {

                assertTrue(lineA == lineB || !lineA.crosses(lineB))
            }
        }
    }

    @Test
    fun givenTheBeeShape_whenCreatingTheTemplateCorners_thenExpectsBorderCorners() {

        val shape = bee()

        val slicer = ShapeSlicer(shape)
        val outerCorners = slicer.findOuterCorners()

        assertEquals(
            setOf(
                Point(1, 0),
                Point(0, 2),
                Point(3, 5),
                Point(4, 4),
            ),
            outerCorners[SOUTHEAST]!!
        )

        assertEquals(
            setOf(
                Point(2, 0),
                Point(9, 1),
                Point(10, 2),
                Point(6, 4),
                Point(7, 5),
            ),
            outerCorners[SOUTHWEST]!!
        )

        assertEquals(
            setOf(
                Point(0, 4),
                Point(1, 5),
                Point(3, 7),
                Point(4, 8),
                Point(5, 9),
                Point(6, 4),
                Point(7, 5),
            ),
            outerCorners[NORTHEAST]!!
        )
    }

    @Test
    fun givenAllPossibleStateForCornerGeneration_whenCheckingIfItShouldCreateASouthEastCorner_thenAllAnswersShouldBeCorrect() {

        val slicer = ShapeSlicer(EmptyPixelShape())

        // NorthWest, North, West, Current, isHorizontal, isMain
        val trueAnswers = setOf(
            listOf(false, true, false, true, true, true),
            listOf(false, false, true, true, false, true),
            listOf(true, true, false, true, true, true),
            listOf(true, false, true, true, false, true),
            listOf(true, true, true, true, true, true),
            listOf(true, true, true, true, false, true),
        )

        val booleanValues = listOf(false, true)

        for (northWest in booleanValues)
            for (north in booleanValues)
                for (west in booleanValues)
                    for (current in booleanValues)
                        for (isHorizontal in booleanValues)
                            for (isMain in booleanValues) {

                                val checkedState = listOf(northWest, north, west, current, isHorizontal, isMain)

                                assertEquals(
                                    trueAnswers.contains(checkedState),
                                    slicer.shouldAddSouthEastOuterCorner(
                                        northWest,
                                        north,
                                        west,
                                        current,
                                        isHorizontal,
                                        isMain
                                    )
                                )
                            }
    }

    @Test
    fun givenAllPossibleStateForCornerGeneration_whenCheckingIfItShouldCreateASouthWestCorner_thenAllAnswersShouldBeCorrect() {

        val slicer = ShapeSlicer(EmptyPixelShape())

        // NorthWest, North, West, Current, isHorizontal, isMain
        val trueAnswers = setOf(
            listOf(false, false, true, true, false, true),
            listOf(false, true, true, true, false, true),
            listOf(true, false, true, false, true, false),
            listOf(true, true, true, false, true, false),
            listOf(true, true, true, true, false, true),
            listOf(true, true, true, true, true, false),
        )

        val booleanValues = listOf(false, true)

        for (northWest in booleanValues)
            for (north in booleanValues)
                for (west in booleanValues)
                    for (current in booleanValues)
                        for (isHorizontal in booleanValues)
                            for (isMain in booleanValues) {

                                val checkedState = listOf(northWest, north, west, current, isHorizontal, isMain)

                                assertEquals(
                                    trueAnswers.contains(checkedState),
                                    slicer.shouldAddSouthWestOuterCorner(
                                        northWest,
                                        north,
                                        west,
                                        current,
                                        isHorizontal,
                                        isMain
                                    ),
                                    checkedState.toString()
                                )
                            }
    }

    @Test
    fun givenAllPossibleStateForCornerGeneration_whenCheckingIfItShouldCreateANorthEastCorner_thenAllAnswersShouldBeCorrect() {

        val slicer = ShapeSlicer(EmptyPixelShape())

        // NorthWest, North, West, Current, isHorizontal, isMain
        val trueAnswers = setOf(
            listOf(true, true, false, false, false, false),
            listOf(false, true, false, true, true, true),
            listOf(true, true, true, false, false, false),
            listOf(false, true, true, true, true, true),
            listOf(true, true, true, true, true, true),
            listOf(true, true, true, true, false, false),
        )

        val booleanValues = listOf(false, true)

        for (northWest in booleanValues)
            for (north in booleanValues)
                for (west in booleanValues)
                    for (current in booleanValues)
                        for (isHorizontal in booleanValues)
                            for (isMain in booleanValues) {

                                val checkedState = listOf(northWest, north, west, current, isHorizontal, isMain)

                                assertEquals(
                                    trueAnswers.contains(checkedState),
                                    slicer.shouldAddNorthEastOuterCorner(
                                        northWest,
                                        north,
                                        west,
                                        current,
                                        isHorizontal,
                                        isMain
                                    ),
                                    checkedState.toString()
                                )
                            }
    }

    @Test
    fun givenTheBeeShape_whenCreatingTheTemplateCorners_thenAllOutputSetsHaveTheSameSize() {

        val shape = bee()

        val slicer = ShapeSlicer(shape)
        val innerCorners = slicer.findInnerCorners()
        val potentialGoodDiagonals = slicer.findPotentialGoodDiagonals(innerCorners)
        val goodDiagonals = slicer.chooseGoodDiagonals(potentialGoodDiagonals)
        slicer.removeInnerCornersOfGoodDiagonals(goodDiagonals, innerCorners)
        val templateCorners = slicer.createCornerTemplates(goodDiagonals, innerCorners)

        val expectedSize = templateCorners[SOUTHEAST]!!.size

        assertEquals(expectedSize, templateCorners[SOUTHWEST]!!.size)
        assertEquals(expectedSize, templateCorners[NORTHEAST]!!.size)
    }

    @Test
    fun givenTheBeeShape_whenCreatingTheBoxes_thenTheCollectionOfCreatedBoxesMatchesTheOriginalShape() {

        val shape = bee()

        val slicer = ShapeSlicer(shape)
        val innerCorners = slicer.findInnerCorners()
        val potentialGoodDiagonals = slicer.findPotentialGoodDiagonals(innerCorners)
        val goodDiagonals = slicer.chooseGoodDiagonals(potentialGoodDiagonals)
        slicer.removeInnerCornersOfGoodDiagonals(goodDiagonals, innerCorners)
        val templateCorners = slicer.createCornerTemplates(goodDiagonals, innerCorners)
        val boxes = slicer.createBoxes(templateCorners)


        assertEquals(10, boxes.size)
        val boxesShapeCollection = UnionPixelShape(boxes)

        assertTrue(shape.containsAll(boxesShapeCollection))
        assertTrue(boxesShapeCollection.containsAll(shape))
    }

    @Test
    fun givenTheFileShape_whenFindingInnerCorners_thenFindsTheExpectedCorners() {

        val shape = file()

        val slicer = ShapeSlicer(shape)

        val innerCorners = slicer.findInnerCorners()

        assertTrue(innerCorners[SOUTHEAST]!!.isEmpty())
        assertTrue(innerCorners[NORTHEAST]!!.isEmpty())
        assertTrue(innerCorners[SOUTHWEST]!!.isEmpty())

        assertEquals(
            setOf(Point(2, 3)),
            innerCorners[NORTHWEST]!!
        )
    }

    @Test
    fun givenTheFileShape_whenCreatingTheTemplateCorners_thenAllOutputSetsHaveTheSameSize() {

        val shape = file()

        val slicer = ShapeSlicer(shape)
        val innerCorners = slicer.findInnerCorners()
        val potentialGoodDiagonals = slicer.findPotentialGoodDiagonals(innerCorners)
        val goodDiagonals = slicer.chooseGoodDiagonals(potentialGoodDiagonals)
        slicer.removeInnerCornersOfGoodDiagonals(goodDiagonals, innerCorners)
        val templateCorners = slicer.createCornerTemplates(goodDiagonals, innerCorners)

        val expectedSize = templateCorners[SOUTHEAST]!!.size

        assertEquals(expectedSize, templateCorners[SOUTHWEST]!!.size)
        assertEquals(expectedSize, templateCorners[NORTHEAST]!!.size)
    }
}