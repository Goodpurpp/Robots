package robots.gui.common

import robots.localisation.LocalisationChangeable
import robots.localisation.LocalisationEnum
import robots.localisation.RobotsLocalisation
import java.awt.Component
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.beans.PropertyChangeEvent
import java.beans.PropertyChangeListener
import java.nio.file.Path
import javax.swing.JOptionPane
import javax.swing.event.InternalFrameAdapter
import javax.swing.event.InternalFrameEvent

class RobotsWindowAdapter(
    private val window: RobotsJFrame,
    private val jsonPath: Path
) : WindowAdapter() {
    override fun windowOpened(e: WindowEvent?) {
        val gsonHelper = GsonHelper<RobotsJFrameState>()
        gsonHelper.loadFromJson(jsonPath, RobotsJFrameState::class.java)?.let {
            window.readWindowState(
                when (askUserForLoadState(window)) {
                    JOptionPane.YES_OPTION -> {
                        it
                    }

                    else -> {
                        null
                    }
                }
            )
        }
    }

    override fun windowClosing(event: WindowEvent) {
        when (askUserForCloseComponent(window)) {
            JOptionPane.YES_OPTION -> {
                window.isVisible = false
                window.dispose()
            }

            JOptionPane.NO_OPTION -> {}
        }
    }

    override fun windowClosed(e: WindowEvent?) {
        val gsonHelper = GsonHelper<RobotsJFrameState>()
        gsonHelper.loadToJson(jsonPath, window.writeWindowState())
    }
}

class RobotsInternalFrameAdapter(
    private val internalFrame: RobotsJInternalFrame,
    private val jsonPath: Path
) : InternalFrameAdapter() {
    override fun internalFrameOpened(e: InternalFrameEvent?) {
        val gsonHelper = GsonHelper<RobotsJInternalFrameState>()
        gsonHelper.loadFromJson(jsonPath, RobotsJInternalFrameState::class.java)?.let {
            internalFrame.readWindowState(
                when (askUserForLoadState(internalFrame)) {
                    JOptionPane.YES_OPTION -> {
                        it
                    }

                    else -> {
                        null
                    }
                }
            )
        }
    }

    override fun internalFrameClosing(event: InternalFrameEvent?) {
        when (askUserForCloseComponent(internalFrame)) {
            JOptionPane.YES_OPTION -> {
                internalFrame.isVisible = false
                internalFrame.dispose()
            }

            JOptionPane.NO_OPTION -> {}
        }
    }

    override fun internalFrameClosed(e: InternalFrameEvent?) {
        val gsonHelper = GsonHelper<RobotsJInternalFrameState>()
        gsonHelper.loadToJson(jsonPath, internalFrame.writeWindowState())
    }
}

class RobotsLocaleChangeAdapter(
    private val component: LocalisationChangeable
) : PropertyChangeListener {
    override fun propertyChange(evt: PropertyChangeEvent?) {
        evt?.let {
            RobotsLocalisation.changeLocalisation(LocalisationEnum.valueOf(it.newValue as Long))
            component.changeLocalisation()
        }
    }
}

private fun askUserForLoadState(component: Component?): Int =
    JOptionPane.showOptionDialog(
        component,
        RobotsLocalisation.getString("state.message"),
        RobotsLocalisation.getString("state.title"),
        JOptionPane.YES_NO_OPTION,
        JOptionPane.QUESTION_MESSAGE,
        null,
        arrayOf(RobotsLocalisation.getString("yes"), RobotsLocalisation.getString("no")),
        RobotsLocalisation.getString("no")
    )

private fun askUserForCloseComponent(component: Component?): Int =
    JOptionPane.showOptionDialog(
        component,
        RobotsLocalisation.getString("exit.message"),
        RobotsLocalisation.getString("exit.title"),
        JOptionPane.YES_NO_OPTION,
        JOptionPane.QUESTION_MESSAGE,
        null,
        arrayOf(RobotsLocalisation.getString("yes"), RobotsLocalisation.getString("no")),
        RobotsLocalisation.getString("no")
    )
