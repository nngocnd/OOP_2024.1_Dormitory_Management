package connectionDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class QueryExample {
    public static void main(String[] args) {
        String query = "SELECT * FROM Students";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                System.out.println("Mã SV: " + resultSet.getString("full_name"));
                System.out.println("Họ Tên: " + resultSet.getString("student_id"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
