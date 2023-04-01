package robots.gui.common

import java.awt.Dimension

data class RobotsJInternalFrameState(
    val dimension: Dimension,
    val isMaximized: Boolean,
    val isIcon: Boolean
)

data class RobotsJFrameState(
    val dimension: Dimension,
    val location: Pair<Int, Int>
)
