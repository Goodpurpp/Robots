package robots.math

import robots.gui.common.Pair
import java.awt.Point
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
    val isInProjectionX =
        java.lang.Double.compare(firstLine.first.first, secondLine.first.x.toDouble()) * java.lang.Double.compare(
            firstLine.first.first,
            secondLine.second.x.toDouble()
        ) <= 0
    val isInProjectionY =
        java.lang.Double.compare(firstLine.first.second, secondLine.second().y.toDouble()) * java.lang.Double.compare(
            firstLine.first.second,
            secondLine.first().y.toDouble()
        ) <= 0

    if (isInProjectionX && isInProjectionY) {
        val resultOldPointer: Double = (firstLine.second.first - secondLine.first.x) * (secondLine.second.y - secondLine.first.y) - (firstLine.second.second - secondLine.first.y) * (secondLine.second.x - secondLine.first.x)
        val resultNewPointer: Double = (firstLine.first.first - secondLine.first.x) * (secondLine.second.y - secondLine.first.y) - (firstLine.first.second - secondLine.first.y) * (secondLine.second.x - secondLine.first.x)
        return sign(resultOldPointer) != sign(resultNewPointer)
    }

    return false;
}

fun isIntersectingLines(firstLine: Pair<Point, Point>, secondLine: Pair<Point, Point>): Boolean =
    isIntersecting(Pair(Pair(firstLine.first.x.toDouble(), firstLine.first.y.toDouble()), Pair(firstLine.second.x.toDouble(), firstLine.second.y.toDouble())), secondLine)
