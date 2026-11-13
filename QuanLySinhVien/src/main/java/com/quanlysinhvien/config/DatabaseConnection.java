package com.quanlysinhvien.config;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

// Đọc cấu hình và tạo kết nối đến SQL Server.
public final class DatabaseConnection {
    private static final Properties PROPERTIES = loadProperties();

    private DatabaseConnection() {
    }

    private static Properties loadProperties() {
        Properties properties = new Properties();
        Path file = Path.of("config.properties");

        if (!Files.exists(file)) {
            return properties;
        }

        try (InputStream input = Files.newInputStream(file)) {
            properties.load(input);
        } catch (IOException exception) {
            throw new IllegalStateException("Không đọc được file config.properties.", exception);
        }
        return properties;
    }

    public static Connection getConnection() throws SQLException {
        String host = required("db.host");
        String port = PROPERTIES.getProperty("db.port", "1433").trim();
        String database = required("db.name");
        String user = required("db.user");
        String password = PROPERTIES.getProperty("db.password", "");
        String encrypt = PROPERTIES.getProperty("db.encrypt", "true").trim();
        String trustCertificate = PROPERTIES.getProperty("db.trustServerCertificate", "true").trim();

        String url = "jdbc:sqlserver://" + host + ":" + port
                + ";databaseName=" + database
                + ";encrypt=" + encrypt
                + ";trustServerCertificate=" + trustCertificate
                + ";loginTimeout=10;";
        return DriverManager.getConnection(url, user, password);
    }

    private static String required(String key) throws SQLException {
        String value = PROPERTIES.getProperty(key);
        if (value == null || value.isBlank()) {
            throw new SQLException("Thiếu cấu hình '" + key + "'. Hãy tạo config.properties từ file mẫu.");
        }
        return value.trim();
    }

    public static void testConnection() throws SQLException {
        try (Connection connection = getConnection()) {
            // Mở rồi đóng kết nối để kiểm tra cấu hình.
        }
    }
}
