package com.quanlysinhvien.dao;

import com.quanlysinhvien.config.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

// Các câu lệnh SQL cho bảng LopHoc.
public class ClassDao implements CrudRepository {
    @Override
    public List<Object[]> search(String keyword) throws SQLException {
        List<Object[]> rows = new ArrayList<>();
        String value = keyword == null ? "" : keyword.trim();
        String sql = "SELECT MaLop, TenLop, NienKhoa FROM dbo.LopHoc "
                + "WHERE ?=N'' OR MaLop LIKE ? OR TenLop LIKE ? OR ISNULL(NienKhoa,N'') LIKE ? ORDER BY MaLop";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, value);
            statement.setString(2, "%" + value + "%");
            statement.setString(3, "%" + value + "%");
            statement.setString(4, "%" + value + "%");
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) rows.add(new Object[]{result.getString("MaLop"),
                        result.getString("TenLop"), result.getString("NienKhoa")});
            }
        }
        return rows;
    }

    @Override
    public void insert(String[] values) throws SQLException {
        String sql = "INSERT INTO dbo.LopHoc(MaLop, TenLop, NienKhoa) VALUES (?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, values[0].trim());
            statement.setString(2, values[1].trim());
            setNullable(statement, 3, values[2]);
            statement.executeUpdate();
        }
    }

    @Override
    public void update(String[] values, String[] originalKeys) throws SQLException {
        String sql = "UPDATE dbo.LopHoc SET MaLop=?, TenLop=?, NienKhoa=? WHERE MaLop=?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, values[0].trim());
            statement.setString(2, values[1].trim());
            setNullable(statement, 3, values[2]);
            statement.setString(4, originalKeys[0]);
            requireUpdated(statement.executeUpdate());
        }
    }

    @Override
    public void delete(String[] keys) throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM dbo.LopHoc WHERE MaLop=?")) {
            statement.setString(1, keys[0]);
            requireUpdated(statement.executeUpdate());
        }
    }

    private static void setNullable(PreparedStatement statement, int index, String value) throws SQLException {
        if (value == null || value.isBlank()) statement.setNull(index, java.sql.Types.NVARCHAR);
        else statement.setString(index, value.trim());
    }

    private static void requireUpdated(int count) throws SQLException {
        if (count == 0) throw new SQLException("Không tìm thấy bản ghi cần cập nhật hoặc xóa.");
    }
}
