package robots.gui.game;

import java.time.Duration;
import java.time.Instant;

public class CooldownSkillTimer {
    private Instant lastUsageTime;

    public CooldownSkillTimer() {
        lastUsageTime = Instant.now();
    }

    public synchronized void update() {
        lastUsageTime = Instant.now();
    }

    public synchronized boolean isDown() {
        long cooldown = 1;
        return Duration.between(lastUsageTime, Instant.now()).compareTo(Duration.ofSeconds(cooldown)) > 0;
    }
}
