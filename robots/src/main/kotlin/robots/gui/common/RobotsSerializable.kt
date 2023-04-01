package robots.gui.common

interface RobotsSerializable<State> {
    fun writeWindowState(): State

    fun readWindowState(state: State?)
}