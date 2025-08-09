package org.example;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class TaskManager {
    List<Task> tasks = new ArrayList<>(100);
    List<Task> completedTasks = new ArrayList<>(100);
    Scanner scanner;

    public TaskManager(Scanner scanner) {
        this.scanner = scanner;
    }

    public void getOutstandingTask(int number) {
        boolean warning = true;
        while (warning) {
            if (number == 1) {
                if (!completedTasks.isEmpty()) {
                    System.out.printf("%-20s %-20s%n", "Название задачи", "Время завершения задачи");
                    for (Task task : completedTasks) {
                        System.out.printf("%-20s %-20s%n", task.getName(), task.getFinishDate());
                    }
                } else {
                    System.out.println("--------------------------");
                    System.out.println("Еще нет выполненных задач.");
                    System.out.println("--------------------------");
                }
                warning = false;
            } else if (number == 2) {
                if (!tasks.isEmpty()) {
                    System.out.printf("%-20s %-20s %-20s%n", "Название задач", "Дедлайн выполнения", "Время выдачи задачи");
                    for (Task task : tasks) {
                        System.out.printf("%-20s %-20s %-20s%n", task.getName(), task.getDeadlineFormatter(), task.getStartDate());
                    }
                } else {
                    System.out.println("--------------------------------------");
                    System.out.println("Все задачи выполнены. Отличная работа.");
                    System.out.println("--------------------------------------");
                }
                warning = false;
            } else {
                System.out.println("Некорректный ввод данных. Повторите снова.");
            }
        }
    }

    public void addTask(String name, LocalDateTime date) {
        Task task1 = new Task(name, date);
        tasks.add(task1);
        System.out.println("Задача успешно добавлена.");
        sortTask();
        saveTaskToFile("tasks.json", tasks);
    }

    public void changeTaskState(String name) {

        String finalName1 = name;
        Optional<Task> found = tasks.stream().filter(task -> task.getName().equalsIgnoreCase(finalName1)).findFirst();
        if (found.isPresent()) {
            Task task = found.get();
            task.setDone(true);
            task.setFinishDate();
            completedTasks.add(task);
            String finalName = name;
            tasks.removeIf(task1 -> task1.getName().equalsIgnoreCase(finalName));
        } else {
            System.out.println("Такой задачи не существует.");
        }
    }

    public void deleteTask(String name) {
        tasks.removeIf(task1 -> task1.getName().equalsIgnoreCase(name));
    }

    public void cleanTaskList(int number) {
        if (number == 1) {
            completedTasks.clear();
        } else if (number == 2) {
            tasks.clear();
        }
    }

    private void sortTask() {
        tasks.sort(Comparator.comparing(Task::getDeadlineFormatter));
    }

    public void showOverdueTasks() {
        LocalDateTime now = LocalDateTime.now();
        List<Task> overdue = tasks.stream().filter(task -> task.getDeadLine().isBefore(now)).collect(Collectors.toList());

        if (overdue.isEmpty()) {
            System.out.println("Нет просроченных задач.");
        } else {
            System.out.println("Просроченные задачи:");
            overdue.forEach(task -> System.out.printf("%-20s Дедлайн: %s%n", task.getName(), task.getDeadlineFormatter()));
        }
    }

    public void showTodayTasks() {
        LocalDate now = LocalDate.now();
        List<Task> overdue = tasks.stream().filter(task -> task.getDeadLine().toLocalDate().isEqual(now)).collect(Collectors.toList());

        if (overdue.isEmpty()) {
            System.out.println("Нет задач на сегодня.");
        } else {
            System.out.println("Задачи на сегодня:");
            overdue.forEach(task -> System.out.printf("%-20s Дедлайн: %s%n", task.getName(), task.getDeadlineFormatter()));
        }
    }


    private void saveTaskToFile(String filename, List<Task> taskList) {
        Gson gson = new Gson();
        try (Writer writer = new FileWriter(filename)) {
            gson.toJson(taskList, writer);
        } catch (IOException e) {
            System.out.println("Ошибка при сохранении задач: " + e.getMessage());
        }
    }

    public void loadTaskFromFile(String filename) {
        Gson gson = new Gson();
        try (Reader reader = new FileReader(filename)) {
            Type taskListType = new TypeToken<List<Task>>() {
            }.getType();
            tasks = gson.fromJson(reader, taskListType);
            if (tasks == null) {
                tasks = new ArrayList<>();
            }
        } catch (IOException e) {
            System.out.println("Ошибка чтения файла: " + e.getMessage());
            tasks = new ArrayList<>();
        }
    }
}
