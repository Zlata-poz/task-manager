package org.example;

import java.util.Scanner;

public class Menu {
    private final TaskManager taskManager;
    private final Scanner scanner;

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
                    taskManager.getOutstandingTask();
                    break;
                case 2:
                    taskManager.addTask();
                    break;
                case 3:
                    taskManager.changeTaskState();
                    break;
                case 4:
                    taskManager.deleteTask();
                    break;
                case 5:
                    taskManager.cleanTaskList();
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

        int choise = taskManager.safeReadInt(scanner);
        return choise;
    }


}
