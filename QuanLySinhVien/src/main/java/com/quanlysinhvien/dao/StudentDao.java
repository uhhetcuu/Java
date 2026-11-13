package com.quanlysinhvien.dao;

import com.quanlysinhvien.config.DatabaseConnection;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

// Các câu lệnh SQL cho bảng SinhVien.
public class StudentDao implements CrudRepository {
    private static final String SELECT_SQL = """
            SELECT MaSV, HoTen, CONVERT(VARCHAR(10), NgaySinh, 23) AS NgaySinh,
                   GioiTinh, Email, MaLop
            FROM dbo.SinhVien
            WHERE ? = N'' OR MaSV LIKE ? OR HoTen LIKE ? OR ISNULL(Email, N'') LIKE ? OR ISNULL(MaLop, N'') LIKE ?
            ORDER BY MaSV
            """;

    @Override
    public List<Object[]> search(String keyword) throws SQLException {
        List<Object[]> rows = new ArrayList<>();
        String value = keyword == null ? "" : keyword.trim();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_SQL)) {
            statement.setString(1, value);
            statement.setString(2, "%" + value + "%");
            statement.setString(3, "%" + value + "%");
            statement.setString(4, "%" + value + "%");
            statement.setString(5, "%" + value + "%");
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    rows.add(new Object[]{result.getString("MaSV"), result.getString("HoTen"),
                            result.getString("NgaySinh"), result.getString("GioiTinh"),
                            result.getString("Email"), result.getString("MaLop")});
                }
            }
        }
        return rows;
    }

    @Override
    public void insert(String[] values) throws SQLException {
        String sql = "INSERT INTO dbo.SinhVien(MaSV, HoTen, NgaySinh, GioiTinh, Email, MaLop) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, values[0].trim());
            statement.setString(2, values[1].trim());
            setDate(statement, 3, values[2]);
            setNullableString(statement, 4, values[3]);
            setNullableString(statement, 5, values[4]);
            setNullableString(statement, 6, values[5]);
            statement.executeUpdate();
        }
    }

    @Override
    public void update(String[] values, String[] originalKeys) throws SQLException {
        String sql = "UPDATE dbo.SinhVien SET MaSV=?, HoTen=?, NgaySinh=?, GioiTinh=?, Email=?, MaLop=? WHERE MaSV=?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, values[0].trim());
            statement.setString(2, values[1].trim());
            setDate(statement, 3, values[2]);
            setNullableString(statement, 4, values[3]);
            setNullableString(statement, 5, values[4]);
            setNullableString(statement, 6, values[5]);
            statement.setString(7, originalKeys[0]);
            requireUpdated(statement.executeUpdate());
        }
    }

    @Override
    public void delete(String[] keys) throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM dbo.SinhVien WHERE MaSV=?")) {
            statement.setString(1, keys[0]);
            requireUpdated(statement.executeUpdate());
        }
    }

    private static void setDate(PreparedStatement statement, int index, String value) throws SQLException {
        if (value == null || value.isBlank()) {
            statement.setNull(index, java.sql.Types.DATE);
        } else {
            try {
                statement.setDate(index, Date.valueOf(value.trim()));
            } catch (IllegalArgumentException exception) {
                throw new SQLException("Ngày sinh phải có định dạng yyyy-MM-dd.", exception);
            }
        }
    }

    private static void setNullableString(PreparedStatement statement, int index, String value) throws SQLException {
        if (value == null || value.isBlank()) statement.setNull(index, java.sql.Types.NVARCHAR);
        else statement.setString(index, value.trim());
    }

    private static void requireUpdated(int count) throws SQLException {
        if (count == 0) throw new SQLException("Không tìm thấy bản ghi cần cập nhật hoặc xóa.");
    }
}
