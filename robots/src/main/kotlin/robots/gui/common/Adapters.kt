package robots.gui.common

import robots.gui.utils.GuiUtils
import java.awt.Component
import java.awt.Window
import java.awt.event.ComponentAdapter
import java.awt.event.ComponentEvent
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import javax.swing.JInternalFrame
import javax.swing.JOptionPane
import javax.swing.event.InternalFrameAdapter
import javax.swing.event.InternalFrameEvent

class RobotsWindowAdapter(
    private val window: Window
) : WindowAdapter() {
    override fun windowClosing(event: WindowEvent) {
        when (GuiUtils.askUserForCloseComponentAndWaitAnswer(window)) {
            JOptionPane.YES_OPTION -> {
                window.isVisible = false
                window.dispose()
            }
            JOptionPane.NO_OPTION -> {}
        }
    }
}

class RobotsInternalFrameAdapter(
    private val internalFrame: JInternalFrame
): InternalFrameAdapter() {
    override fun internalFrameClosing(event: InternalFrameEvent?) {
        when (GuiUtils.askUserForCloseComponentAndWaitAnswer(internalFrame)) {
            JOptionPane.YES_OPTION -> {
                internalFrame.isVisible = false
                internalFrame.dispose()
            }
            JOptionPane.NO_OPTION -> {}
        }
    }
}
