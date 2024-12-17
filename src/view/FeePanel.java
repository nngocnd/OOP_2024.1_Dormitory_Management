package view;

import Model.Fee;
import Model.FeeType;
import Model.Student;
import service.FeeService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class FeePanel extends JPanel {
    private JTable feeTable; // Bảng hiển thị thông tin phí
    private JScrollPane scrollPane;
    private JButton addFeeButton, editFeeButton, deleteFeeButton, searchFeeButton, refreshButton;
    private FeeService feeService;
    private DefaultTableModel tableModel;
    private JTextField feeIdTextField;
    private JTextField feeTypeTextField;
    private JTextField studentIdTextField;

    public FeePanel() {
        setLayout(new BorderLayout());
        feeService = new FeeService();

        // Tạo bảng hiển thị thông tin phí
        tableModel = new DefaultTableModel(new Object[]{"ID", "Type", "Amount", "Payment Date", "Student ID", "Is Paid"}, 0);
        feeTable = new JTable(tableModel);
        scrollPane = new JScrollPane(feeTable);

        // Tạo các nút chức năng
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        addFeeButton = new JButton("Add");
        editFeeButton = new JButton("Update");
        deleteFeeButton = new JButton("Delete");
        searchFeeButton = new JButton("Search");
        refreshButton = new JButton("Refresh");

        feeIdTextField = new JTextField(10);
        feeTypeTextField = new JTextField(10);
        studentIdTextField = new JTextField(10);

        buttonPanel.add(addFeeButton);
        buttonPanel.add(editFeeButton);
        buttonPanel.add(deleteFeeButton);
        buttonPanel.add(refreshButton);

        // Thêm các thành phần vào panel
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new FlowLayout());

        // Thêm các thành phần vào searchPanel
        searchPanel.add(new JLabel("Fee ID:"));
        searchPanel.add(feeIdTextField);
        searchPanel.add(new JLabel("Fee Type:"));
        searchPanel.add(feeTypeTextField);
        searchPanel.add(new JLabel("Student ID:"));
        searchPanel.add(studentIdTextField);
        searchPanel.add(searchFeeButton);

        add(searchPanel, BorderLayout.NORTH);

        addFeeButton.addActionListener(e -> addFee());
        editFeeButton.addActionListener(e -> onUpdateFee());
        deleteFeeButton.addActionListener(e -> onDeleteFee());
        searchFeeButton.addActionListener(e -> onSearchFee());
        refreshButton.addActionListener(e -> loadFeeData());

        loadFeeData();
    }

    // Getter cho các nút
    public JButton getAddFeeButton() {
        return addFeeButton;
    }

    public JButton getEditFeeButton() {
        return editFeeButton;
    }

    public JButton getDeleteFeeButton() {
        return deleteFeeButton;
    }

    public JTable getFeeTable() {
        return feeTable;
    }

    private Fee showFeeDialog(Fee fee) {
        // Tạo JComboBox để chọn FeeType
        JComboBox<FeeType> feeTypeComboBox = new JComboBox<>(FeeType.values());
        if (fee != null && fee.getFeeType() != null) {
            feeTypeComboBox.setSelectedItem(fee.getFeeType());
        }

        JTextField feeAmountField = new JTextField(fee != null ? String.valueOf(fee.getFeeAmount()) : "", 20);
        JTextField paymentDateField = new JTextField(fee != null ? fee.getPaymentDate() : "", 20);
        JTextField studentIdField = new JTextField(fee != null && fee.getStudent() != null ? String.valueOf(fee.getStudent().getStudentId()) : "", 20);
        JCheckBox isPaidCheckBox = new JCheckBox("Is Paid", fee != null && fee.getIs_paid() == 1);

        JPanel panel = new JPanel(new GridLayout(0, 2));
        panel.add(new JLabel("Fee Type:"));
        panel.add(feeTypeComboBox);
        panel.add(new JLabel("Fee Amount:"));
        panel.add(feeAmountField);
        panel.add(new JLabel("Payment Date:"));
        panel.add(paymentDateField);
        panel.add(new JLabel("Student ID:"));
        panel.add(studentIdField);
        panel.add(new JLabel("Is Paid:"));
        panel.add(isPaidCheckBox);

        int result = JOptionPane.showConfirmDialog(null, panel, "Fee Information", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                Fee newFee = new Fee();
                if (fee != null){
                    newFee.setFeeId(fee.getFeeId());
                }
                newFee.setFeeType((FeeType) feeTypeComboBox.getSelectedItem());
                newFee.setFeeAmount(Double.parseDouble(feeAmountField.getText()));
                newFee.setPaymentDate(paymentDateField.getText());

                Student student = new Student();
                student.setStudentId(Integer.parseInt(studentIdField.getText()));
                newFee.setStudent(student);

                newFee.setIs_paid(isPaidCheckBox.isSelected() ? 1 : 0);
                return newFee;
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid input! Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        return null;
    }


    private void loadFeeData() {
        tableModel.setRowCount(0); // Xóa dữ liệu cũ
        List<Fee> feeList = feeService.getAllFees();
        for (Fee fee : feeList) {
            tableModel.addRow(new Object[]{
                    fee.getFeeId(),
                    fee.getFeeType(),
                    fee.getFeeAmount(),
                    fee.getPaymentDate(),
                    fee.getStudent().getStudentId(),
                    fee.getIs_paid() == 1 ? "Yes" : "No"
            });
        }
    }

    private void addFee() {
        Fee fee = showFeeDialog(null); // Hiển thị dialog nhập liệu
        if (fee != null) {
            try {
                // Gọi service để thêm fee vào cơ sở dữ liệu
                boolean isAdded = feeService.addFee(fee);

                // Thông báo kết quả
                if (isAdded) {
                    JOptionPane.showMessageDialog(this, "Fee added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadFeeData();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to add fee. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "An error occurred: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Adding fee was canceled or invalid data entered.", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void onUpdateFee() {
        int selectedRow = feeTable.getSelectedRow();
        if (selectedRow != -1) {
            int feeId = (int) feeTable.getValueAt(selectedRow, 0);
            Fee feeToEdit = new Fee(feeId);
            System.out.println("feeid: " + feeToEdit.getFeeId());
            Fee editedFee = showFeeDialog(feeToEdit);
            System.out.println("print: " + editedFee.getFeeType() + " - " + editedFee.getPaymentDate());
            if (editedFee != null) {
                try {
                    // Gọi service để thêm fee vào cơ sở dữ liệu
                    boolean isUpdated = feeService.updateFee(editedFee);
                    System.out.println("bool: editedFee DI: " + editedFee.getFeeId());

                    // Thông báo kết quả
                    if (isUpdated) {
                        JOptionPane.showMessageDialog(this, "Fee added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        loadFeeData();
                    } else {
                        JOptionPane.showMessageDialog(this, "Failed to add fee. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "An error occurred: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Update fee was canceled or invalid data entered.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn phòng để sửa!", "Lỗi", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void onDeleteFee() {
        int selectedRow = feeTable.getSelectedRow();  // Lấy dòng đã chọn trong bảng
        if (selectedRow != -1) {  // Kiểm tra nếu người dùng đã chọn một dòng
            int feeId = (int) feeTable.getValueAt(selectedRow, 0);  // Lấy Fee ID từ cột đầu tiên trong bảng
            System.out.println("Selected Fee ID for deletion: " + feeId);

            // Hiển thị hộp thoại xác nhận trước khi xóa
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete this fee?",
                    "Confirm Deletion", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {  // Nếu người dùng chọn "Yes"
                try {
                    // Gọi service để xóa Fee từ cơ sở dữ liệu
                    boolean isDeleted = feeService.deleteFee(feeId);

                    // Thông báo kết quả
                    if (isDeleted) {
                        JOptionPane.showMessageDialog(this,
                                "Fee deleted successfully!",
                                "Success", JOptionPane.INFORMATION_MESSAGE);
                        loadFeeData();  // Làm mới bảng dữ liệu
                    } else {
                        JOptionPane.showMessageDialog(this,
                                "Failed to delete fee. Please try again.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this,
                            "An error occurred: " + e.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                System.out.println("Deletion canceled by user.");  // Nếu người dùng chọn "No"
            }
        } else {
            // Thông báo khi không chọn dòng nào trong bảng
            JOptionPane.showMessageDialog(this,
                    "Please select a fee to delete!",
                    "Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void onSearchFee() {
        // Lấy giá trị tìm kiếm từ các trường nhập liệu
        String feeTypeInput = feeTypeTextField.getText().trim();  // Lấy Fee Type
        String feeIdInput = feeIdTextField.getText().trim();  // Lấy Fee ID
        String studentIdInput = studentIdTextField.getText().trim();  // Lấy Student ID

        // Tạo một biến chứa danh sách kết quả tìm kiếm
        List<Fee> searchResults = new ArrayList<>();

        // Kiểm tra nếu người dùng nhập thông tin tìm kiếm
        if (!feeIdInput.isEmpty() || !feeTypeInput.isEmpty() || !studentIdInput.isEmpty()) {
            // Gọi service để tìm kiếm dựa trên các điều kiện
            try {
                searchResults = feeService.searchFees(feeIdInput, feeTypeInput, studentIdInput);

                // Nếu không tìm thấy kết quả, thông báo cho người dùng
                if (searchResults.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "No matching fees found.", "Search Result", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "An error occurred while searching: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please enter at least one search parameter.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Cập nhật bảng với kết quả tìm kiếm
        updateFeeTable(searchResults);
    }

    private void updateFeeTable(List<Fee> fees) {
        // Xóa hết dữ liệu cũ trong bảng
        DefaultTableModel model = (DefaultTableModel) feeTable.getModel();
        model.setRowCount(0);  // Xóa tất cả các hàng hiện tại

        // Thêm các hàng mới từ danh sách fees
        for (Fee fee : fees) {
            model.addRow(new Object[] {
                    fee.getFeeId(),
                    fee.getFeeType(),
                    fee.getFeeAmount(),
                    fee.getPaymentDate(),
                    fee.getStudent().getStudentId(),
                    fee.getIs_paid()
            });
        }
    }

}
