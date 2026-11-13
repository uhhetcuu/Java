package com.quanlysinhvien;

import com.quanlysinhvien.config.DatabaseConnection;
import com.quanlysinhvien.ui.MainFrame;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.sql.SQLException;

// Lớp chạy chương trình.
public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
                // Dùng giao diện Swing mặc định nếu không tải được giao diện của hệ thống.
            }

            try {
                DatabaseConnection.testConnection();
            } catch (SQLException | IllegalStateException exception) {
                JOptionPane.showMessageDialog(null,
                        "Không kết nối được SQL Server.\n"
                                + exception.getMessage()
                                + "\nKiểm tra SQL Server và file config.properties.",
                        "Lỗi kết nối", JOptionPane.ERROR_MESSAGE);
                return;
            }

            new MainFrame().setVisible(true);
        });
    }
}
