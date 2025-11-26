import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RestaurantBilling extends JFrame {

    JTextField nameField, mobileField, admissionField;
    JComboBox<String> itemMenu;
    JSpinner quantitySpinner;

    // Menu Data
    String[] items = {
        "Pizza", "Burger", "Pasta", "Fries", 
        "Sandwich", "Noodles", "Cold Coffee", "Ice Cream"
    };
    int[] prices = {300, 150, 200, 100, 120, 180, 150, 80};

    // --- COLORS & FONTS ---
    Color primaryColor = new Color(44, 62, 80);   // Dark Blue
    Color bgColor = new Color(236, 240, 241);     // Light Grey
    Font mainFont = new Font("Segoe UI", Font.PLAIN, 14);
    Font titleFont = new Font("Segoe UI", Font.BOLD, 20);

    public RestaurantBilling() {
        setTitle("Bistro Billing System");
        setSize(500, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // 1. TOP HEADER
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(primaryColor);
        headerPanel.setBorder(new EmptyBorder(15, 0, 15, 0));
        JLabel titleLabel = new JLabel("Restaurant Billing");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(titleFont);
        headerPanel.add(titleLabel);
        add(headerPanel, BorderLayout.NORTH);

        // 2. FORM PANEL (Center)
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new EmptyBorder(20, 40, 20, 40));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Padding between items
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // --- ADDING COMPONENTS ---
        
        // Row 1: Name
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.3;
        formPanel.add(createLabel("Customer Name:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.7;
        nameField = createTextField();
        formPanel.add(nameField, gbc);

        // Row 2: Mobile
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(createLabel("Mobile Number:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 1;
        mobileField = createTextField();
        formPanel.add(mobileField, gbc);

        // Row 3: Admission (Student)
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(createLabel("Student ID (Opt):"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 2;
        admissionField = createTextField();
        admissionField.setToolTipText("Enter for 10% Discount");
        formPanel.add(admissionField, gbc);

        // Row 4: Item Selection
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(createLabel("Select Item:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 3;
        itemMenu = new JComboBox<>(new String[] {
            "Pizza - ₹300", "Burger - ₹150", "Pasta - ₹200", "Fries - ₹100",
            "Sandwich - ₹120", "Noodles - ₹180", "Cold Coffee - ₹150", "Ice Cream - ₹80"
        });
        itemMenu.setFont(mainFont);
        itemMenu.setBackground(Color.WHITE);
        formPanel.add(itemMenu, gbc);

        // Row 5: Quantity
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(createLabel("Quantity:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 4;
        quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 50, 1));
        quantitySpinner.setFont(mainFont);
        formPanel.add(quantitySpinner, gbc);

        add(formPanel, BorderLayout.CENTER);

        // 3. BOTTOM BUTTON (UPDATED)
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(Color.WHITE);
        footerPanel.setBorder(new EmptyBorder(0, 0, 20, 0));
        
        JButton billButton = new JButton("GENERATE BILL");
        billButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        // --- CHANGE HERE: Set Background to Black ---
        billButton.setBackground(Color.BLACK); 
        billButton.setForeground(Color.WHITE);
        // --------------------------------------------
        
        billButton.setFocusPainted(false);
        billButton.setPreferredSize(new Dimension(200, 40));
        billButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        billButton.addActionListener(e -> generateTextBill());
        
        footerPanel.add(billButton);
        add(footerPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    // --- HELPER METHODS FOR STYLING ---
    
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(mainFont);
        label.setForeground(primaryColor);
        return label;
    }

    private JTextField createTextField() {
        JTextField field = new JTextField();
        field.setFont(mainFont);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY), 
            BorderFactory.createEmptyBorder(5, 5, 5, 5) // Internal padding
        ));
        return field;
    }

    public void generateTextBill() {
        String name = nameField.getText().trim();
        String mobile = mobileField.getText().trim();
        String admissionNo = admissionField.getText().trim();

        if (name.isEmpty() || mobile.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter customer details.", "Missing Info", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int index = itemMenu.getSelectedIndex();
        String itemName = items[index];
        int price = prices[index];
        int quantity = (int) quantitySpinner.getValue();

        double subtotal = price * quantity;
        double discount = 0.0;
        
        if (!admissionNo.isEmpty()) {
            discount = subtotal * 0.10; 
        }

        double taxableAmount = subtotal - discount;
        double gst = taxableAmount * 0.05;
        double grandTotal = taxableAmount + gst;

        String fileName = "BillSlip_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".txt";

        try (PrintWriter writer = new PrintWriter(fileName)) {
            writer.println("==================== BISTRO RECEIPT ====================");
            writer.println("Date: " + new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()));
            writer.println("Customer Name : " + name);
            writer.println("Mobile Number : " + mobile);
            if (!admissionNo.isEmpty()) writer.println("Student ID    : " + admissionNo);
            writer.println("--------------------------------------------------------");
            writer.printf("%-20s %-10s %-10s %-10s\n", "Item", "Price", "Qty", "Total");
            writer.println("--------------------------------------------------------");
            writer.printf("%-20s ₹%-9d %-10d ₹%-10.2f\n", itemName, price, quantity, subtotal);
            writer.println("--------------------------------------------------------");
            writer.printf("%-20s ₹%-10.2f\n", "Subtotal:", subtotal);
            if (!admissionNo.isEmpty()) writer.printf("%-20s -₹%-10.2f\n", "Student Disc (10%):", discount);
            writer.printf("%-20s ₹%-10.2f\n", "Taxable Amount:", taxableAmount);
            writer.printf("%-20s +₹%-10.2f\n", "GST (5%):", gst);
            writer.println("--------------------------------------------------------");
            writer.printf("%-20s ₹%-10.2f\n", "GRAND TOTAL:", grandTotal);
            writer.println("========================================================");
            writer.println("Thank you for dining with us!");
            
            Desktop.getDesktop().open(new File(fileName));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error generating bill.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        try { 
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); 
        } catch (Exception e) { e.printStackTrace(); }
        
        SwingUtilities.invokeLater(RestaurantBilling::new);
    }
}
