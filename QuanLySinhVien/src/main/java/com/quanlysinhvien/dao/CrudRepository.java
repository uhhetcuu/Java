package com.quanlysinhvien.dao;

import java.sql.SQLException;
import java.util.List;

// Các hàm thêm, sửa, xóa và tìm kiếm dùng chung.
public interface CrudRepository {
    List<Object[]> search(String keyword) throws SQLException;

    void insert(String[] values) throws SQLException;

    void update(String[] values, String[] originalKeys) throws SQLException;

    void delete(String[] keys) throws SQLException;
}
