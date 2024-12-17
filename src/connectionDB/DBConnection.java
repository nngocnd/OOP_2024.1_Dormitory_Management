package connectionDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/dormitory_management"; // Địa chỉ database
    private static final String USER = "root"; // Tên đăng nhập MySQL
    private static final String PASSWORD = ""; // Mật khẩu MySQL

    public static Connection getConnection() {
        Connection connection = null;
        try {
            // Kết nối tới cơ sở dữ liệu
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Kết nối thành công!");
        } catch (SQLException e) {
            System.err.println("Kết nối thất bại: " + e.getMessage());
        }
        return connection;
    }
}
