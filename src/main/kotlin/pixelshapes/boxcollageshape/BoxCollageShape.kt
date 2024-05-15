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

import Graph
import pixelshapes.BoxPixelShape
import pixelshapes.PixelShape
import pixelshapes.mutablepixelshapes.MutablePixelShape
import pixelshapes.mutablepixelshapes.SetMutablePixelShape
import java.util.*

class BoxCollageShape private constructor(
    private val boxes: Set<BoxPixelShape>
) : PixelShape {

    constructor(template: PixelShape) : this(mutableSetOf()) {

    }

    override val size: Int = boxes.sumOf { it.size }

    fun boxes(): Set<BoxPixelShape> = boxes

    override fun boundingBox(): BoxPixelShape {
        TODO("Not yet implemented")
    }

    override fun contains(element: Pair<Int, Int>): Boolean {
        TODO("Not yet implemented")
    }

    override fun containsAll(elements: Collection<Pair<Int, Int>>): Boolean {
        TODO("Not yet implemented")
    }

    override fun iterator(): Iterator<Pair<Int, Int>> {
        TODO("Not yet implemented")
    }

    /**
     * NW N
     * W  C
     *
     * NW = xC-1, yC-1
     * N  = xC, yC-1
     * W  = xC-1, yC
     *
     * NW = 3
     * N  = 2
     * W  = 1
     * C  = 0
     */
    internal fun getPointsAround(shape: PixelShape, current: Pair<Int, Int>): HashMap<Int, Boolean> {

        val res = mutableSetOf<Pair<Int, Int>>()

        fun tallyPoint(point: Pair<Int, Int>) {
            if (shape.contains(point)) {
                res += point
            }
        }

        val northWest = Pair(current.first - 1, current.second - 1)
        tallyPoint(northWest)

        val north = Pair(current.first, current.second - 1)
        tallyPoint(north)

        val west = Pair(current.first - 1, current.second)
        tallyPoint(west)

        tallyPoint(current)

        return res
    }

    /**
     * Finds corners like this
     *
     * [][]
     *
     * []
     *
     * where [] is inside the shape.
     */
    internal fun findInnerCorners(shape: PixelShape): HashMap<CornerDirection, MutableSet<Corner>> {

        val res = hashMapOf<CornerDirection, MutableSet<Corner>>()
        for (direction in CornerDirection.entries)
            res[direction] = mutableSetOf()

        val boundingBox = shape.boundingBox()

        for (current in boundingBox) {

            val pointsAroundCurrent = getPointsAround(shape, current)

            if (pointsAroundCurrent.size == 3) {

                var direction = CornerDirection.SOUTHEAST

                val north = Pair(current.first, current.second - 1)
                if (!pointsAroundCurrent.contains(north)) {
                    direction = CornerDirection.SOUTHWEST
                }

                val west = Pair(current.first - 1, current.second)
                if (!pointsAroundCurrent.contains(west)) {
                    direction = CornerDirection.NORTHEAST
                }

                if (!pointsAroundCurrent.contains(current)) {
                    direction = CornerDirection.NORTHWEST
                }

                res[direction]!! += Corner(current, direction)
            }
        }

        return res
    }

    internal fun drawHorizontalGoodDiagonal(shape: PixelShape, start: Corner): Optional<Line> {


        var iterator = start.coordinates
        var northOfIterator: Pair<Int, Int>
        var hasIterator = true
        var hasNorthOfIterator = true

        fun updateState() {
            northOfIterator = Pair(iterator.first, iterator.second - 1)
            hasIterator = shape.contains(iterator)
            hasNorthOfIterator = shape.contains(northOfIterator)
        }

        updateState()

        while (hasIterator && hasNorthOfIterator) {

            iterator = Pair(iterator.first + 1, iterator.second)
            updateState()
        }

        if (!hasIterator && !hasNorthOfIterator) return Optional.empty()

        val oppositeCornerDirection = if (hasIterator) {
            CornerDirection.SOUTHWEST
        } else {
            CornerDirection.NORTHWEST
        }

        val foundLine = Line(start, Corner(iterator, oppositeCornerDirection))

        return Optional.of(foundLine)
    }

    internal fun drawVerticalGoodDiagonal(shape: PixelShape, start: Corner): Optional<Line> {


        var iterator = start.coordinates
        var northOfIterator: Pair<Int, Int>
        var hasIterator = true
        var hasNorthOfIterator = true

        fun updateState() {
            northOfIterator = Pair(iterator.first - 1, iterator.second)
            hasIterator = shape.contains(iterator)
            hasNorthOfIterator = shape.contains(northOfIterator)
        }

        updateState()

        while (hasIterator && hasNorthOfIterator) {

            iterator = Pair(iterator.first, iterator.second + 1)
            updateState()
        }

        if (!hasIterator && !hasNorthOfIterator) return Optional.empty()

        val oppositeCornerDirection = if (hasIterator) {
            CornerDirection.NORTHEAST
        } else {
            CornerDirection.NORTHWEST
        }

        val foundLine = Line(start, Corner(iterator, oppositeCornerDirection))

        return Optional.of(foundLine)
    }

    internal fun drawAllGoodDiagonals(
        shape: PixelShape,
        corners: HashMap<CornerDirection, MutableSet<Corner>>
    ): List<Line> {

        val res = mutableListOf<Line>()

        val verticalCornerStarts = corners[CornerDirection.SOUTHEAST]!! union corners[CornerDirection.SOUTHWEST]!!
        val horizontalCornerStarts = corners[CornerDirection.SOUTHEAST]!! union corners[CornerDirection.NORTHEAST]!!

        for (corner in verticalCornerStarts) {

            drawVerticalGoodDiagonal(shape, corner).ifPresent {
                res += it
            }
        }

        for (corner in horizontalCornerStarts) {

            drawHorizontalGoodDiagonal(shape, corner).ifPresent {
                res += it
            }
        }

        return res
    }

    internal fun createNoCrossesMap(lines: List<Line>): HashMap<Line, MutableSet<Line>> {

        val crosses = hashMapOf<Line, MutableSet<Line>>()
        for (line in lines) crosses[line] = mutableSetOf()
        for (i in lines.indices) {
            val currentLine = lines[i]

            for (j in i + 1..<lines.size) {
                val testedLine = lines[j]
                if (!currentLine.crosses(testedLine)) {

                    crosses[currentLine]!! += testedLine
                    crosses[testedLine]!! += currentLine
                }
            }
        }
        return crosses
    }

    internal fun chooseGoodDiagonals(potentialGoodDiagonals: List<Line>): List<Line> {

        // Optimisation : Run Clique finder only on connected components

        val noCrossesMap = createNoCrossesMap(potentialGoodDiagonals)

        val noCrossesGraph = Graph.create<Line>(
            successorsFunction = { noCrossesMap[it]!! },
            areJoinedFunction = { a, b -> !a.crosses(b) }
        )

        val candidateSets = noCrossesGraph.maximalCliques(potentialGoodDiagonals.toSet())

        val result = candidateSets.maxBy { it.size }.toList()

        return result
    }

    internal fun applyCorner(
        corner: Corner,
        corners: HashMap<CornerDirection, MutableSet<Corner>>,
        flipNorthSouth: Boolean
    ) {

        corners[corner.direction]!!.remove(corner)

        val newCornerDirection = if (flipNorthSouth) {
            corner.direction.flipNorthSouth()
        } else {
            corner.direction.flipEastWest()
        }

        val newCorner = Corner(corner.coordinates, newCornerDirection)

        corners[newCornerDirection]!! += newCorner
    }

    internal fun applyCornersOfGoodDiagonals(
        corners: HashMap<CornerDirection, MutableSet<Corner>>,
        goodDiagonals: List<Line>
    ) {

        for (diagonal in goodDiagonals) {

            applyCorner(diagonal.mainCorner, corners, diagonal.isHorizontal())
            applyCorner(diagonal.secondCorner, corners, diagonal.isHorizontal())
        }
    }

    internal fun initBoundaryShape(shape: PixelShape, goodDiagonals: List<Line>): MutablePixelShape {

        val res = SetMutablePixelShape()

        val box = shape.boundingBox()

        for (current in box) {

            val pointsAroundCurrent = getPointsAround(shape, current)

            val insideNeighborsCount = pointsAroundCurrent.size

            if (insideNeighborsCount != 0 && insideNeighborsCount != 4) {
                res += current
            }
        }

        for (diagonal in goodDiagonals) {
            for (iterator in diagonal) {
                res += iterator
            }
        }

        return res
    }

    internal fun findAllOuterCornersOnShape(
        shape: PixelShape,
        cornerMap: HashMap<CornerDirection, MutableSet<Corner>>
    ) {

        val boundingBox = shape.boundingBox()

        for (current in boundingBox) {

            val pointsAroundCurrent = getPointsAround(shape, current)

            if (pointsAroundCurrent.size == 1) {

                var direction = CornerDirection.NORTHEAST

                val west = Pair(current.first - 1, current.second)
                if (pointsAroundCurrent.contains(west)) {
                    direction = CornerDirection.SOUTHWEST
                }

                if (pointsAroundCurrent.contains(current)) {
                    direction = CornerDirection.SOUTHEAST
                }

                cornerMap[direction]!! += Corner(current, direction)
            }
        }
    }

    internal fun findAllOuterCorners(
        shape: PixelShape,
        goodDiagonals: List<Line>,
        remainingInnerCorners: HashMap<CornerDirection, MutableSet<Corner>>
    ): HashMap<CornerDirection, MutableSet<Corner>> {

        val boundaryShape = initBoundaryShape(shape, goodDiagonals)

        val cornerMap = hashMapOf<CornerDirection, MutableSet<Corner>>()
        cornerMap[CornerDirection.SOUTHEAST] = mutableSetOf()
        cornerMap[CornerDirection.SOUTHWEST] = mutableSetOf()
        cornerMap[CornerDirection.NORTHEAST] = mutableSetOf()

        findAllOuterCornersOnShape(shape, cornerMap)


    }

}