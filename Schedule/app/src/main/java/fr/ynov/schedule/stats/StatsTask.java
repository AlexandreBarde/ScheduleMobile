package fr.ynov.schedule.stats;

public class StatsTask
{

    private String name;
    private String description;
    private long timestamp;
    private String state;

    public StatsTask(String name, String description, long timestamp, String state)
    {
        this.name = name;
        this.description = description;
        this.timestamp = timestamp;
        this.state = state;
    }

    public String getName()
    {
        return name;
    }

    public String getDescription()
    {
        return description;
    }

    public long getTimestamp()
    {
        return timestamp;
    }

    public String getState()
    {
        return state;
    }

    @Override
    public String toString()
    {
        return "StatsTask{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", timestamp=" + timestamp +
                ", state='" + state + '\'' +
                '}';
    }

}
