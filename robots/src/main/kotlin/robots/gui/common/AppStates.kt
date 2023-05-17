package robots.gui.common

import robots.localisation.LocalisationEnum
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

data class Localisation(
    val localisation: LocalisationEnum
)
