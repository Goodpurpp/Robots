package robots.gui.common;

import lombok.Getter;

import java.nio.file.Path;

public enum PathEnum {
    BASE_JSON_PATH(Path.of("robots", "src", "main", "resources", "app-state")),
    MAIN_APPLICATION_FRAME_JSON(Path.of(BASE_JSON_PATH.path.toString(), "main-application-frame.json")),
    LOG_WINDOW_JSON_PATH(Path.of(BASE_JSON_PATH.path.toString(), "log-window.json")),
    GAME_WINDOW_JSON_PATH(Path.of(BASE_JSON_PATH.path.toString(), "game-window.json")),
    LOCALISATION_JSON_PATH(Path.of(BASE_JSON_PATH.path.toString(), "localisation.json"));

    @Getter
    private final Path path;

    PathEnum(Path path) {
        this.path = path;
    }
}
