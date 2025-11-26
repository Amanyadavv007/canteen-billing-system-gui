import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Simple College Canteen Billing System
 * Single-file console app for college project submission.
 */
public class CanteenBillingSystem {
    static class Item {
        String code, name;
        double price;
        Item(String code, String name, double price) { this.code = code; this.name = name; this.price = price; }
    }

    private static final Map<String, Item> MENU = new LinkedHashMap<>();
    private static final DecimalFormat DF = new DecimalFormat("#0.00");
    private static final Scanner SC = new Scanner(System.in);

    static {
        MENU.put("B1", new Item("B1","Veg Burger", 40));
        MENU.put("B2", new Item("B2","Chicken Burger", 70));
        MENU.put("P1", new Item("P1","Paneer Wrap", 60));
        MENU.put("S1", new Item("S1","Samosa (2 pcs)", 20));
        MENU.put("D1", new Item("D1","Cold Drink", 25));
        MENU.put("T1", new Item("T1","Tea/Coffee", 15));
        MENU.put("M1", new Item("M1","Maggi", 30));
    }

    public static void main(String[] args) {
        System.out.println("=== College Canteen Billing System ===");
        Map<Item, Integer> cart = new LinkedHashMap<>();
        boolean ordering = true;

        while (ordering) {
            showMenu();
            System.out.print("Enter item code (or 'DONE' to finish): ");
            String code = SC.nextLine().trim().toUpperCase();
            if (code.equals("DONE")) break;
            if (!MENU.containsKey(code)) {
                System.out.println("Invalid code — try again.\n");
                continue;
            }
            Item chosen = MENU.get(code);
            System.out.print("Quantity: ");
            int qty;
            try {
                qty = Integer.parseInt(SC.nextLine().trim());
                if (qty <= 0) { System.out.println("Enter positive quantity.\n"); continue; }
            } catch (NumberFormatException e) {
                System.out.println("Invalid number.\n"); continue;
            }
            cart.put(chosen, cart.getOrDefault(chosen, 0) + qty);
            System.out.println(qty + " x " + chosen.name + " added.\n");
        }

        if (cart.isEmpty()) {
            System.out.println("No items ordered. Exiting.");
            return;
        }

        System.out.print("Customer name: ");
        String name = SC.nextLine().trim();
        System.out.print("Student? (y/n): ");
        boolean isStudent = SC.nextLine().trim().equalsIgnoreCase("y");
        boolean applyDiscount = false;
        String roll = "";
        if (isStudent) {
            System.out.print("Enter roll number (for 10% discount): ");
            roll = SC.nextLine().trim();
            if (!roll.isEmpty()) applyDiscount = true;
        }

        // Calculations
        double subtotal = 0.0;
        StringBuilder receipt = new StringBuilder();
        receipt.append("=== College Canteen Receipt ===\n");
        receipt.append("Customer: ").append(name).append("\n");
        if (applyDiscount) receipt.append("Student Roll: ").append(roll).append("\n");
        receipt.append("Date: ").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))).append("\n\n");
        receipt.append(String.format("%-25s %5s %8s\n", "Item", "Qty", "Amount"));
        receipt.append("------------------------------------------------\n");

        for (Map.Entry<Item, Integer> e : cart.entrySet()) {
            Item it = e.getKey();
            int q = e.getValue();
            double line = it.price * q;
            subtotal += line;
            receipt.append(String.format("%-25s %5d %8s\n", it.name, q, DF.format(line)));
        }

        double discount = applyDiscount ? subtotal * 0.10 : 0.0; // 10% student discount
        double taxed = (subtotal - discount);
        double gst = taxed * 0.05; // 5% GST
        double total = taxed + gst;

        receipt.append("------------------------------------------------\n");
        receipt.append(String.format("%-31s %8s\n", "Subtotal:", DF.format(subtotal)));
        if (applyDiscount) receipt.append(String.format("%-31s %8s\n", "Student Discount (10%):", "-" + DF.format(discount)));
        receipt.append(String.format("%-31s %8s\n", "Taxable Amount:", DF.format(taxed)));
        receipt.append(String.format("%-31s %8s\n", "GST (5%):", DF.format(gst)));
        receipt.append(String.format("%-31s %8s\n", "TOTAL:", DF.format(total)));
        receipt.append("\nThank you! Visit again.\n");

        System.out.println("\n" + receipt.toString());

        // Save to file
        String filename = "receipt_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".txt";
        try (FileWriter fw = new FileWriter(filename)) {
            fw.write(receipt.toString());
            System.out.println("Receipt saved as: " + filename);
        } catch (IOException e) {
            System.out.println("Failed to save receipt: " + e.getMessage());
        }
    }

    private static void showMenu() {
        System.out.println("\n--- MENU ---");
        System.out.printf("%-5s %-20s %6s\n", "Code", "Item", "Price");
        System.out.println("------------------------------");
        for (Item it : MENU.values()) {
            System.out.printf("%-5s %-20s %6s\n", it.code, it.name, DF.format(it.price));
        }
        System.out.println();
    }
}