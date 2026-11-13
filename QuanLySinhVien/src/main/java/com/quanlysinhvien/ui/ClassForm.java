package com.quanlysinhvien.ui;

import com.quanlysinhvien.dao.ClassDao;

// Màn hình quản lý lớp học.
public class ClassForm extends AbstractCrudPanel {
    public ClassForm() {
        super("Lớp học",
                new String[]{"Mã lớp", "Tên lớp", "Niên khóa"},
                new String[]{"Mã lớp", "Tên lớp", "Niên khóa"},
                new int[]{0}, new boolean[]{true, true, false}, new ClassDao());
    }

    @Override
    protected void validateSpecific(String[] values) {
        if (values[0].length() > 20) {
            throw new IllegalArgumentException("Mã lớp không được dài hơn 20 ký tự.");
        }
        if (values[1].length() > 100) {
            throw new IllegalArgumentException("Tên lớp không được dài hơn 100 ký tự.");
        }
        if (values[2].length() > 20) {
            throw new IllegalArgumentException("Niên khóa không được dài hơn 20 ký tự.");
        }
    }
}
