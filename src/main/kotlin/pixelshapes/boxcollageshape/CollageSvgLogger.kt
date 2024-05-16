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
import pixelshapes.BoxPixelShape
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter

class CollageSvgLogger {

    private var body = ""

    fun reset() {
        body = ""
    }

    fun adaptPoint(point: Point): Point {
        return Point(
            (point.first + 10) * 10,
            (point.second + 10) * 10,
        )
    }

    fun addSquare(point: Point) {
        val adapted = adaptPoint(point)
        body += "<rect class=\"point\" x=\"${adapted.first}\" y=\"${adapted.second}\" width=\"10\" height=\"10\" fill=\"white\" stroke-width=\"1\"/>"
    }

    fun addPotentialGoodDiagonal(start: Point, end: Point) {
        val adaptedStart = adaptPoint(start)
        val adaptedEnd = adaptPoint(end)

        body +=
            "<line class=\"potentialGD\" x1=\"${adaptedStart.first}\" y1=\"${adaptedStart.second}\" x2=\"${adaptedEnd.first}\" y2=\"${adaptedEnd.second}\" stroke=\"#800080\" stroke-width=\"2\"/>"
    }

    fun addGoodDiagonal(start: Point, end: Point) {
        val adaptedStart = adaptPoint(start)
        val adaptedEnd = adaptPoint(end)

        body +=
            "<line class=\"GD\" x1=\"${adaptedStart.first}\" y1=\"${adaptedStart.second}\" x2=\"${adaptedEnd.first}\" y2=\"${adaptedEnd.second}\" stroke=\"#FF00FF\" stroke-width=\"1.5\"/>"
    }

    fun addLeftoverLine(start: Point, end: Point) {
        val adaptedStart = adaptPoint(start)
        val adaptedEnd = adaptPoint(end)

        body +=
            "<line class=\"leftover\" x1=\"${adaptedStart.first}\" y1=\"${adaptedStart.second}\" x2=\"${adaptedEnd.first}\" y2=\"${adaptedEnd.second}\" stroke=\"#707070\" stroke-width=\"1.5\"/>"
    }

    fun addSouthEastCorner(point: Point) {
        val adapted = adaptPoint(point)
        body +=
            "<rect class=\"southEast outerCorner corner\" x=\"${adapted.first + 2}\" y=\"${adapted.second + 2}\" width=\"2\" height=\"2\" fill=\"cyan\"/>"
    }

    fun addSouthWestCorner(point: Point) {
        val adapted = adaptPoint(point)
        body +=
            "<rect class=\"southWest outerCorner corner\" x=\"${adapted.first - 4}\" y=\"${adapted.second + 2}\" width=\"2\" height=\"2\" fill=\"#00FF00\"/>"
    }

    fun addNorthEastCorner(point: Point) {
        val adapted = adaptPoint(point)
        body +=
            "<rect class=\"northEast outerCorner corner\" x=\"${adapted.first + 2}\" y=\"${adapted.second - 4}\" width=\"2\" height=\"2\" fill=\"#FFFF00\"/>"
    }

    fun addBox(box: BoxPixelShape) {

        val origin = adaptPoint(box.topLeftPoint)
        val opposite = adaptPoint(Point(box.bottomRightPoint.first + 1, box.bottomRightPoint.second + 1))

        val width = opposite.first - origin.first
        val height = opposite.second - origin.second

        body +=
            "<rect class=\"rectangle\" x=\"${origin.first}\" y=\"${origin.second}\" width=\"${width}\" height=\"${height}\" fill=\"#808080\" stroke-width=\"2\" stroke=\"#707070\"/>"
    }

    fun build() {

        val text = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<svg xmlns=\"http://www.w3.org/2000/svg\" version=\"1.1\" width=\"300\" height=\"300\">\n" +
                "<style xmlns=\"http://www.w3.org/2000/svg\" id=\"style_css_sheet\" type=\"text/css\">\n" +
                "        .rectangle {opacity : 0;}\n" +
                "        .corner {opacity : 0;}\n" +
                "        .GD {opacity : 0;}\n" +
                "        .potentialGD {opacity : 0;}\n" +
                "        .leftover {opacity : 0;}\n" +
                "    </style>" +
                body +
                "</svg>"

        val file = File("./logger.svg")

        val fileWriter = BufferedWriter(FileWriter(file))

        fileWriter.write(text)

        fileWriter.flush()
        fileWriter.close()
    }
}