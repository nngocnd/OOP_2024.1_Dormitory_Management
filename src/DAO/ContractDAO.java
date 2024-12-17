package DAO;

import Model.Contract;
import Model.Room;
import Model.Student;
import connectionDB.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ContractDAO {
    public static Connection connection;

    public ContractDAO() {
        connection = DBConnection.getConnection();
    }

    public List<Contract> getAllContracts() throws SQLException {
        List<Contract> contracts = new ArrayList<>();
        String sql = "SELECT * FROM contracts";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Contract contract = new Contract(
                        rs.getInt("contract_id"),
                        rs.getString("start_date"),
                        rs.getString("end_date"),
                        rs.getDouble("price"),
                        rs.getString("payment_method"),
                        new Student(rs.getInt("student_id")),
                        new Room(rs.getInt("room_id"))
                );
                contracts.add(contract);
            }
        }
        return contracts;
    }

    public void createContract(Contract contract) throws SQLException {
        String sql = "INSERT INTO contracts (student_id, room_id, start_date, end_date, price, payment_method) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, contract.getStudent().getStudentId());
            stmt.setInt(2, contract.getRoom().getRoomId());
            stmt.setString(3, contract.getStartDate());
            stmt.setString(4, contract.getEndDate());
            stmt.setDouble(5, contract.getRoomPrice());
            stmt.setString(6, contract.getPaymentMethod());
            stmt.executeUpdate();
        }
    }
}
