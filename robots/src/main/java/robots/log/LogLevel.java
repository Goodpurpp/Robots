package robots.log;

public enum LogLevel
{
    Trace(0),
    Debug(1),
    Info(2),
    Warning(3),
    Error(4),
    Fatal(5);
    
    private int level;
    
    private LogLevel(int level)
    {
        this.level = level;
    }
    
    public int level()
    {
        return level;
    }
}

