package service;

import DAO.StudentDAO;
import Model.Fee;
import Model.Student;
import connectionDB.DBConnection;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentService {
    private final StudentDAO studentDAO;

    public StudentService() {
        studentDAO = new StudentDAO();
    }

    public boolean addStudent(Student student){
        if (student == null || student.getFullName().isEmpty() || student.getEmail().isEmpty()){
            return false;
        }
        return studentDAO.addStudent(student);
    }

    public boolean updateStudent(Student student) {
        if (student == null || student.getStudentId() <= 0) {
            return false;  // Kiểm tra tính hợp lệ của thông tin sinh viên
        }
        return studentDAO.updateStudent(student);  // Cập nhật thông tin sinh viên trong cơ sở dữ liệu
    }

    public Student getStudentById(int studentId) {
        if (studentId <= 0) {
            return null;  // Kiểm tra ID hợp lệ
        }
        return studentDAO.getStudentById(studentId);  // Lấy thông tin sinh viên từ cơ sở dữ liệu
    }

    public List<Student> searchStudentsByName(String fullName) {
        if (fullName == null || fullName.isEmpty()) {
            return null;  // Kiểm tra tên hợp lệ
        }
        return studentDAO.searchStudentsByName(fullName);  // Tìm kiếm sinh viên theo tên
    }

    public List<Student> getAllStudents() {
        return studentDAO.getAllStudents();  // Lấy tất cả sinh viên từ cơ sở dữ liệu
    }

    public boolean deleteStudent(int studentId) {
        if (studentId <= 0) {
            return false;  // Kiểm tra ID hợp lệ
        }
        return studentDAO.deleteStudent(studentId);
    }

//    public boolean payFee(int studentId, Fee fee) {
//        Student student = studentDAO.getStudentById(studentId);
//        if (student != null) {
//            fee.setStudent(student);  // Gán sinh viên cho phí
//            return contractService.payFee(fee);  // Thanh toán phí
//        }
//        return false;
//    }
    public boolean addStudentWithContract(Student student) {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection(); // Kết nối đến DB
            conn.setAutoCommit(false); // Bắt đầu transaction

            // 1. Thêm sinh viên
            String insertStudentSQL = "INSERT INTO students (full_name, date_of_birth, gender, address, email, room_id) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement studentStmt = conn.prepareStatement(insertStudentSQL, Statement.RETURN_GENERATED_KEYS);
            studentStmt.setString(1, student.getFullName());
            studentStmt.setString(2, student.getDateOfBirth());
            studentStmt.setString(3, student.getGender());
            studentStmt.setString(4, student.getAddress());
            studentStmt.setString(5, student.getEmail());
            studentStmt.setInt(6, student.getRoom().getRoomId());
            studentStmt.executeUpdate();

            // Lấy student_id vừa được tạo
            ResultSet studentRS = studentStmt.getGeneratedKeys();
            int studentId = -1;
            if (studentRS.next()) {
                studentId = studentRS.getInt(1);
            }

            // 2. Thêm hợp đồng
            String insertContractSQL = "INSERT INTO contracts (student_id, room_id, start_date, end_date, payment_method, price) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement contractStmt = conn.prepareStatement(insertContractSQL, Statement.RETURN_GENERATED_KEYS);
            contractStmt.setInt(1, studentId);
            contractStmt.setInt(2, student.getRoom().getRoomId());
            contractStmt.setDate(3, java.sql.Date.valueOf("2024-01-01")); // start_date
            contractStmt.setDate(4, java.sql.Date.valueOf("2024-12-31")); // end_date
            contractStmt.setString(5, "Cash"); // payment_method
            contractStmt.setDouble(6, 5000); // price
            contractStmt.executeUpdate();

            // Lấy contract_id vừa được tạo
            ResultSet contractRS = contractStmt.getGeneratedKeys();
            int contractId = -1;
            if (contractRS.next()) {
                contractId = contractRS.getInt(1);
            }

            // 3. Cập nhật contract_id vào bảng students
            String updateStudentSQL = "UPDATE students SET contract_id = ? WHERE student_id = ?";
            PreparedStatement updateStmt = conn.prepareStatement(updateStudentSQL);
            updateStmt.setInt(1, contractId);
            updateStmt.setInt(2, studentId);
            updateStmt.executeUpdate();

            // Commit transaction
            conn.commit();
            JOptionPane.showMessageDialog(null, "Thêm sinh viên và hợp đồng thành công!");
            return true;
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Hủy transaction nếu lỗi xảy ra
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true); // Khôi phục chế độ auto-commit
                    conn.close();
                } catch (SQLException closeEx) {
                    closeEx.printStackTrace();
                }
            }
        }
    }

    public Map<String, Integer> getStudentCountByRoom() {
        Map<String, Integer> roomStatistics = new HashMap<>();
        String sql = "SELECT room_id, COUNT(*) AS student_count FROM students GROUP BY room_id";

        try (PreparedStatement statement = studentDAO.connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                String roomId = resultSet.getString("room_id");
                int studentCount = resultSet.getInt("student_count");
                roomStatistics.put(roomId, studentCount);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return roomStatistics;
    }

    public static class RoomStatistics {
        private String roomId;
        private int studentCount;
        private int totalBeds;

        public RoomStatistics(String roomId, int studentCount, int totalBeds) {
            this.roomId = roomId;
            this.studentCount = studentCount;
            this.totalBeds = totalBeds;
        }

        public String getRoomId() {
            return roomId;
        }

        public int getStudentCount() {
            return studentCount;
        }

        public int getTotalBeds() {
            return totalBeds;
        }
    }

    // Phương thức thống kê sinh viên và tổng giường trong từng phòng
    public List<RoomStatistics> getRoomStatistics() {
        List<RoomStatistics> roomStatisticsList = new ArrayList<>();
        String sql = "SELECT r.room_id, COUNT(s.student_id) AS student_count, r.number_of_beds " +
                "FROM rooms r " +
                "LEFT JOIN students s ON r.room_id = s.room_id " +
                "GROUP BY r.room_id, r.number_of_beds";

        try (PreparedStatement statement = studentDAO.connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                String roomId = resultSet.getString("room_id");
                int studentCount = resultSet.getInt("student_count");
                int totalBeds = resultSet.getInt("number_of_beds");

                roomStatisticsList.add(new RoomStatistics(roomId, studentCount, totalBeds));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return roomStatisticsList;
    }
}
