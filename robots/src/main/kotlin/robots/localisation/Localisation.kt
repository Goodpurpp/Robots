package robots.localisation

abstract class Localisation {
    abstract val menuLocalisation: MenuLocalisation
    abstract val gameLocalisation: GameLocalisation
    abstract val logLocalisation: LogLocalisation
    abstract val exitLocalisation: ExitLocalisation

    data class MenuLocalisation(
        val itemsLocalisation: List<MenuItemLocalisation>
    )

    data class MenuItemLocalisation(
        val menuName: String,
        val description: String,
        val itemsName: List<String>
    )

    data class GameLocalisation(
        val gameField: String
    )

    data class LogLocalisation(
        val logProtocolStart: String,
        val testMessage: String
    )

    data class ExitLocalisation(
        val yes: String,
        val no: String,
        val message: String,
        val title: String
    )
}

class RuLocalisation : Localisation() {
    override val menuLocalisation = MenuLocalisation(
        listOf(
            MenuItemLocalisation(
                "Режим отображения",
                "Управление режимом отображения приложения",
                listOf(
                    "Системная схема",
                    "Универсальная схема"
                )
            ),
            MenuItemLocalisation(
                "Тесты",
                "Тестовые команды",
                listOf("Сообщение в лог")
            ),
            MenuItemLocalisation(
                "Выйти",
                "Выход из приложения",
                listOf("Выйти из приложения")
            )
        )
    )
    override val gameLocalisation = GameLocalisation("Игровое поле")
    override val logLocalisation = LogLocalisation("Протокол работает", "Новая строка")
    override val exitLocalisation = ExitLocalisation(
        "Да",
        "Нет",
        "Ряльно выйти?",
        "Выйти из приложения"
    )
}

class EnLocalisation : Localisation() {
    override val menuLocalisation = MenuLocalisation(
        listOf(
            MenuItemLocalisation(
                "Display mode",
                "Application display mode control",
                listOf(
                    "System schema",
                    "Universal schema"
                )
            ),
            MenuItemLocalisation(
                "Tests",
                "Tests commands",
                listOf("Log message")
            ),
            MenuItemLocalisation(
                "Exit",
                "Exit application",
                listOf("Exit application")
            )
        )
    )
    override val gameLocalisation = GameLocalisation("Game field")
    override val logLocalisation = LogLocalisation("Protocol online", "New log")
    override val exitLocalisation = ExitLocalisation(
        "Yes",
        "No",
        "Really exit?",
        "Exit application"
    )
}

