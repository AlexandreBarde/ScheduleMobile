package fr.ynov.schedule.stats;

public class StatsTask
{

    private String name;
    private String description;
    private long timestamp;

    public StatsTask(String name, String description, long timestamp)
    {
        this.name = name;
        this.description = description;
        this.timestamp = timestamp;
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

    @Override
    public String toString()
    {
        return "StatsTask{" +
                "name='" + this.getName() + '\'' +
                ", description='" + this.getDescription() + '\'' +
                ", timestamp=" + this.getTimestamp() +
                '}';
    }

}
