package com.quanlysinhvien.ui;

import com.quanlysinhvien.dao.StudentDao;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

// Màn hình quản lý sinh viên.
public class StudentForm extends AbstractCrudPanel {
    public StudentForm() {
        super("Sinh viên",
                new String[]{"Mã SV", "Họ tên", "Ngày sinh", "Giới tính", "Email", "Mã lớp"},
                new String[]{"Mã SV", "Họ tên", "Ngày sinh (yyyy-MM-dd)", "Giới tính", "Email", "Mã lớp"},
                new int[]{0}, new boolean[]{true, true, false, false, false, false}, new StudentDao());
    }

    @Override
    protected void validateSpecific(String[] values) {
        if (values[0].length() > 20) {
            throw new IllegalArgumentException("Mã sinh viên không được dài hơn 20 ký tự.");
        }
        if (values[1].length() > 100) {
            throw new IllegalArgumentException("Họ tên không được dài hơn 100 ký tự.");
        }
        if (values[3].length() > 10) {
            throw new IllegalArgumentException("Giới tính không được dài hơn 10 ký tự.");
        }
        if (values[4].length() > 100) {
            throw new IllegalArgumentException("Email không được dài hơn 100 ký tự.");
        }
        if (!values[4].isBlank() && !values[4].matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new IllegalArgumentException("Email không đúng định dạng.");
        }
        if (values[5].length() > 20) {
            throw new IllegalArgumentException("Mã lớp không được dài hơn 20 ký tự.");
        }
        if (!values[2].isBlank()) {
            try {
                LocalDate.parse(values[2]);
            } catch (DateTimeParseException exception) {
                throw new IllegalArgumentException("Ngày sinh phải có định dạng yyyy-MM-dd, ví dụ 2005-08-20.");
            }
        }
    }
}
