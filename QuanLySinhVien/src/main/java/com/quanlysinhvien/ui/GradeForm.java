package com.quanlysinhvien.ui;

import com.quanlysinhvien.dao.GradeDao;

// Màn hình quản lý bảng điểm.
public class GradeForm extends AbstractCrudPanel {
    public GradeForm() {
        super("Bảng điểm",
                new String[]{"Mã SV", "Mã môn", "Điểm chuyên cần", "Điểm kiểm tra", "Điểm thi"},
                new String[]{"Mã SV", "Mã môn", "Điểm chuyên cần", "Điểm kiểm tra", "Điểm thi"},
                new int[]{0, 1}, new boolean[]{true, true, false, false, false}, new GradeDao());
    }

    @Override
    protected void validateSpecific(String[] values) {
        if (values[0].length() > 20 || values[1].length() > 20) {
            throw new IllegalArgumentException("Mã sinh viên và mã môn không được dài hơn 20 ký tự.");
        }
        for (int i = 2; i < values.length; i++) {
            if (values[i].isBlank()) continue;
            try {
                double score = Double.parseDouble(values[i]);
                if (!Double.isFinite(score) || score < 0 || score > 10) {
                    throw new IllegalArgumentException("Các cột điểm phải nằm trong khoảng từ 0 đến 10.");
                }
            } catch (NumberFormatException exception) {
                throw new IllegalArgumentException("Điểm phải là số từ 0 đến 10. Có thể để trống nếu chưa có điểm.");
            }
        }
    }
}
