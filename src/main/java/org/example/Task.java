package org.example;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Task {
    private String name; //название задачи
    private boolean isDone; //статус выполнения задачи
    private LocalDateTime startDate; //время выдачи задачи
    private LocalDateTime deadline;
    private LocalDateTime finishDate; //время завершения работы над задачей

    public Task(String name, LocalDateTime deadline) {
        this.name = name;
        this.isDone = false;
        this.startDate = LocalDateTime.now();
        this.deadline = deadline;
        this.finishDate = null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFinishDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return finishDate.format(formatter);
    }

    public void setFinishDate() {
        this.finishDate = LocalDateTime.now();
    }

    public String getStartDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return startDate.format(formatter);
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public String getDeadlineFormatter() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return deadline.format(formatter);
    }

    public LocalDateTime getDeadLine() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        this.isDone = done;
    }

}
