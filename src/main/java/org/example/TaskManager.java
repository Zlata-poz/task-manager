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

    public void getOutstandingTask() {
        System.out.println("Вы хотите ознакомиться со списком:");
        System.out.println("1. Выполненных задач");
        System.out.println("2. Невыполненных задач");
        int number = safeReadInt(scanner);
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

    public void addTask() {
        System.out.println("Введите название задачи: ");
        String name;
        while (true) {
            name = scanner.nextLine();
            if (name.trim().isEmpty()) {
                System.out.println("Введите не пустую строку.");
            } else {
                break;
            }
        }

        LocalDateTime dateTime = null;
        while (dateTime == null) {
            System.out.println("Введите дедлайн для задачи: ");
            String date = scanner.nextLine();
            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm", Locale.ENGLISH);
            try {
                dateTime = LocalDateTime.parse(date, dateFormat);
            } catch (Exception e) {
                System.out.println("Вы ввели дату и/или время неправильно, повторите снова.");
            }
        }
        Task task1 = new Task(name, dateTime);
        tasks.add(task1);
        System.out.println("Задача успешно добавлена.");
        sortTask();
        saveTaskToFile("tasks.json", tasks);
    }

    public void changeTaskState() {
        System.out.println("Введите название выполненной задачи");

        String name;
        while (true) {
            name = scanner.nextLine();
            if (name.trim().isEmpty()) {
                System.out.println("Введите не пустую строку.");
            } else {
                break;
            }
        }

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

    public void deleteTask() {
        System.out.println("Введите название задачи, которую хотите удалить");
        String name = scanner.nextLine();
        tasks.removeIf(task1 -> task1.getName().equalsIgnoreCase(name));
    }

    public void cleanTaskList() {
        System.out.println("Какой список вы хотите очистить?");
        System.out.println("1. Выполненных задач");
        System.out.println("2. Невыполненных задач");
        int number = safeReadInt(scanner);
        boolean warning = true;
        while (warning) {
            if (number == 1) {
                completedTasks.clear();
                warning = false;
            } else if (number == 2) {
                tasks.clear();
                warning = false;
            } else {
                System.out.println("Некорректный ввод данных. Повторите снова.");
            }
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

    public int safeReadInt(Scanner scanner) {
        while (true) {
            String input = scanner.nextLine();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Ошибка! Введите число!");
            }
        }
    }

    public void saveTaskToFile(String filename, List<Task> taskList) {
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
