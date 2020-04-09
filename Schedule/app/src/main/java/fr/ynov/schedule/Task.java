package fr.ynov.schedule;

import android.util.Log;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Task {
    private  String name_task;
    private String description;
    private  long date;
    private  int image_status;

    public Task(String name_task, String description_task, long date_task, int image_status_task)  {
        this.name_task = name_task;
        this.description = description_task;
        this.date = date_task;
        this.image_status = image_status_task;
    }

    public String getName() {
        return this.name_task;
    }
    public String getDescription() {
        return  this.description;
    }
    public Long getDate() {
        return  this.date ;
    }
    public int getImage_status() {
        return this.image_status;
    }
    public String getDateFormat() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM hh:mm");
        Date date = new Date(this.date);
        String date_format =  dateFormat.format(date);
        return  date_format;
    }
}
