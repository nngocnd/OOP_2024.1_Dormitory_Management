package view;

import Model.ARoom;
import Model.BRoom;
import Model.Room;
import Model.Student;
import service.RoomService;
import service.StudentService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class RoomPanel extends JPanel {
    private JTable roomTable; // Bảng hiển thị thông tin phòng
    private JScrollPane scrollPane;
    private JButton addRoomButton, editRoomButton, deleteRoomButton;
    private RoomService roomService;
    public RoomPanel() {
        setLayout(new BorderLayout());
        roomService = new RoomService();

        // Tạo bảng hiển thị thông tin phòng
        String[] columnNames = {"Mã phòng", "Số phòng", "Loại phòng", "Số lượng tối đa"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0); // Tạo DefaultTableModel với tiêu đề cột
        roomTable = new JTable(model); // Sử dụng DefaultTableModel khi khởi tạo JTable
        scrollPane = new JScrollPane(roomTable);

        // Tạo các nút
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        addRoomButton = new JButton("Thêm phòng");
        editRoomButton = new JButton("Sửa phòng");
        deleteRoomButton = new JButton("Xóa phòng");

        buttonPanel.add(addRoomButton);
        buttonPanel.add(editRoomButton);
        buttonPanel.add(deleteRoomButton);

        // Thêm các thành phần vào panel
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        addRoomButton.addActionListener(e -> handleAddRoom());
        editRoomButton.addActionListener(e -> handleEditRoom());
        deleteRoomButton.addActionListener(e -> handleDeleteRoom());

        loadRoomTable();
    }

    // Getter cho các nút
    public JButton getAddRoomButton() {
        return addRoomButton;
    }

    public JButton getEditRoomButton() {
        return editRoomButton;
    }

    public JButton getDeleteRoomButton() {
        return deleteRoomButton;
    }

    public JTable getRoomTable() {
        return roomTable;
    }

    public void loadRoomTable() {
        // Khởi tạo RoomService để lấy danh sách phòng
        RoomService roomService = new RoomService();
        List<Room> roomList = roomService.getAllRooms();

        // Lấy mô hình bảng để thêm dữ liệu
        DefaultTableModel model = (DefaultTableModel) roomTable.getModel();
        model.setRowCount(0); // Xóa dữ liệu cũ, nếu có

        // Lặp qua danh sách phòng và thêm từng phòng vào bảng
        for (Room room : roomList) {
            if (room instanceof ARoom aRoom) {
                model.addRow(new Object[]{
                        room.getRoomId(),
                        room.getRoomNumber(),
                        room.getRoomType(),
                        room.getNumberOfBeds(),
                        room.getRoomPrice(),
                        aRoom.getAdditionalFee()
                });
            } else {
                model.addRow(new Object[]{
                        room.getRoomId(),
                        room.getRoomNumber(),
                        room.getRoomType(),
                        room.getNumberOfBeds(),
                        room.getRoomPrice(),
                        "-" // Không có phụ phí cho ARoom
                });
            }
        }
    }

    private void handleAddRoom() {
        // Hiển thị dialog để nhập thông tin phòng mới
        JTextField roomIdField = new JTextField();
        JTextField roomNumberField = new JTextField();
        JTextField roomTypeField = new JTextField();
        JTextField numberOfBedsField = new JTextField();
        JTextField priceField = new JTextField();
        JTextField additionalPriceField = new JTextField();

        Object[] message = {
                "Mã phòng:", roomIdField,
                "Số phòng:", roomNumberField,
                "Loại phòng (A/B):", roomTypeField,
                "Số giường:", numberOfBedsField,
                "Giá phòng:", priceField,
                "Phụ phí (Chỉ dành cho phòng loại A):", additionalPriceField,
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Thêm phòng", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            // Lấy dữ liệu từ các trường nhập
            int roomId = Integer.parseInt(roomIdField.getText());
            String roomNumber = roomNumberField.getText();
            String roomType = roomTypeField.getText();
            int numberOfBeds = Integer.parseInt(numberOfBedsField.getText());
            double roomPrice = Double.parseDouble(priceField.getText());
            double additionalPrice = additionalPriceField.getText().isEmpty() ? 0.0 : Double.parseDouble(additionalPriceField.getText());

            Room newRoom;
            // Tạo một đối tượng Room mới
            if (roomType.equals("A")){
                newRoom = new ARoom(roomId, roomNumber, roomType, numberOfBeds, false, roomPrice, additionalPrice);
            } else if (roomType.equals("B")){
                newRoom = new BRoom(roomId, roomNumber, roomType, numberOfBeds, false, roomPrice);
            } else {
                JOptionPane.showMessageDialog(this, "Loại phòng không hợp lệ (chỉ A hoặc B)", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }


            // Thêm phòng vào cơ sở dữ liệu
            boolean isAdded = roomService.addRoom(newRoom);
            if (isAdded) {
                // Nếu thêm thành công, cập nhật bảng hiển thị
                loadRoomTable();
                JOptionPane.showMessageDialog(this, "Phòng đã được thêm thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Không thể thêm phòng vào cơ sở dữ liệu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void handleEditRoom() {
        System.out.println("access edit");
        int selectedRow = roomTable.getSelectedRow();
        if (selectedRow != -1) {

            int roomId = (int) roomTable.getValueAt(selectedRow, 0);
            Room roomToEdit = new Room(roomId);

            Room editedRoom = showRoomDialog(roomToEdit);
            if (editedRoom != null) {
                // Nếu người dùng nhấn OK và sửa xong, cập nhật thông tin vào cơ sở dữ liệu
                boolean isUpdated = roomService.updateRoom(editedRoom);
                if (isUpdated) {
                    // Cập nhật lại bảng hiển thị
                    loadRoomTable();
                    JOptionPane.showMessageDialog(this, "Phòng đã được cập nhật!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Không thể cập nhật phòng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn phòng để sửa!", "Lỗi", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void handleDeleteRoom() {
        int selectedRow = roomTable.getSelectedRow();
        if (selectedRow != -1) {
            int roomId = (int) roomTable.getValueAt(selectedRow, 0);
            String roomNumber = (String) roomTable.getValueAt(selectedRow, 1);

            // Xác nhận xóa phòng
            int option = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa phòng " + roomNumber + "?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                // Xóa phòng khỏi cơ sở dữ liệu
                boolean isDeleted = roomService.deleteRoom(roomId);
                if (isDeleted) {
                    // Cập nhật lại bảng sau khi xóa
                    loadRoomTable();
                    JOptionPane.showMessageDialog(this, "Phòng đã được xóa thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Không thể xóa phòng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn phòng để xóa!", "Lỗi", JOptionPane.WARNING_MESSAGE);
        }
    }

    public Room showRoomDialog(Room room) {
        JTextField roomNumberField = new JTextField();
        JTextField roomTypeField = new JTextField();
        JTextField numberOfBedsField = new JTextField();
        JTextField priceField = new JTextField();
        JTextField additionalFeeField = new JTextField();

        if (room != null) {
            roomNumberField.setText(room.getRoomNumber());
            roomTypeField.setText(room.getRoomType());
            numberOfBedsField.setText(String.valueOf(room.getNumberOfBeds()));
            priceField.setText(String.valueOf(room.getRoomPrice()));
            if (room instanceof ARoom) {
                additionalFeeField.setText(String.valueOf(((ARoom) room).getAdditionalFee()));
            } else {
                additionalFeeField.setText(""); // Nếu không phải ARoom, không cần hiển thị phụ phí
            }
        }

        JPanel panel = new JPanel(new GridLayout(6, 2));
        panel.add(new JLabel("Room Number: "));
        panel.add(roomNumberField);
        panel.add(new JLabel("Room Type: "));
        panel.add(roomTypeField);
        panel.add(new JLabel("Number of Beds: "));
        panel.add(numberOfBedsField);
        panel.add(new JLabel("Room Price: "));
        panel.add(priceField);
        panel.add(new JLabel("Additional Fee (for Type A rooms): "));
        panel.add(additionalFeeField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Room Information", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            Room newRoom;
            String roomType = roomTypeField.getText();

            // Tạo đối tượng Room mới với thông tin nhập vào
            if (roomType.equals("A")) {
                newRoom = new ARoom(
                        room.getRoomId(),
                        roomNumberField.getText(),
                        roomType,
                        Integer.parseInt(numberOfBedsField.getText()),
                        false, // hoặc giá trị hợp lý khác
                        Double.parseDouble(priceField.getText()),
                        Double.parseDouble(additionalFeeField.getText())
                );
            } else if (roomType.equals("B")) {
                newRoom = new BRoom(
                        room.getRoomId(),
                        roomNumberField.getText(),
                        roomType,
                        Integer.parseInt(numberOfBedsField.getText()),
                        false, // hoặc giá trị hợp lý khác
                        Double.parseDouble(priceField.getText())
                );
            } else {
                JOptionPane.showMessageDialog(this, "Room type must be 'A' or 'B'.", "Invalid Room Type", JOptionPane.ERROR_MESSAGE);
                return null;
            }

            return newRoom;
        }

        return null;
    }

}
