package com.quanlysinhvien.dao;

import com.quanlysinhvien.config.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

// Các câu lệnh SQL cho bảng BangDiem.
public class GradeDao implements CrudRepository {
    @Override
    public List<Object[]> search(String keyword) throws SQLException {
        List<Object[]> rows = new ArrayList<>();
        String value = keyword == null ? "" : keyword.trim();
        String sql = "SELECT MaSV, MaMon, DiemChuyenCan, DiemKiemTra, DiemThi FROM dbo.BangDiem "
                + "WHERE ?=N'' OR MaSV LIKE ? OR MaMon LIKE ? ORDER BY MaSV, MaMon";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, value);
            statement.setString(2, "%" + value + "%");
            statement.setString(3, "%" + value + "%");
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) rows.add(new Object[]{result.getString("MaSV"), result.getString("MaMon"),
                        result.getObject("DiemChuyenCan"), result.getObject("DiemKiemTra"), result.getObject("DiemThi")});
            }
        }
        return rows;
    }

    @Override
    public void insert(String[] values) throws SQLException {
        String sql = "INSERT INTO dbo.BangDiem(MaSV, MaMon, DiemChuyenCan, DiemKiemTra, DiemThi) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, values[0].trim());
            statement.setString(2, values[1].trim());
            setScore(statement, 3, values[2]);
            setScore(statement, 4, values[3]);
            setScore(statement, 5, values[4]);
            statement.executeUpdate();
        }
    }

    @Override
    public void update(String[] values, String[] originalKeys) throws SQLException {
        String sql = "UPDATE dbo.BangDiem SET MaSV=?, MaMon=?, DiemChuyenCan=?, DiemKiemTra=?, DiemThi=? "
                + "WHERE MaSV=? AND MaMon=?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, values[0].trim());
            statement.setString(2, values[1].trim());
            setScore(statement, 3, values[2]);
            setScore(statement, 4, values[3]);
            setScore(statement, 5, values[4]);
            statement.setString(6, originalKeys[0]);
            statement.setString(7, originalKeys[1]);
            requireUpdated(statement.executeUpdate());
        }
    }

    @Override
    public void delete(String[] keys) throws SQLException {
        String sql = "DELETE FROM dbo.BangDiem WHERE MaSV=? AND MaMon=?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, keys[0]);
            statement.setString(2, keys[1]);
            requireUpdated(statement.executeUpdate());
        }
    }

    private static void setScore(PreparedStatement statement, int index, String value) throws SQLException {
        if (value == null || value.isBlank()) {
            statement.setNull(index, java.sql.Types.FLOAT);
            return;
        }
        try {
            double score = Double.parseDouble(value.trim());
            if (!Double.isFinite(score) || score < 0 || score > 10) {
                throw new NumberFormatException("outside range");
            }
            statement.setDouble(index, score);
        } catch (NumberFormatException exception) {
            throw new SQLException("Điểm phải là số từ 0 đến 10. Có thể để trống nếu chưa có điểm.", exception);
        }
    }

    private static void requireUpdated(int count) throws SQLException {
        if (count == 0) throw new SQLException("Không tìm thấy bản ghi cần cập nhật hoặc xóa.");
    }
}
