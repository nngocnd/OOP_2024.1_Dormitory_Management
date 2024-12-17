package view;

import Model.ARoom;
import Model.Contract;
import Model.Room;
import service.ContractService;
import service.RoomService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ContractPanel extends JPanel {
    private JTable contractTable; // Bảng hiển thị thông tin hợp đồng
    private JScrollPane scrollPane;
    private JButton addContractButton, editContractButton, deleteContractButton;
    private JButton loadContractButton;
    ContractService contractService;

    public ContractPanel() {
        setLayout(new BorderLayout());
        this.contractService = new ContractService();

        // Tạo bảng hiển thị thông tin hợp đồng
        String[] columnNames = {"Mã hợp đồng", "Mã sinh viên", "Mã phòng", "Ngày bắt đầu", "Ngày kết thúc", "Tiền phí"};
        DefaultTableModel model = new DefaultTableModel(new Object[][]{}, columnNames); // Sử dụng DefaultTableModel
        contractTable = new JTable(model);
        scrollPane = new JScrollPane(contractTable);

        // Tạo các nút
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
//        addContractButton = new JButton("Thêm hợp đồng");
//        editContractButton = new JButton("Sửa hợp đồng");
//        deleteContractButton = new JButton("Xóa hợp đồng");
        loadContractButton = new JButton("Refresh");

//        buttonPanel.add(addContractButton);
//        buttonPanel.add(editContractButton);
//        buttonPanel.add(deleteContractButton);
        buttonPanel.add(loadContractButton);

        // Thêm các thành phần vào panel
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        //addContractButton.addActionListener(e -> onAddContract());
        loadContractButton.addActionListener(e -> loadContractTable());

        loadContractTable();
    }

    public JTable getContractTable() {
        return contractTable;
    }

    public void loadContractTable() {
        // Khởi tạo RoomService để lấy danh sách phòng
        ContractService contractService = new ContractService();
        List<Contract> contracts = contractService.getAllContracts();

        // Lấy mô hình bảng để thêm dữ liệu
        DefaultTableModel model = (DefaultTableModel) contractTable.getModel();
        model.setRowCount(0); // Xóa dữ liệu cũ, nếu có

        // Lặp qua danh sách phòng và thêm từng phòng vào bảng
        for (Contract contract : contracts) {
            model.addRow(new Object[]{
                    contract.getContractId(),
                    contract.getStudent().getStudentId(),
                    contract.getRoom().getRoomId(),
                    contract.getStartDate(),
                    contract.getEndDate(),
                    contract.getRoomPrice()
            });
        }
    }

    public Contract showContractDialog(Contract contract) {
        JTextField studentIdField = new JTextField();
        JTextField roomIdField = new JTextField();
        JTextField startDateField = new JTextField();
        JTextField endDateField = new JTextField();
        JTextField priceField = new JTextField();

        // Nếu hợp đồng đã tồn tại, điền thông tin vào các trường
        if (contract != null) {
            studentIdField.setText(String.valueOf(contract.getStudent().getStudentId()));
            roomIdField.setText(String.valueOf(contract.getRoom().getRoomId()));
            startDateField.setText(contract.getStartDate().toString());
            endDateField.setText(contract.getEndDate().toString());
            priceField.setText(String.valueOf(contract.getRoomPrice()));
        }

        // Tạo panel hiển thị thông tin hợp đồng
        JPanel panel = new JPanel(new GridLayout(5, 2));
        panel.add(new JLabel("Student ID: "));
        panel.add(studentIdField);
        panel.add(new JLabel("Room ID: "));
        panel.add(roomIdField);
        panel.add(new JLabel("Start Date: "));
        panel.add(startDateField);
        panel.add(new JLabel("End Date: "));
        panel.add(endDateField);
        panel.add(new JLabel("Room Price: "));
        panel.add(priceField);

        // Hiển thị hộp thoại
        int result = JOptionPane.showConfirmDialog(this, panel, "Contract Information", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                // Tạo đối tượng Contract mới và gán các giá trị từ form
                Contract newContract = new Contract();
                if (contract != null) {
                    newContract.setContractId(contract.getContractId());
                }
                newContract.setStudent(new Model.Student(Integer.parseInt(studentIdField.getText())));
                newContract.setRoom(new Model.Room(Integer.parseInt(roomIdField.getText())));
                newContract.setStartDate(String.valueOf(java.sql.Date.valueOf(startDateField.getText())));
                newContract.setEndDate(String.valueOf(java.sql.Date.valueOf(endDateField.getText())));
                newContract.setRoomPrice(Double.parseDouble(priceField.getText()));
                return newContract;
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(this, "Invalid input! Please check the data format.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        return null;
    }

    private void onAddContract() {
        // Hiển thị hộp thoại nhập thông tin hợp đồng mới
        Contract newContract = showContractDialog(null);

        if (newContract != null) {
            // Gọi phương thức thêm hợp đồng
            boolean isAdded = contractService.addNewContract(newContract);
            if (isAdded) {
                JOptionPane.showMessageDialog(this, "Thêm hợp đồng thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                loadContractTable(); // Tải lại bảng hiển thị hợp đồng
            } else {
                JOptionPane.showMessageDialog(this, "Thêm hợp đồng thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

}
