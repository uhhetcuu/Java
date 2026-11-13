package com.quanlysinhvien.dao;

import com.quanlysinhvien.config.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

// Các câu lệnh SQL cho bảng MonHoc.
public class SubjectDao implements CrudRepository {
    @Override
    public List<Object[]> search(String keyword) throws SQLException {
        List<Object[]> rows = new ArrayList<>();
        String value = keyword == null ? "" : keyword.trim();
        String sql = "SELECT MaMon, TenMon, SoTinChi FROM dbo.MonHoc "
                + "WHERE ?=N'' OR MaMon LIKE ? OR TenMon LIKE ? ORDER BY MaMon";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, value);
            statement.setString(2, "%" + value + "%");
            statement.setString(3, "%" + value + "%");
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) rows.add(new Object[]{result.getString("MaMon"),
                        result.getString("TenMon"), result.getObject("SoTinChi")});
            }
        }
        return rows;
    }

    @Override
    public void insert(String[] values) throws SQLException {
        String sql = "INSERT INTO dbo.MonHoc(MaMon, TenMon, SoTinChi) VALUES (?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, values[0].trim());
            statement.setString(2, values[1].trim());
            statement.setInt(3, Integer.parseInt(values[2].trim()));
            statement.executeUpdate();
        } catch (NumberFormatException exception) {
            throw new SQLException("Số tín chỉ phải là số nguyên dương.", exception);
        }
    }

    @Override
    public void update(String[] values, String[] originalKeys) throws SQLException {
        String sql = "UPDATE dbo.MonHoc SET MaMon=?, TenMon=?, SoTinChi=? WHERE MaMon=?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, values[0].trim());
            statement.setString(2, values[1].trim());
            statement.setInt(3, Integer.parseInt(values[2].trim()));
            statement.setString(4, originalKeys[0]);
            requireUpdated(statement.executeUpdate());
        } catch (NumberFormatException exception) {
            throw new SQLException("Số tín chỉ phải là số nguyên dương.", exception);
        }
    }

    @Override
    public void delete(String[] keys) throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM dbo.MonHoc WHERE MaMon=?")) {
            statement.setString(1, keys[0]);
            requireUpdated(statement.executeUpdate());
        }
    }

    private static void requireUpdated(int count) throws SQLException {
        if (count == 0) throw new SQLException("Không tìm thấy bản ghi cần cập nhật hoặc xóa.");
    }
}
