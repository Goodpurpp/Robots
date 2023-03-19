package robots.gui

import java.awt.Component
import javax.swing.JOptionPane

object GuiUtils {
    private val closingOptions = arrayOf("Да", "Нет")
    private val defaultOption = closingOptions[1]

    @JvmStatic
    fun askUserForCloseComponentAndWaitAnswer(component: Component) : Int {
        return JOptionPane.showOptionDialog(
            component,
            "Ряльно выйти?",
            "Выход из приложения",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            closingOptions,
            defaultOption
        )
    }
}