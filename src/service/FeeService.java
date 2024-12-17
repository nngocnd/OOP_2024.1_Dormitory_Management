package service;

import DAO.FeeDAO;
import Model.Fee;
import Model.FeeType;
import Model.Student;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FeeService {
    private final FeeDAO feeDAO;

    public FeeService() {
        this.feeDAO = new FeeDAO();
    }

    public boolean addFee(Fee fee) {
        return feeDAO.addNewFee(fee);
    }

    public List<Fee> getAllFees() {
        return feeDAO.getAllFees();
    }

    // Cập nhật thông tin khoản phí
    public boolean updateFee(Fee fee) {
        return feeDAO.updateFee(fee);
    }

    // Xóa khoản phí theo ID
    public boolean deleteFee(int feeId) {
        return feeDAO.deleteFee(feeId);
    }

    // Tìm khoản phí theo ID
////  public Fee getFeeById(int feeId) {
//        return feeDAO.getFeeById(feeId);
//    }

    // Tìm kiếm khoản phí theo tên loại phí
    public List<Fee> searchFeesByName(String feeTypeName) {
        return feeDAO.searchByName(feeTypeName);
    }

    public List<Fee> searchFees(String feeId, String feeType, String studentId) {
        List<Fee> fees = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM fees WHERE 1=1");

        if (!feeId.isEmpty()) {
            sql.append(" AND fee_id = ?");
        }
        if (!feeType.isEmpty()) {
            sql.append(" AND fee_type = ?");
        }
        if (!studentId.isEmpty()) {
            sql.append(" AND student_id = ?");
        }

        try (PreparedStatement statement = feeDAO.connection.prepareStatement(sql.toString())) {
            int paramIndex = 1;

            // Gán giá trị cho các tham số tìm kiếm
            if (!feeId.isEmpty()) {
                statement.setString(paramIndex++, feeId);
            }
            if (!feeType.isEmpty()) {
                statement.setString(paramIndex++, feeType);
            }
            if (!studentId.isEmpty()) {
                statement.setString(paramIndex++, studentId);
            }

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Fee fee = new Fee();
                    fee.setFeeId(resultSet.getInt("fee_id"));
                    fee.setFeeType(FeeType.valueOf(resultSet.getString("fee_type")));
                    fee.setFeeAmount(resultSet.getDouble("amount"));
                    fee.setPaymentDate(resultSet.getString("payment_date"));

                    Student student = new Student();
                    student.setStudentId(resultSet.getInt("student_id"));
                    fee.setStudent(student);

                    fee.setIs_paid(resultSet.getInt("is_paid"));
                    fees.add(fee);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return fees;
    }

}
