package fr.ynov.schedule;

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

    public Task(String name_task, String description_task, long timestamp, String state, int image_status)  {
        this.name_task = name_task;
        this.description = description_task;
        this.timestamp = timestamp;
        this.state = state;
        this.image_status = image_status;
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
    public String getDateFormat() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM HH:m");
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
}
