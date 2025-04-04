package org.example;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final GymService gymService = new GymService(new Gym());

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n--- Menu ---");
            System.out.println("1. Register a visitor");
            System.out.println("2. View all visitors");
            System.out.println("3. Register a trainer");
            System.out.println("4. View all trainers");
            System.out.println("5. Purchase a membership");
            System.out.println("6. Assign a visitor to a trainer");
            System.out.println("7. View visitor information");
            System.out.println("8. Export visitors to JSON");
            System.out.println("9. Import visitors from JSON");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> registerVisitor();
                case "2" -> viewVisitors();
                case "3" -> registerTrainer();
                case "4" -> viewTrainers();
                case "5" -> buyMembership();
                case "6" -> assignTrainer();
                case "7" -> showVisitorInfo();
                case "8" -> exportVisitors();
                case "9" -> importVisitors();
                case "0" -> {
                    System.out.println("Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void registerVisitor() {
        System.out.print("Visitor ID: ");
        String id = scanner.nextLine();
        System.out.print("First name: ");
        String name = scanner.nextLine();
        System.out.print("Last name: ");
        String surname = scanner.nextLine();
        System.out.print("Phone number: ");
        String phone = scanner.nextLine();

        Visitor visitor = new Visitor(id, name, surname, phone);
        gymService.registerVisitor(visitor);
        System.out.println("✅ Visitor registered.");
    }

    private static void viewVisitors() {
        List<Visitor> visitors = gymService.getAllVisitors();
        if (visitors.isEmpty()) {
            System.out.println("No registered visitors.");
        } else {
            visitors.forEach(System.out::println);
        }
    }

    private static void registerTrainer() {
        System.out.print("Trainer ID: ");
        String id = scanner.nextLine();
        System.out.print("Trainer name: ");
        String name = scanner.nextLine();

        Trainer trainer = new Trainer(id, name);
        gymService.registerTrainer(trainer);
        System.out.println("✅ Trainer registered.");
    }

    private static void viewTrainers() {
        List<Trainer> trainers = gymService.getAllTrainers();
        if (trainers.isEmpty()) {
            System.out.println("No trainers found.");
        } else {
            trainers.forEach(System.out::println);
        }
    }

    private static void buyMembership() {
        System.out.print("Visitor ID: ");
        String visitorId = scanner.nextLine();
        System.out.print("Membership duration (days): ");
        int days = Integer.parseInt(scanner.nextLine());

        LocalDate start = LocalDate.now();
        LocalDate end = start.plusDays(days);
        Membership membership = new Membership(visitorId, start, end);
        gymService.purchaseMembership(visitorId, membership);
        System.out.println("✅ Membership activated.");
    }

    private static void assignTrainer() {
        System.out.print("Visitor ID: ");
        String visitorId = scanner.nextLine();
        System.out.print("Trainer ID: ");
        String trainerId = scanner.nextLine();

        boolean success = gymService.assignTrainerToVisitor(visitorId, trainerId);
        if (success) {
            System.out.println("✅ Visitor assigned to trainer.");
        } else {
            System.out.println("❌ Failed to assign visitor.");
        }
    }

    private static void showVisitorInfo() {
        System.out.print("Visitor ID: ");
        String id = scanner.nextLine();
        String info = gymService.getVisitorInfo(id);
        System.out.println(info);
    }

    private static void exportVisitors() {
        System.out.print("Enter file path (e.g., visitors.json): ");
        String path = scanner.nextLine();

        System.out.println("Sort by: 1 - First name, 2 - Last name, 3 - ID");
        String sortChoice = scanner.nextLine();

        Comparator<Visitor> comparator = switch (sortChoice) {
            case "1" -> Comparator.comparing(Visitor::getName);
            case "2" -> Comparator.comparing(Visitor::getSurname);
            case "3" -> Comparator.comparing(Visitor::getId);
            default -> Comparator.comparing(Visitor::getName);
        };

        try {
            gymService.exportVisitors(path, comparator);
            System.out.println("✅ Visitors exported.");
        } catch (IOException e) {
            System.out.println("❌ Export error: " + e.getMessage());
        }
    }

    private static void importVisitors() {
        System.out.print("Enter file path (e.g., visitors.json): ");
        String path = scanner.nextLine();

        try {
            List<Visitor> imported = gymService.importVisitors(path);
            System.out.println("✅ Imported " + imported.size() + " visitors.");
        } catch (IOException e) {
            System.out.println("❌ Import error: " + e.getMessage());
        }
    }
}
