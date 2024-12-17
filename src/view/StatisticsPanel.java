package view;

import Model.Fee;
import service.FeeService;
import service.StudentService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class StatisticsPanel extends JPanel{
    private StudentService studentService;

    private JTable roomTable;
    private DefaultTableModel tableModel;
    private JButton refreshButton;

    public StatisticsPanel() {
        studentService = new StudentService();
        initComponents();
        loadRoomStatistics();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // Header
        JLabel titleLabel = new JLabel("Room Statistics", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(titleLabel, BorderLayout.NORTH);

        // Room Statistics Table
        tableModel = new DefaultTableModel(new String[]{"Room ID", "Student Count", "Total Beds"}, 0);
        roomTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(roomTable);

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Room and Student Statistics"));
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        add(tablePanel, BorderLayout.CENTER);

        refreshButton = new JButton("Refresh");
        refreshButton.setFont(new Font("Arial", Font.PLAIN, 14));
        refreshButton.addActionListener(e -> loadRoomStatistics());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(refreshButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadRoomStatistics() {
        List<StudentService.RoomStatistics> roomStatisticsList = studentService.getRoomStatistics();

        // Xóa dữ liệu cũ trong bảng
        tableModel.setRowCount(0);

        // Thêm dữ liệu mới
        for (StudentService.RoomStatistics stats : roomStatisticsList) {
            tableModel.addRow(new Object[]{
                    stats.getRoomId(),
                    stats.getStudentCount(),
                    stats.getTotalBeds()
            });
        }
    }
}
