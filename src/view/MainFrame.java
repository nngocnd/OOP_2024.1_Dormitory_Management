package view;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class MainFrame extends JFrame {
    private JTabbedPane tabbedPane; // Tab chính
    private JMenuBar menuBar; // Thanh menu
    private JMenu fileMenu, helpMenu;
    private JMenuItem exitMenuItem, aboutMenuItem;

    // Các panel quản lý
    private StudentPanel studentPanel;
    private RoomPanel roomPanel;
    private ContractPanel contractPanel;
    private FeePanel feePanel;
    private StatisticsPanel statisticsPanel;

    public MainFrame() {
        // Cấu hình cơ bản của JFrame
        setTitle("Hệ thống quản lý ký túc xá");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);

        studentPanel = new StudentPanel();
        roomPanel = new RoomPanel();
        contractPanel = new ContractPanel();
        feePanel = new FeePanel();
        statisticsPanel = new StatisticsPanel();

        // Tạo JTabbedPane và thêm các tab
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Quản lý sinh viên", studentPanel);
        tabbedPane.addTab("Quản lý phòng", roomPanel);
        tabbedPane.addTab("Quản lý hợp đồng", contractPanel);
        tabbedPane.addTab("Quản lý phí", feePanel);
        tabbedPane.addTab("Thống kê", statisticsPanel);

        // Thêm tabbedPane vào giao diện chính
        add(tabbedPane, BorderLayout.CENTER);

        // Tạo thanh menu
        menuBar = new JMenuBar();
        fileMenu = new JMenu("File");
        helpMenu = new JMenu("Help");

        // Thêm các item vào menu File
        exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(e -> System.exit(0)); // Thoát ứng dụng
        fileMenu.add(exitMenuItem);

        // Thêm các item vào menu Help
        aboutMenuItem = new JMenuItem("About");
        aboutMenuItem.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Phần mềm Quản lý ký túc xá\nVersion 1.0",
                "About", JOptionPane.INFORMATION_MESSAGE));
        helpMenu.add(aboutMenuItem);

        menuBar.add(fileMenu);
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);
    }

    public StudentPanel getStudentPanel() {
        return studentPanel;
    }

    public RoomPanel getRoomPanel() {
        return roomPanel;
    }

    public ContractPanel getContractPanel() {
        return contractPanel;
    }

    public FeePanel getFeePanel() {
        return feePanel;
    }

    public StatisticsPanel getStatisticsPanel() {
        return statisticsPanel;
    }
}
