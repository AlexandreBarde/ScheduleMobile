package fr.ynov.schedule;


import android.widget.Button;
import android.widget.Switch;

public class AlarmClock {
    private String hourAlarmClock;
    private Switch onOffAlarmClock;
    private Button onDelete;

    public AlarmClock(String c_hourAlarmClock, Switch c_onOffAlarmClock, Button c_onDelete) {
        hourAlarmClock = c_hourAlarmClock;
        onOffAlarmClock = c_onOffAlarmClock;
        onDelete = c_onDelete;
    }

    public String getHourAlarmClock() {
        return hourAlarmClock;
    }
    public Switch getOnOffAlarmClock() {
        return onOffAlarmClock;
    }
    public Button getOnDelete() {
        return onDelete;
    }
}
