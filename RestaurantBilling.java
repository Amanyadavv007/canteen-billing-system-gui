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

    // UI Styling
    Color primaryColor = new Color(44, 62, 80);
    Font mainFont = new Font("Segoe UI", Font.PLAIN, 14);
    Font titleFont = new Font("Segoe UI", Font.BOLD, 20);

    public RestaurantBilling() {
        setTitle("Bistro Billing System");
        setSize(500, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(primaryColor);
        headerPanel.setBorder(new EmptyBorder(15, 0, 15, 0));
        JLabel titleLabel = new JLabel("Restaurant Billing");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(titleFont);
        headerPanel.add(titleLabel);
        add(headerPanel, BorderLayout.NORTH);

        // Form
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(new EmptyBorder(20, 40, 20, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(createLabel("Customer Name:"), gbc);
        gbc.gridx = 1;
        nameField = createTextField();
        formPanel.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(createLabel("Mobile Number:"), gbc);
        gbc.gridx = 1;
        mobileField = createTextField();
        formPanel.add(mobileField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(createLabel("Student ID (Opt):"), gbc);
        gbc.gridx = 1;
        admissionField = createTextField();
        admissionField.setToolTipText("10% Student Discount");
        formPanel.add(admissionField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(createLabel("Select Item:"), gbc);
        gbc.gridx = 1;
        itemMenu = new JComboBox<>(new String[]{
                "Pizza - ₹300", "Burger - ₹150", "Pasta - ₹200", "Fries - ₹100",
                "Sandwich - ₹120", "Noodles - ₹180", "Cold Coffee - ₹150", "Ice Cream - ₹80"
        });
        itemMenu.setFont(mainFont);
        formPanel.add(itemMenu, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(createLabel("Quantity:"), gbc);
        gbc.gridx = 1;
        quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 50, 1));
        quantitySpinner.setFont(mainFont);
        formPanel.add(quantitySpinner, gbc);

        add(formPanel, BorderLayout.CENTER);

        // Button
        JPanel footerPanel = new JPanel();
footerPanel.setBackground(new Color(236, 240, 241)); // light grey
JButton billButton = new JButton("GENERATE BILL");
billButton.setBackground(Color.BLACK);
billButton.setForeground(Color.WHITE);
billButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
billButton.setPreferredSize(new Dimension(200, 45));
billButton.setFocusPainted(false);
billButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
billButton.setOpaque(true);
billButton.addActionListener(e -> generateTextBill());
        footerPanel.add(billButton);
        add(footerPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    // UI Helpers
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
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        return field;
    }

    // Validation Helpers (Review-2)
    private boolean isValidMobile(String mobile) {
        return mobile.matches("\\d{10}");
    }

    private boolean isValidName(String name) {
        return name.matches("[a-zA-Z ]{2,}");
    }

    private double calculateSubtotal(int price, int quantity) {
        return price * quantity;
    }

    // Generates receipt with validation, discount, GST and file handling (Enhanced in Review-2)
    public void generateTextBill() {
        String name = nameField.getText().trim();
        String mobile = mobileField.getText().trim();
        String admissionNo = admissionField.getText().trim();

        if (name.isEmpty() || mobile.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All required fields must be filled.");
            return;
        }

        if (!isValidName(name)) {
            JOptionPane.showMessageDialog(this, "Enter a valid name.");
            return;
        }

        if (!isValidMobile(mobile)) {
            JOptionPane.showMessageDialog(this, "Enter a valid 10-digit mobile number.");
            return;
        }

        int index = itemMenu.getSelectedIndex();
        int price = prices[index];
        int quantity = (int) quantitySpinner.getValue();

        double subtotal = calculateSubtotal(price, quantity);
        double discount = admissionNo.isEmpty() ? 0 : subtotal * 0.10;
        double taxable = subtotal - discount;
        double gst = taxable * 0.05;
        double total = taxable + gst;

        String fileName = "Bill_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".txt";

        try (PrintWriter writer = new PrintWriter(fileName)) {
            writer.println("=========== BISTRO RECEIPT ===========");
            writer.println("Customer : " + name);
            writer.println("Mobile   : " + mobile);
            if (!admissionNo.isEmpty()) writer.println("Student ID: " + admissionNo);
            writer.println("-------------------------------------");
            writer.println("Item      : " + items[index]);
            writer.println("Quantity  : " + quantity);
            writer.println("Subtotal  : ₹" + subtotal);
            writer.println("Discount  : ₹" + discount);
            writer.println("GST (5%)  : ₹" + gst);
            writer.println("-------------------------------------");
            writer.println("TOTAL     : ₹" + total);
            writer.println("=====================================");
            Desktop.getDesktop().open(new File(fileName));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "File generation failed.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(RestaurantBilling::new);
    }
}
