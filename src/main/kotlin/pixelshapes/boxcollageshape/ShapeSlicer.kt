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
import Point
import pixelshapes.BoxPixelShape
import pixelshapes.PixelShape
import pixelshapes.mutablepixelshapes.MutablePixelShape
import pixelshapes.mutablepixelshapes.SetMutablePixelShape

internal class ShapeSlicer(
    private val shape: PixelShape
) {

    private val boundingBox = shape.boundingBox()

    private val widerBoundingBox = BoxPixelShape(
        Pair(boundingBox.topLeftPoint.first - 1, boundingBox.topLeftPoint.second - 1),
        Pair(boundingBox.bottomRightPoint.first + 1, boundingBox.bottomRightPoint.second + 1)
    )

    internal enum class CornerDirection {

        /**
         * X positive, Y positive
         */
        NORTHEAST,

        /**
         * X negative, Y positive
         */
        NORTHWEST,

        /**
         * X positive, Y negative
         */
        SOUTHEAST,

        /**
         * X negative, Y negative
         */
        SOUTHWEST

        ;

    }

    internal data class Line(val mainCorner: Point, val secondCorner: Point) : Iterable<Point> {

        init {
            if (mainCorner == secondCorner)
                throw Exception("Corners cannot overlap.")
        }

        fun length(): Int {
            return (secondCorner.first - mainCorner.first) + (secondCorner.second - mainCorner.second)
        }

        fun isHorizontal(): Boolean = mainCorner.second == secondCorner.second

        fun isVertical(): Boolean = !isHorizontal()

        fun horizontalRange(): IntRange {
            return IntRange(mainCorner.first, secondCorner.first)
        }

        fun verticalRange(): IntRange {
            return IntRange(mainCorner.second, secondCorner.second)
        }

        fun crosses(other: Line): Boolean {

            if (!this.touches(other)) return false

            val horizontal = if (this.isHorizontal()) this else other
            val vertical = if (this == horizontal) other else this

            val verticalX = vertical.mainCorner.first
            val horizontalY = horizontal.mainCorner.second

            val horizontalRange = horizontal.horizontalRange()
            val verticalRange = vertical.verticalRange()

            return verticalX != horizontalRange.first
                    && verticalX != horizontalRange.last
                    && horizontalY != verticalRange.first
                    && horizontalY != verticalRange.last
        }

        fun touches(other: Line): Boolean {

            if (this.isHorizontal() == other.isHorizontal()) return false

            val horizontal = if (this.isHorizontal()) this else other
            val vertical = if (this == horizontal) other else this

            val verticalX = vertical.mainCorner.first
            val horizontalY = horizontal.mainCorner.second

            return verticalX in horizontal.horizontalRange() && horizontalY in vertical.verticalRange()
        }

        override fun iterator(): Iterator<Point> {
            val box = BoxPixelShape(mainCorner, secondCorner)
            return box.iterator()
        }
    }

    internal data class NeighborTally(
        val hasNorthWest: Boolean,
        val hasNorth: Boolean,
        val hasWest: Boolean,
        val hasCurrent: Boolean
    ) {

        constructor(shape: PixelShape, current: Point) : this(
            hasNorthWest = shape.contains(Point(current.first - 1, current.second - 1)),
            hasNorth = shape.contains(Point(current.first, current.second - 1)),
            hasWest = shape.contains(Point(current.first - 1, current.second)),
            hasCurrent = shape.contains(current),
        )

        val size: Int

        init {
            var tempSize = 0

            if (hasNorthWest) tempSize += 1
            if (hasNorth) tempSize += 1
            if (hasWest) tempSize += 1
            if (hasCurrent) tempSize += 1

            size = tempSize
        }
    }

    fun use(): Set<BoxPixelShape> {

        val innerCorners = findInnerCorners()

        val potentialGoodDiagonals = findPotentialGoodDiagonals(innerCorners)

        val goodDiagonals = chooseGoodDiagonals(potentialGoodDiagonals)

        removeInnerCornersOfGoodDiagonals(goodDiagonals, innerCorners)

        val templateCorners = createCornerTemplates(goodDiagonals, innerCorners)

        return createBoxes(templateCorners)
    }

    internal fun findInnerCorners(): HashMap<CornerDirection, MutableSet<Point>> {

        val res = hashMapOf<CornerDirection, MutableSet<Point>>()
        res[CornerDirection.SOUTHEAST] = mutableSetOf()
        res[CornerDirection.NORTHEAST] = mutableSetOf()
        res[CornerDirection.SOUTHWEST] = mutableSetOf()
        res[CornerDirection.NORTHWEST] = mutableSetOf()

        for (current in boundingBox) {

            val tally = NeighborTally(shape, current)

            if (tally.size == 3) {

                if (!tally.hasNorthWest) {
                    res[CornerDirection.SOUTHEAST]!! += current
                    continue
                }

                if (!tally.hasNorth) {
                    res[CornerDirection.SOUTHWEST]!! += current
                    continue
                }

                if (!tally.hasWest) {
                    res[CornerDirection.NORTHEAST]!! += current
                    continue
                }

                if (!tally.hasCurrent) {
                    res[CornerDirection.NORTHWEST]!! += current
                    continue
                }
            }
        }

        return res
    }

    private fun traceLine(
        westOrigin: Point,
        next: (Point) -> Point,
        stop: (Point) -> Boolean
    ): Line {

        var cursor = westOrigin

        do {
            cursor = next(cursor)
        } while (!stop(cursor))

        return Line(westOrigin, cursor)
    }

    internal fun findPotentialGoodDiagonals(
        innerCorners: HashMap<CornerDirection, MutableSet<Point>>
    ): Set<Line> {

        val res = mutableSetOf<Line>()

        val eastwardStarts = innerCorners[CornerDirection.SOUTHEAST]!! union innerCorners[CornerDirection.NORTHEAST]!!
        for (start in eastwardStarts) {

            val line = traceLine(start, { Point(it.first + 1, it.second) }, { NeighborTally(shape, it).size != 4 })

            if (NeighborTally(shape, line.secondCorner).size == 3)
                res += line
        }

        val southwardStarts = innerCorners[CornerDirection.SOUTHEAST]!! union innerCorners[CornerDirection.SOUTHWEST]!!
        for (start in southwardStarts) {

            val line = traceLine(start, { Point(it.first, it.second + 1) }, { NeighborTally(shape, it).size != 4 })

            if (NeighborTally(shape, line.secondCorner).size == 3)
                res += line
        }

        return res
    }

    private fun createNoCrossesMap(
        potentialGoodDiagonals: Set<Line>
    ): HashMap<Line, Set<Line>> {

        val noCrossesMap = hashMapOf<Line, Set<Line>>()
        val potentialGoodDiagonalsList = potentialGoodDiagonals.toList()

        for (mainIndex in potentialGoodDiagonalsList.indices) {
            val mainLine = potentialGoodDiagonalsList[mainIndex]

            val noCrossesOfMainLine = mutableSetOf<Line>()
            for (secondIndex in (mainIndex + 1)..potentialGoodDiagonalsList.lastIndex) {
                val secondLine = potentialGoodDiagonalsList[secondIndex]

                if (!mainLine.touches(secondLine)) {
                    noCrossesOfMainLine += secondLine
                }
            }
            noCrossesMap[mainLine] = noCrossesOfMainLine
        }

        return noCrossesMap
    }

    internal fun chooseGoodDiagonals(
        potentialGoodDiagonals: Set<Line>
    ): Set<Line> {

        val noCrossesMap = createNoCrossesMap(potentialGoodDiagonals)

        val noCrossesGraph = Graph.create<Line>(
            successorsFunction = {
                noCrossesMap[it]!!
            },
            areJoinedFunction = { a, b ->
                !a.touches(b)
            }
        )

        val candidateResults = noCrossesGraph.maximalCliques(potentialGoodDiagonals)

        return candidateResults.maxBy { it.size }
    }

    internal fun removeInnerCornersOfGoodDiagonals(
        goodDiagonals: Set<Line>,
        innerCorners: HashMap<CornerDirection, MutableSet<Point>>
    ) {

        for (line in goodDiagonals) {

            for (direction in innerCorners.keys) {

                innerCorners[direction]!!.remove(line.mainCorner)
                innerCorners[direction]!!.remove(line.secondCorner)
            }
        }
    }

    internal fun findOuterCorners(): HashMap<CornerDirection, MutableSet<Point>> {

        val res = hashMapOf<CornerDirection, MutableSet<Point>>()
        res[CornerDirection.SOUTHEAST] = mutableSetOf()
        res[CornerDirection.NORTHEAST] = mutableSetOf()
        res[CornerDirection.SOUTHWEST] = mutableSetOf()

        for (current in widerBoundingBox) {

            val tally = NeighborTally(shape, current)

            if (tally.size == 1) {

                if (tally.hasCurrent) {
                    res[CornerDirection.SOUTHEAST]!! += current
                    continue
                }

                if (tally.hasNorth) {
                    res[CornerDirection.NORTHEAST]!! += current
                    continue
                }

                if (tally.hasWest) {
                    res[CornerDirection.SOUTHWEST]!! += current
                    continue
                }
            }

            if (tally.size == 2) {

                if (tally.hasWest && tally.hasNorth) {
                    res[CornerDirection.NORTHEAST]!! += current
                    res[CornerDirection.SOUTHWEST]!! += current
                    continue
                }

                if (tally.hasNorthWest && tally.hasCurrent) {
                    res[CornerDirection.SOUTHEAST]!! += current
                    continue
                }
            }
        }

        return res
    }

    internal fun initNegativeShape(): MutablePixelShape {

        val res = SetMutablePixelShape()

        res += widerBoundingBox
        res -= shape

        return res
    }

    internal fun traceLinesFromInnerCorners(
        innerCorners: HashMap<CornerDirection, MutableSet<Point>>,
        goodDiagonals: Set<Line>
    ): Set<Line> {

        val stopShape = initNegativeShape()

        for (goodDiagonal in goodDiagonals) {
            stopShape += goodDiagonal
        }

        val southwardStarts = innerCorners[CornerDirection.SOUTHEAST]!! union innerCorners[CornerDirection.SOUTHWEST]!!
        val northwardStarts = innerCorners[CornerDirection.NORTHWEST]!! union innerCorners[CornerDirection.NORTHEAST]!!

        val res = mutableSetOf<Line>()

        for (start in southwardStarts) {

            val line = traceLine(start, { Point(it.first, it.second + 1) }, { stopShape.contains(it) })
            stopShape += line
            res += line
        }

        for (start in northwardStarts) {

            val line = traceLine(start, { Point(it.first, it.second - 1) }, { stopShape.contains(it) })

            val correctedLine = Line(line.secondCorner, line.mainCorner)

            stopShape += correctedLine
            res += correctedLine
        }

        return res

    }

    internal fun shouldAddSouthEastOuterCorner(
        hasNorthWest: Boolean,
        hasNorth: Boolean,
        hasWest: Boolean,
        hasCurrent: Boolean,
        lineIsHorizontal: Boolean,
        isMainCorner: Boolean
    ): Boolean {

        val d = hasNorthWest
        val w = hasWest
        val n = hasNorth
        val c = hasCurrent
        val h = lineIsHorizontal
        val m = isMainCorner

        // ~NWC~HM + N~WCHM + DNWCM = MC(~NW~H + N~WH + DNW)
        return m && c && ((!n && w && !h) || (n && !w && h) || (d && n && w))
    }

    internal fun shouldAddSouthWestOuterCorner(
        hasNorthWest: Boolean,
        hasNorth: Boolean,
        hasWest: Boolean,
        hasCurrent: Boolean,
        lineIsHorizontal: Boolean,
        isMainCorner: Boolean
    ): Boolean {

        val d = hasNorthWest
        val w = hasWest
        val n = hasNorth
        val c = hasCurrent
        val h = lineIsHorizontal
        val m = isMainCorner

        // ~D~NWC~HM + ~DNWC~HM + D~NW~CH~M + DNW~CH~M + DNWC~HM + DNWCH~M
        return (!d && !n && w && c && !h && m) ||
                (!d && n && w && c && !h && m) ||
                (d && !n && w && !c && h && !m) ||
                ((d && n && w && !c && h && !m)) ||
                ((d && n && w && c && !h && m)) ||
                ((d && n && w && c && h && !m))
    }

    internal fun shouldAddNorthEastOuterCorner(
        hasNorthWest: Boolean,
        hasNorth: Boolean,
        hasWest: Boolean,
        hasCurrent: Boolean,
        lineIsHorizontal: Boolean,
        isMainCorner: Boolean
    ): Boolean {

        val d = hasNorthWest
        val w = hasWest
        val n = hasNorth
        val c = hasCurrent
        val h = lineIsHorizontal
        val m = isMainCorner

        // DN~C~H~M + DNW~H~M + ~DNCHM + NWCHM = N(D~C~H~M + DW~H~M + ~DCHM + WCHM)
        return n && ((d && !c && !h && !m) || (d && w && !h && !m) || (!d && c && h && m) || (w && c && h && m))
    }

    internal fun addOuterCorner(
        point: Point,
        lineIsHorizontal: Boolean,
        isMainCorner: Boolean,
        outerCorners: HashMap<CornerDirection, MutableSet<Point>>
    ) {

        val tally = NeighborTally(shape, point)

        if (shouldAddSouthEastOuterCorner(
                tally.hasNorthWest,
                tally.hasNorth,
                tally.hasWest,
                tally.hasCurrent,
                lineIsHorizontal,
                isMainCorner
            )
        ) {
            outerCorners[CornerDirection.SOUTHEAST]!! += point
        }

        if (shouldAddSouthWestOuterCorner(
                tally.hasNorthWest,
                tally.hasNorth,
                tally.hasWest,
                tally.hasCurrent,
                lineIsHorizontal,
                isMainCorner
            )
        ) {
            outerCorners[CornerDirection.SOUTHWEST]!! += point
        }

        if (shouldAddNorthEastOuterCorner(
                tally.hasNorthWest,
                tally.hasNorth,
                tally.hasWest,
                tally.hasCurrent,
                lineIsHorizontal,
                isMainCorner
            )
        ) {
            outerCorners[CornerDirection.NORTHEAST]!! += point
        }
    }

    internal fun addLine(line: Line, outerCorners: HashMap<CornerDirection, MutableSet<Point>>) {

        addOuterCorner(line.mainCorner, line.isHorizontal(), true, outerCorners)
        addOuterCorner(line.secondCorner, line.isHorizontal(), false, outerCorners)
    }

    internal fun createCornerTemplates(
        goodDiagonals: Set<Line>,
        innerCorners: HashMap<CornerDirection, MutableSet<Point>>
    ): HashMap<CornerDirection, MutableSet<Point>> {

        val res = findOuterCorners()

        for (diagonal in goodDiagonals)
            addLine(diagonal, res)

        val leftoverInnerCornerLines = traceLinesFromInnerCorners(innerCorners, goodDiagonals)

        for (line in leftoverInnerCornerLines)
            addLine(line, res)

        return res
    }

    internal fun createBoxes(templateCorners: HashMap<CornerDirection, MutableSet<Point>>): Set<BoxPixelShape> {

        val res = mutableSetOf<BoxPixelShape>()

        val southEastCorners = templateCorners[CornerDirection.SOUTHEAST]!!
        val southWestCorners = templateCorners[CornerDirection.SOUTHWEST]!!
        val northEastCorners = templateCorners[CornerDirection.NORTHEAST]!!

        for (southEastPoint in southEastCorners) {

            val horizontalLine =
                traceLine(southEastPoint, { Point(it.first + 1, it.second) }, { southWestCorners.contains(it) })
            val verticalLine =
                traceLine(southEastPoint, { Point(it.first, it.second + 1) }, { northEastCorners.contains(it) })

            res += BoxPixelShape(southEastPoint, horizontalLine.length(), verticalLine.length())
        }

        return res
    }
}