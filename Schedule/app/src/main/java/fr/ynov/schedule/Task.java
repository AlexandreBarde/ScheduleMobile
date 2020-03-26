package fr.ynov.schedule;

public class Task {
    private  String name_task;
    private String description;
    private  String date;
    private  int image_status;

    public Task(String name_task, String description_task, String date_task, int image_status_task)  {
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
    public String getDate() {
        return  this.date;
    }
    public int getImage_status() {
        return this.image_status;
    }
}
