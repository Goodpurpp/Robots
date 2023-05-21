package robots.gui.game;

import java.time.Duration;
import java.time.Instant;
import java.util.Timer;

public class CooldownSkillTimer {
    private Instant cooldown;

    public CooldownSkillTimer() {
        cooldown = Instant.now();
    }
    public synchronized void update() {
        cooldown = Instant.now();
    }
    public synchronized boolean isDown() {
        return Duration.between(cooldown, Instant.now()).compareTo(Duration.ofSeconds(10)) > 0;
    }
}
