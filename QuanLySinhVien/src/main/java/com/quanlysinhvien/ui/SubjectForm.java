package com.quanlysinhvien.ui;

import com.quanlysinhvien.dao.SubjectDao;

// Màn hình quản lý môn học.
public class SubjectForm extends AbstractCrudPanel {
    public SubjectForm() {
        super("Môn học",
                new String[]{"Mã môn", "Tên môn", "Số tín chỉ"},
                new String[]{"Mã môn", "Tên môn", "Số tín chỉ"},
                new int[]{0}, new boolean[]{true, true, true}, new SubjectDao());
    }

    @Override
    protected void validateSpecific(String[] values) {
        if (values[0].length() > 20) {
            throw new IllegalArgumentException("Mã môn không được dài hơn 20 ký tự.");
        }
        if (values[1].length() > 100) {
            throw new IllegalArgumentException("Tên môn không được dài hơn 100 ký tự.");
        }
        try {
            if (Integer.parseInt(values[2]) <= 0) {
                throw new IllegalArgumentException("Số tín chỉ phải lớn hơn 0.");
            }
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("Số tín chỉ phải là số nguyên dương.");
        }
    }
}
