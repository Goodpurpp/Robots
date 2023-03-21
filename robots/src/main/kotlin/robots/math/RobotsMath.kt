package robots.math

import kotlin.math.atan2
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt

fun distance(x1: Double, y1: Double, x2: Double, y2: Double): Double {
    val diffX = x1 - x2
    val diffY = y1 - y2
    return sqrt(diffX * diffX + diffY * diffY)
}

fun angleTo(fromX: Double, fromY: Double, toX: Double, toY: Double) =
    asNormalizedRadians(atan2(toY - fromY, toX - fromX))

fun asNormalizedRadians(angle: Double) = (angle % (2 * Math.PI) + 2 * Math.PI) % (2 * Math.PI)

fun applyLimits(value: Double, min: Double, max:Double) = max(min, min(value, max))

fun findScalar(first: Double, second: Double) = second / first
