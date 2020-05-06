package fr.ynov.schedule.alarm;


import android.widget.Button;
import android.widget.Switch;

public class AlarmClock {
    private String hourAlarmClock;
    private Boolean activation;
    private String day;
    private long timestamp;

    public AlarmClock(String c_hourAlarmClock, Boolean c_activation, String c_day, long c_timestamp) {
        hourAlarmClock = c_hourAlarmClock;
        activation = c_activation;
        day = c_day;
        timestamp = c_timestamp;
    }

    public String getHourAlarmClock() {
        return hourAlarmClock;
    }
    public Boolean getActivation() {
        return activation;
    }
    public String getDay() {
        return day;
    }
    public long getTimestamp() { return timestamp; }
}
