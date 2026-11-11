package org.example;

import com.formdev.flatlaf.FlatDarkLaf;
import org.example.model.Expense;
import org.example.service.ExpenseService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class GuiMain extends JFrame {
    private final ExpenseService service = new ExpenseService();
    private final DefaultTableModel tableModel;
    private final JTable table;
    private final JLabel totalSpentLabel = new JLabel("Total Spent: €0.00");

    public GuiMain() {
        setTitle("Personal Expense Analyzer");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Expense Tracker");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        totalSpentLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        totalSpentLabel.setForeground(new Color(0, 200, 120));

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(totalSpentLabel, BorderLayout.EAST);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        String[] columns = {"#", "Amount", "Category", "Date", "Description"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setRowHeight(30);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        table.getColumnModel().getColumn(0).setHeaderRenderer(new ImageHeaderRenderer("spy.png"));
        table.getColumnModel().getColumn(1).setHeaderRenderer(new ImageHeaderRenderer("panic.png"));
        table.getColumnModel().getColumn(2).setHeaderRenderer(new ImageHeaderRenderer("food.png"));
        table.getColumnModel().getColumn(3).setHeaderRenderer(new ImageHeaderRenderer("sleep.png"));
        table.getColumnModel().getColumn(4).setHeaderRenderer(new ImageHeaderRenderer("desc.png"));

        JScrollPane scrollPane = new JScrollPane(table);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        JButton addButton = new JButton("Add");
        JButton editButton = new JButton("Edit");
        JButton deleteButton = new JButton("Delete");

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        addButton.addActionListener(e -> showAddDialog());
        editButton.addActionListener(e -> showEditDialog());
        deleteButton.addActionListener(e -> deleteSelectedExpense());

        refreshTable();
    }

    private static class ImageHeaderRenderer extends JLabel implements javax.swing.table.TableCellRenderer {
        private final String imageName;

        public ImageHeaderRenderer(String imageName) {
            this.imageName = imageName;
            setHorizontalAlignment(CENTER);
            setVerticalAlignment(CENTER);
            setHorizontalTextPosition(CENTER);
            setVerticalTextPosition(BOTTOM);
            setFont(new Font("SansSerif", Font.BOLD, 12));
            setForeground(Color.LIGHT_GRAY);
            setBorder(UIManager.getBorder("TableHeader.cellBorder"));
            setOpaque(true);
            setBackground(new Color(60, 63, 65));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText(value != null ? value.toString() : "");

            var imgUrl = getClass().getResource("/images/" + imageName);
            if (imgUrl != null) {
                ImageIcon icon = new ImageIcon(imgUrl);
                Image scaled = icon.getImage().getScaledInstance(48, 48, Image.SCALE_SMOOTH);
                setIcon(new ImageIcon(scaled));
            }
            return this;
        }
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        var list = service.getAllExpenses();
        BigDecimal total = BigDecimal.ZERO;

        for (int i = 0; i < list.size(); i++) {
            Expense exp = list.get(i);
            tableModel.addRow(new Object[]{
                    i + 1,
                    exp.getAmount() != null ? "€" + exp.getAmount() : "€0.00",
                    exp.getCategory(),
                    exp.getDate(),
                    exp.getDescription()
            });
            if (exp.getAmount() != null) {
                total = total.add(exp.getAmount());
            }
        }

        totalSpentLabel.setText("Total Spent: €" + total);
    }

    private void showAddDialog() {
        JTextField amountField = new JTextField();
        JTextField categoryField = new JTextField();
        JTextField descField = new JTextField();

        Object[] fields = {
                "Amount (€):", amountField,
                "Category (e.g. food):", categoryField,
                "Description:", descField
        };

        int result = JOptionPane.showConfirmDialog(this, fields, "Add New Expense", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                // Автоматическая замена запятой на точку для BigDecimal
                String amountText = amountField.getText().trim().replace(",", ".");
                BigDecimal amount = new BigDecimal(amountText);

                String category = categoryField.getText().trim();
                String desc = descField.getText().trim();

                Expense newExp = new Expense(UUID.randomUUID().toString(), amount, category, LocalDate.now(), desc);
                service.addExpense(newExp);
                refreshTable();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid amount format! Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showEditDialog() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a row to edit!");
            return;
        }

        Expense old = service.getAllExpenses().get(selectedRow);

        JTextField amountField = new JTextField(old.getAmount() != null ? old.getAmount().toString() : "");
        JTextField categoryField = new JTextField(old.getCategory());
        JTextField descField = new JTextField(old.getDescription());

        Object[] fields = {
                "Amount (€):", amountField,
                "Category:", categoryField,
                "Description:", descField
        };

        int result = JOptionPane.showConfirmDialog(this, fields, "Edit Expense", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                String amountText = amountField.getText().trim().replace(",", ".");
                BigDecimal amount = new BigDecimal(amountText);

                String category = categoryField.getText().trim();
                String desc = descField.getText().trim();

                Expense updated = new Expense(old.getId(), amount, category, old.getDate(), desc);
                service.updateExpense(selectedRow, updated);
                refreshTable();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input data!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteSelectedExpense() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            int confirm = JOptionPane.showConfirmDialog(this, "Delete this expense?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                service.deleteExpense(selectedRow);
                refreshTable();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a row to delete!");
        }
    }

    public static void main(String[] args) {
        FlatDarkLaf.setup();
        SwingUtilities.invokeLater(() -> {
            new GuiMain().setVisible(true);
        });
    }
}