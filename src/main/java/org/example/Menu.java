package org.example;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Scanner;

public class Menu {
    private final TaskManager taskManager;
    private final Scanner scanner;

    public String taskName;
    public LocalDateTime deadline;

    public Menu(TaskManager taskManager, Scanner scanner) {
        this.taskManager = taskManager;
        this.scanner = scanner;
    }

    public void start() {
        boolean isRunning = true;
        taskManager.loadTaskFromFile("tasks.json");
        while (isRunning) {
            int task = getMenu();
            switch (task) {
                case 1:
                    getOutstandingTaskInput();
                    taskManager.getOutstandingTask(listNumber);
                    break;
                case 2:
                    addTaskInput();
                    taskManager.addTask(taskName, deadline);
                    break;
                case 3:
                    changeTaskStateInput();
                    taskManager.changeTaskState(taskName);
                    break;
                case 4:
                    deleteTaskInput();
                    taskManager.deleteTask(taskName);
                    break;
                case 5:
                    cleanTaskListInput();
                    taskManager.cleanTaskList(listNumber);
                    break;
                case 6:
                    taskManager.showOverdueTasks();
                    break;
                case 7:
                    taskManager.showTodayTasks();
                    break;
                case 8:
                    isRunning = false;
                    break;
                default:
                    System.out.println("ОШИБКА! Некорректный ввод. Повторите снова.");
            }

        }

    }

    private int getMenu() {
        System.out.println("-------------------- Вы можете выполнить следующие действия: --------------------");
        System.out.println("-------------------- 1. Ознакомиться со списком задач. --------------------------");
        System.out.println("-------------------- 2. Добавить новую задачу. ----------------------------------");
        System.out.println("-------------------- 3. Отметить задачу как выполненную. ------------------------");
        System.out.println("-------------------- 4. Удалить задачу из списка. -------------------------------");
        System.out.println("-------------------- 5. Очистить список задач. ----------------------------------");
        System.out.println("-------------------- 6. Показать просроченные задачи. ---------------------------");
        System.out.println("-------------------- 7. Показать задачи на сегодня. -----------------------------");
        System.out.println("-------------------- 8. Закрыть программу. --------------------------------------");

        int choise = safeReadInt(scanner);
        return choise;
    }

    private int listNumber;

    public void getOutstandingTaskInput() {
        System.out.println("Вы хотите ознакомиться со списком:");
        System.out.println("1. Выполненных задач");
        System.out.println("2. Невыполненных задач");
        listNumber = safeReadInt(scanner);
        while (listNumber != 1 && listNumber != 2) {
            System.out.println("Некорректный ввод. Повторите снова");
            listNumber = safeReadInt(scanner);
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


    public void addTaskInput() {
        System.out.println("Введите название задачи: ");
        while (true) {
            taskName = scanner.nextLine();
            if (taskName.trim().isEmpty()) {
                System.out.println("Введите не пустую строку.");
            } else {
                break;
            }
        }

        while (deadline == null) {
            System.out.println("Введите дедлайн для задачи: ");
            String date = scanner.nextLine();
            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm", Locale.ENGLISH);
            try {
                deadline = LocalDateTime.parse(date, dateFormat);
                break;
            } catch (Exception e) {
                System.out.println("Вы ввели дату и/или время неправильно, повторите снова.");
            }
        }
    }

    public void changeTaskStateInput() {
        System.out.println("Введите название выполненной задачи");

        while (true) {
            taskName = scanner.nextLine();
            if (taskName.trim().isEmpty()) {
                System.out.println("Введите не пустую строку.");
            } else {
                break;
            }
        }
    }

    public void deleteTaskInput() {
        System.out.println("Введите название задачи, которую хотите удалить");
        taskName = scanner.nextLine();
    }

    public void cleanTaskListInput() {
        System.out.println("Какой список вы хотите очистить?");
        System.out.println("1. Выполненных задач");
        System.out.println("2. Невыполненных задач");
        listNumber = safeReadInt(scanner);
        while (listNumber != 1 && listNumber != 2) {
            System.out.println("Некорректный ввод. Повторите снова");
            listNumber = safeReadInt(scanner);
        }
    }


}
