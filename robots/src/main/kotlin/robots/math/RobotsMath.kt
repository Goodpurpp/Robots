package robots.math

import robots.gui.common.Pair
import java.awt.Point
import java.lang.Double.compare
import kotlin.math.*

fun distance(x1: Double, y1: Double, x2: Double, y2: Double): Double {
    val diffX = x1 - x2
    val diffY = y1 - y2
    return sqrt(diffX * diffX + diffY * diffY)
}

fun angleTo(fromX: Double, fromY: Double, toX: Double, toY: Double) =
    asNormalizedRadians(atan2(toY - fromY, toX - fromX))

fun asNormalizedRadians(angle: Double) = (angle % (2 * Math.PI) + 2 * Math.PI) % (2 * Math.PI)

fun applyLimits(value: Double, min: Double, max: Double) = max(min, min(value, max))

fun findScalar(first: Double, second: Double) = second / first

fun isIntersecting(firstLine: Pair<Pair<Double, Double>, Pair<Double, Double>>, secondLine: Pair<Point, Point>): Boolean {
    val isInProjectionX = compare(firstLine.first.first, secondLine.first.x.toDouble()) *
                compare(firstLine.first.first, secondLine.second.x.toDouble()) <= 0
    val isInProjectionY = compare(firstLine.first.second, secondLine.second().y.toDouble()) *
            compare(firstLine.first.second, secondLine.first().y.toDouble()) <= 0

    if (isInProjectionX && isInProjectionY) {
        val resultOldPointer = (firstLine.second.first - secondLine.first.x) * (secondLine.second.y - secondLine.first.y) - (firstLine.second.second - secondLine.first.y) * (secondLine.second.x - secondLine.first.x)
        val resultNewPointer = (firstLine.first.first - secondLine.first.x) * (secondLine.second.y - secondLine.first.y) - (firstLine.first.second - secondLine.first.y) * (secondLine.second.x - secondLine.first.x)
        return sign(resultOldPointer) != sign(resultNewPointer)
    }

    return false
}

fun isIntersectingLines(firstLine: Pair<Point, Point>, secondLine: Pair<Point, Point>): Boolean {
    val resultOldPointer = (firstLine.second.x - secondLine.first.x) * (secondLine.second.y - secondLine.first.y) - (firstLine.second.y - secondLine.first.y) * (secondLine.second.x - secondLine.first.x)
    val resultNewPointer = (firstLine.first.x - secondLine.first.x) * (secondLine.second.y - secondLine.first.y) - (firstLine.first.y - secondLine.first.y) * (secondLine.second.x - secondLine.first.x)
    return sign(resultOldPointer.toDouble()) != sign(resultNewPointer.toDouble())
}
