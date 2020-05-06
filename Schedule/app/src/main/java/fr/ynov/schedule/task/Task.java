package fr.ynov.schedule.task;

import android.util.Log;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Task {
    private  String name_task;
    private String description;
    private  long timestamp;
    private  String state;
    private  int image_status;
    private  Long durée_minutes;

    public Task(String name_task, String description_task, long timestamp, String state, int image_status, Long durée)  {
        this.name_task = name_task;
        this.description = description_task;
        this.timestamp = timestamp;
        this.state = state;
        this.image_status = image_status;
        this.durée_minutes = durée;

    }

    public String getName() {
        return this.name_task;
    }

    public String getDescription() {
        return  this.description;
    }

    public String getState () {
        return this.state;
    }
    public String dateFormat() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM HH:mm");
        Date date = new Date(this.timestamp);
        String date_format =  dateFormat.format(date);
        return  date_format;
    }

    public int getImage_status() {
        return image_status;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public long getDurée_minutes() {
        return durée_minutes;
    }

    public String heureFormat() {
        long heu = durée_minutes/60;
        long min = durée_minutes % 60;

        if(heu == 0) {
            return  min + "min";
        }
        else  {
            return  heu + "h" + min + "min";
        }
    }
}
