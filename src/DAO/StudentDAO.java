package DAO;

import Model.Contract;
import Model.Room;
import Model.Student;
import connectionDB.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class StudentDAO implements Search{
    public static Connection connection;

    public StudentDAO() {
        connection = DBConnection.getConnection();
    }

    public boolean addStudent(Student student){
        String query = "INSERT INTO students (student_id, full_name, date_of_birth, gender, address, email, room_id, contract_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, student.getStudentId());
            stmt.setString(2, student.getFullName());
            stmt.setDate(3, Date.valueOf(student.getDateOfBirth()));
            stmt.setString(4, student.getGender());
            stmt.setString(5, student.getAddress());
            stmt.setString(6, student.getEmail());
            stmt.setInt(7, student.getRoom().getRoomId());  // Giả sử sinh viên đã có phòng
            stmt.setInt(8, student.getContract().getContractId());  // Giả sử sinh viên đã có hợp đồng
            int result = stmt.executeUpdate();
            return result > 0;  // Trả về true nếu thêm thành công
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateStudent(Student student) {
        String query = "UPDATE students SET full_name = ?, date_of_birth = ?, gender = ?, address = ?, email = ?, room_id = ?, contract_id = ? WHERE student_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, student.getFullName());
            stmt.setDate(2, Date.valueOf(student.getDateOfBirth()));
            stmt.setString(3, student.getGender());
            stmt.setString(4, student.getAddress());
            stmt.setString(5, student.getEmail());
            stmt.setInt(6, student.getRoom().getRoomId());  // Giả sử sinh viên đã có phòng
            stmt.setInt(7, student.getContract().getContractId());  // Giả sử sinh viên đã có hợp đồng
            stmt.setInt(8, student.getStudentId());
            int result = stmt.executeUpdate();
            return result > 0;  // Trả về true nếu cập nhật thành công
        } catch (SQLException e) {
            System.err.println("Error while updating student: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteStudent(int studentId) {
        String query = "DELETE FROM students WHERE student_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, studentId);
            int result = stmt.executeUpdate();
            return result > 0;  // Trả về true nếu xóa thành công
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Student getStudentById(int studentId) {
        String query = "SELECT * FROM students WHERE student_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, studentId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                // Chuyển đổi dữ liệu ResultSet thành đối tượng Student
                Student student = new Student();
                student.setStudentId(rs.getInt("student_id"));
                student.setFullName(rs.getString("full_name"));
                //
                String dobString = String.valueOf(rs.getDate("date_of_birth"));
                student.setDateOfBirth(String.valueOf(LocalDate.parse(dobString)));
                //
                student.setGender(rs.getString("gender"));
                student.setAddress(rs.getString("address"));
                student.setEmail(rs.getString("email"));
                // Giả sử bạn có các phương thức để lấy thông tin phòng và hợp đồng từ ID
                student.setRoom(new Room(rs.getInt("room_id")));
                student.setContract(new Contract(rs.getInt("contract_id")));
                return student;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;  // Trả về null nếu không tìm thấy sinh viên
    }

    public List<Student> searchStudentsByName(String fullName) {
        String query = "SELECT * FROM students WHERE full_name LIKE ?";
        List<Student> students = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, "%" + fullName + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                // Chuyển đổi dữ liệu ResultSet thành đối tượng Student và thêm vào danh sách
                Student student = new Student();
                student.setStudentId(rs.getInt("student_id"));
                student.setFullName(rs.getString("full_name"));
                student.setDateOfBirth(String.valueOf(rs.getDate("date_of_birth").toLocalDate()));
                student.setGender(rs.getString("gender"));
                student.setAddress(rs.getString("address"));
                student.setEmail(rs.getString("email"));
                student.setRoom(new Room(rs.getInt("room_id")));
                student.setContract(new Contract(rs.getInt("contract_id")));
                students.add(student);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }

    public List<Student> getAllStudents() {
        String query = "SELECT * FROM students";
        List<Student> students = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Student student = new Student();
                student.setStudentId(rs.getInt("student_id"));
                student.setFullName(rs.getString("full_name"));
                student.setDateOfBirth(String.valueOf(rs.getDate("date_of_birth").toLocalDate()));
                student.setGender(rs.getString("gender"));
                student.setAddress(rs.getString("address"));
                student.setEmail(rs.getString("email"));
                student.setRoom(new Room(rs.getInt("room_id")));
                student.setContract(new Contract(rs.getInt("contract_id")));
                students.add(student);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }

    @Override
    public List<Student> searchByName(String name) {
        return searchStudentsByName(name);
    }
}
