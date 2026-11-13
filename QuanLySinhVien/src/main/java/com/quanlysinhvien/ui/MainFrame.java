package com.quanlysinhvien.ui;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Font;

// Cửa sổ chính của chương trình.
public class MainFrame extends JFrame {
    public MainFrame() {
        super("PHẦN MỀM QUẢN LÝ SINH VIÊN");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1120, 720);
        setMinimumSize(new java.awt.Dimension(900, 600));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(0, 8));

        JLabel heading = new JLabel("PHẦN MỀM QUẢN LÝ SINH VIÊN", SwingConstants.CENTER);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 21));
        heading.setBorder(javax.swing.BorderFactory.createEmptyBorder(14, 8, 4, 8));
        add(heading, BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabs.addTab("Sinh viên", new StudentForm());
        tabs.addTab("Lớp học", new ClassForm());
        tabs.addTab("Môn học", new SubjectForm());
        tabs.addTab("Bảng điểm", new GradeForm());
        add(tabs, BorderLayout.CENTER);

        JLabel footer = new JLabel("Đồ án phần mềm Java - Cơ sở dữ liệu: QuanLySinhVienDB", SwingConstants.CENTER);
        footer.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 8, 10, 8));
        add(footer, BorderLayout.SOUTH);
    }
}
