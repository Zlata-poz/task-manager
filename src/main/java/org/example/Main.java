package org.example;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        TaskManager taskManager = new TaskManager(scanner);
        Menu menu = new Menu(taskManager, scanner);
        menu.start();
    }
}