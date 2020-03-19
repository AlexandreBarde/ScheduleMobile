package fr.ynov.schedule;

public class Task {
    private  String name_task;

    public Task(String name_task)  {
        this.name_task = name_task;
    }

    public String getName() {
        return this.name_task;
    }
}
