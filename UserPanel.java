import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserPanel extends JPanel {
    private JTextField budgetField;
    private JTextField locationField;
    private JTextField guestCountField;

    private JTextArea resultArea;

    private ArrayList<WeddingPackage> packages;
    private ArrayList<Weight> weights;

    public UserPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(20, 20, 20)); // Black background

        // Sample data for demonstration
        packages = new ArrayList<>();
        packages.add(new WeddingPackage("Silver Package", 10000, "Jakarta", 100, 7));
        packages.add(new WeddingPackage("Gold Package", 20000, "Bandung", 150, 9));
        packages.add(new WeddingPackage("Platinum Package", 30000, "Jakarta", 200, 10));

        weights = new ArrayList<>();
        weights.add(new Weight("price", 0.4));
        weights.add(new Weight("location", 0.2));
        weights.add(new Weight("guestcount", 0.2));
        weights.add(new Weight("quality", 0.2));

        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        inputPanel.setBackground(new Color(20, 20, 20));

        JLabel budgetLabel = new JLabel("Budget:");
        JLabel locationLabel = new JLabel("Location:");
        JLabel guestCountLabel = new JLabel("Guest Count:");

        styleLabel(budgetLabel);
        styleLabel(locationLabel);
        styleLabel(guestCountLabel);

        budgetField = new JTextField();
        locationField = new JTextField();
        guestCountField = new JTextField();

        inputPanel.add(budgetLabel);
        inputPanel.add(budgetField);
        inputPanel.add(locationLabel);
        inputPanel.add(locationField);
        inputPanel.add(guestCountLabel);
        inputPanel.add(guestCountField);

        JButton calculateBtn = new JButton("Find Best Package");
        styleButton(calculateBtn);

        inputPanel.add(new JLabel());
        inputPanel.add(calculateBtn);

        add(inputPanel, BorderLayout.NORTH);

        resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setBackground(new Color(30, 30, 30));
        resultArea.setForeground(new Color(212, 175, 55));
        add(new JScrollPane(resultArea), BorderLayout.CENTER);

        calculateBtn.addActionListener(e -> calculateBestPackage());
    }

    private void styleLabel(JLabel label) {
        label.setForeground(new Color(212, 175, 55));
    }

    private void styleButton(JButton button) {
        button.setBackground(new Color(212, 175, 55));
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
    }

    private void calculateBestPackage() {
        try {
            double budget = Double.parseDouble(budgetField.getText());
            String location = locationField.getText();
            int guestCount = Integer.parseInt(guestCountField.getText());

            // Filter packages by budget and guest count
            ArrayList<WeddingPackage> filteredPackages = new ArrayList<>();
            for (WeddingPackage wp : packages) {
                if (wp.getPrice() <= budget && wp.getGuestCount() >= guestCount) {
                    filteredPackages.add(wp);
                }
            }

            if (filteredPackages.isEmpty()) {
                resultArea.setText("No packages match your criteria.");
                return;
            }

            Map<String, Object> userCriteria = new HashMap<>();
            userCriteria.put("location", location);

            WeddingPackage best = SAWCalculator.calculateBestPackage(filteredPackages, weights, userCriteria);

            if (best != null) {
                resultArea.setText("Best Package:\n");
                resultArea.append("Name: " + best.getName() + "\n");
                resultArea.append("Price: " + best.getPrice() + "\n");
                resultArea.append("Location: " + best.getLocation() + "\n");
                resultArea.append("Guest Count: " + best.getGuestCount() + "\n");
                resultArea.append("Quality: " + best.getQuality() + "\n");
            } else {
                resultArea.setText("No suitable package found.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numeric values for budget and guest count.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
