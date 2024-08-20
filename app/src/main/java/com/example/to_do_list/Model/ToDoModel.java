package com.example.to_do_list.Model;

public class ToDoModel {

    private String task; // Task description
    private int id;      // Unique identifier for the task
    private int status;  // Status of the task (e.g., completed or pending)

    // Getter for task description
    public String getTask() {
        return task;
    }

    // Setter for task description
    public void setTask(String task) {
        this.task = task;
    }

    // Getter for unique identifier
    public int getId() {
        return id;
    }

    // Setter for unique identifier
    public void setId(int id) {
        this.id = id;
    }

    // Getter for status
    public int getStatus() {
        return status;
    }

    // Setter for status
    public void setStatus(int status) {
        this.status = status;
    }
}
