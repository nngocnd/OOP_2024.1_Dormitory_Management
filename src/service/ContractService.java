package service;

import DAO.ContractDAO;
import Model.Contract;

import javax.swing.*;
import java.sql.*;
import java.util.List;


public class ContractService {
    private ContractDAO contractDAO;

    public ContractService() {
        this.contractDAO = new ContractDAO();
    }

    public List<Contract> getAllContracts() {
        try {
            return contractDAO.getAllContracts();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error fetching contracts", e);
        }
    }

    public void createContract(Contract contract) {
        try {
            contractDAO.createContract(contract);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error creating contract", e);
        }
    }

    public boolean addNewContract(Contract contract) {
        String checkStudentQuery = "SELECT contract_id FROM students WHERE student_id = ?";
        String insertContractQuery = "INSERT INTO contracts (student_id, room_id, start_date, end_date, price) VALUES (?, ?, ?, ?, ?)";
        String updateStudentQuery = "UPDATE students SET contract_id = ? WHERE student_id = ?";

        try {
            // Kiểm tra xem sinh viên đã có hợp đồng hay chưa
            PreparedStatement checkStmt = ContractDAO.connection.prepareStatement(checkStudentQuery);
            checkStmt.setInt(1, contract.getStudent().getStudentId());
            ResultSet rs = checkStmt.executeQuery();

            // Nếu sinh viên đã có hợp đồng, thông báo lỗi
            if (rs.next() && rs.getInt("contract_id") != 0) {
                JOptionPane.showMessageDialog(null, "Sinh viên này đã có hợp đồng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            // Thêm hợp đồng mới vào bảng contracts
            PreparedStatement insertStmt = ContractDAO.connection.prepareStatement(insertContractQuery, Statement.RETURN_GENERATED_KEYS);
            insertStmt.setInt(1, contract.getStudent().getStudentId());
            insertStmt.setInt(2, contract.getRoom().getRoomId());
            insertStmt.setString(3, contract.getStartDate());
            insertStmt.setString(4, contract.getEndDate());
            insertStmt.setDouble(5, contract.getRoomPrice());

            int affectedRows = insertStmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Thêm hợp đồng thất bại, không có hàng nào bị thay đổi.");
            }

            // Lấy contract_id của hợp đồng vừa thêm
            ResultSet generatedKeys = insertStmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                int newContractId = generatedKeys.getInt(1);

                // Cập nhật contract_id vào bảng students
                PreparedStatement updateStmt = ContractDAO.connection.prepareStatement(updateStudentQuery);
                updateStmt.setInt(1, newContractId);
                updateStmt.setInt(2, contract.getStudent().getStudentId());
                int updatedRows = updateStmt.executeUpdate();

                if (updatedRows > 0) {
                    return true;  // Thêm hợp đồng mới thành công
                } else {
                    throw new SQLException("Cập nhật contract_id trong bảng students thất bại.");
                }
            } else {
                throw new SQLException("Không thể tạo contract_id cho hợp đồng.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        } finally {
            // Đảm bảo đóng kết nối
            try {
                if (ContractDAO.connection != null) {
                    ContractDAO.connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }



}
