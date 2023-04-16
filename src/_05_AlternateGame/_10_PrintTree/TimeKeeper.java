package _05_AlternateGame._10_PrintTree;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class TimeKeeper {
    private LocalDateTime startTime;
    private long timeThreshold;
    
    public TimeKeeper(long timeThreshold) {
        this.startTime = LocalDateTime.now();
        this.timeThreshold = timeThreshold;
    }
    
    public boolean isTimeOver() {
        long diff = ChronoUnit.MILLIS.between(this.startTime, LocalDateTime.now());
        return diff >= this.timeThreshold;
    }
}
