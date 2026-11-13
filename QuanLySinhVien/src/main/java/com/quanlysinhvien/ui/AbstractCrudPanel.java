package com.quanlysinhvien.ui;

import com.quanlysinhvien.dao.CrudRepository;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.sql.SQLException;
import java.util.List;

// Giao diện dùng chung cho các chức năng thêm, sửa, xóa và tìm kiếm.
public abstract class AbstractCrudPanel extends JPanel {
    private final CrudRepository repository;
    private final String entityName;
    private final String[] columnNames;
    private final String[] fieldLabels;
    private final int[] keyColumnIndexes;
    private final boolean[] requiredFields;
    private final JTextField[] inputs;
    private final JTextField searchInput = new JTextField(22);
    private final DefaultTableModel tableModel;
    private final JTable table;
    private String[] originalKeys;

    protected AbstractCrudPanel(String entityName, String[] columnNames, String[] fieldLabels,
                                int[] keyColumnIndexes, boolean[] requiredFields,
                                CrudRepository repository) {
        this.entityName = entityName;
        this.columnNames = columnNames;
        this.fieldLabels = fieldLabels;
        this.keyColumnIndexes = keyColumnIndexes;
        this.requiredFields = requiredFields;
        this.repository = repository;
        this.inputs = new JTextField[fieldLabels.length];

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));

        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        add(buildHeader(), BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(buildEditor(), BorderLayout.SOUTH);

        table.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting() && table.getSelectedRow() >= 0) {
                loadSelectedRow();
            }
        });
        refreshTable(false);
    }

    private JPanel buildHeader() {
        JPanel panel = new JPanel(new BorderLayout(12, 8));
        JLabel title = new JLabel("QUẢN LÝ " + entityName.toUpperCase());
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(new Color(35, 65, 105));
        panel.add(title, BorderLayout.WEST);

        JPanel searchPanel = new JPanel();
        searchInput.setToolTipText("Nhập từ khóa cần tìm");
        JButton searchButton = new JButton("Tìm kiếm");
        JButton reloadButton = new JButton("Làm mới");
        searchButton.addActionListener(event -> refreshTable());
        searchInput.addActionListener(event -> refreshTable());
        reloadButton.addActionListener(event -> {
            searchInput.setText("");
            refreshTable();
            clearForm();
        });
        searchPanel.add(new JLabel("Từ khóa:"));
        searchPanel.add(searchInput);
        searchPanel.add(searchButton);
        searchPanel.add(reloadButton);
        panel.add(searchPanel, BorderLayout.EAST);
        return panel;
    }

    private JPanel buildEditor() {
        JPanel wrapper = new JPanel(new BorderLayout(8, 8));
        wrapper.setBorder(BorderFactory.createTitledBorder("Thông tin " + entityName.toLowerCase()));

        JPanel fieldsPanel = new JPanel(new GridLayout(0, 4, 8, 7));
        for (int i = 0; i < fieldLabels.length; i++) {
            JLabel label = new JLabel(fieldLabels[i] + (requiredFields[i] ? " *" : ""));
            label.setHorizontalAlignment(SwingConstants.RIGHT);
            inputs[i] = new JTextField();
            inputs[i].setFont(new Font("Segoe UI", Font.PLAIN, 12));
            fieldsPanel.add(label);
            fieldsPanel.add(inputs[i]);
        }
        wrapper.add(fieldsPanel, BorderLayout.CENTER);

        JPanel buttons = new JPanel();
        JButton addButton = new JButton("Thêm");
        JButton updateButton = new JButton("Sửa");
        JButton deleteButton = new JButton("Xóa");
        JButton clearButton = new JButton("Xóa trắng");
        addButton.addActionListener(event -> insertRecord());
        updateButton.addActionListener(event -> updateRecord());
        deleteButton.addActionListener(event -> deleteRecord());
        clearButton.addActionListener(event -> clearForm());
        buttons.add(addButton);
        buttons.add(updateButton);
        buttons.add(deleteButton);
        buttons.add(clearButton);
        wrapper.add(buttons, BorderLayout.SOUTH);
        return wrapper;
    }

    private void loadSelectedRow() {
        int row = table.convertRowIndexToModel(table.getSelectedRow());
        String[] values = new String[columnNames.length];
        for (int i = 0; i < columnNames.length; i++) {
            Object value = tableModel.getValueAt(row, i);
            values[i] = value == null ? "" : value.toString();
            inputs[i].setText(values[i]);
        }
        originalKeys = new String[keyColumnIndexes.length];
        for (int i = 0; i < keyColumnIndexes.length; i++) {
            originalKeys[i] = values[keyColumnIndexes[i]];
        }
    }

    private String[] readValues() {
        String[] values = new String[inputs.length];
        for (int i = 0; i < inputs.length; i++) values[i] = inputs[i].getText().trim();
        return values;
    }

    private void validateValues(String[] values) {
        for (int i = 0; i < requiredFields.length; i++) {
            if (requiredFields[i] && values[i].isBlank()) {
                throw new IllegalArgumentException("Vui lòng nhập " + fieldLabels[i] + ".");
            }
        }
        validateSpecific(values);
    }

    // Mỗi màn hình có thể kiểm tra thêm dữ liệu riêng.
    protected void validateSpecific(String[] values) {
        // Mặc định không có kiểm tra bổ sung.
    }

    private void insertRecord() {
        String[] values = readValues();
        try {
            validateValues(values);
            repository.insert(values);
            showSuccess("Thêm " + entityName.toLowerCase() + " thành công.");
            refreshTable();
            clearForm();
        } catch (IllegalArgumentException exception) {
            showError(exception.getMessage());
        } catch (SQLException exception) {
            showError(explainDatabaseError(exception));
        }
    }

    private void updateRecord() {
        if (originalKeys == null) {
            showError("Vui lòng chọn một dòng trong bảng trước khi sửa.");
            return;
        }
        String[] values = readValues();
        try {
            validateValues(values);
            repository.update(values, originalKeys.clone());
            showSuccess("Cập nhật " + entityName.toLowerCase() + " thành công.");
            refreshTable();
            clearForm();
        } catch (IllegalArgumentException exception) {
            showError(exception.getMessage());
        } catch (SQLException exception) {
            showError(explainDatabaseError(exception));
        }
    }

    private void deleteRecord() {
        if (originalKeys == null) {
            showError("Vui lòng chọn một dòng trong bảng trước khi xóa.");
            return;
        }
        int answer = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc chắn muốn xóa " + entityName.toLowerCase() + " đã chọn?",
                "Xác nhận xóa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (answer != JOptionPane.YES_OPTION) return;

        try {
            repository.delete(originalKeys.clone());
            showSuccess("Đã xóa " + entityName.toLowerCase() + ".");
            refreshTable();
            clearForm();
        } catch (SQLException exception) {
            showError(explainDatabaseError(exception));
        }
    }

    protected final void refreshTable() {
        refreshTable(true);
    }

    private void refreshTable(boolean showError) {
        try {
            List<Object[]> rows = repository.search(searchInput.getText());
            tableModel.setRowCount(0);
            for (Object[] row : rows) tableModel.addRow(row);
        } catch (SQLException exception) {
            if (showError) showError(explainDatabaseError(exception));
        }
    }

    private void clearForm() {
        for (JTextField input : inputs) input.setText("");
        originalKeys = null;
        table.clearSelection();
    }

    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message == null ? "Có lỗi xảy ra." : message,
                "Lỗi", JOptionPane.ERROR_MESSAGE);
    }

    private String explainDatabaseError(SQLException exception) {
        String message = exception.getMessage();
        if (message != null && (message.contains("config.properties") || message.contains("db.host"))) {
            return message;
        }
        if (message != null && (message.contains("Violation of PRIMARY KEY")
                || message.contains("duplicate key") || message.contains("UNIQUE KEY"))) {
            return "Mã đã tồn tại. Vui lòng kiểm tra lại mã khóa chính.";
        }
        if (message != null && (message.contains("REFERENCE constraint") || message.contains("FOREIGN KEY"))) {
            return "Không thể xóa hoặc cập nhật vì dữ liệu đang được bảng khác tham chiếu. "
                    + "Hãy xử lý dữ liệu liên quan trước.";
        }
        if (message != null && (message.contains("CHECK constraint") || message.contains("CHECK"))) {
            return "Dữ liệu không hợp lệ. Số tín chỉ phải lớn hơn 0 và điểm phải nằm trong khoảng 0 đến 10.";
        }
        return "Không thao tác được với cơ sở dữ liệu. Kiểm tra SQL Server, config.properties và dữ liệu nhập.\nChi tiết: "
                + (message == null ? "Không có thông tin lỗi." : message);
    }
}
