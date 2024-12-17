import DAO.RoomDAO;
import DAO.StudentDAO;
import Model.Contract;
import Model.Room;
import Model.Student;
import connectionDB.DBConnection;
import service.ContractService;
import view.MainFrame;

import javax.swing.*;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame();
            mainFrame.setVisible(true);
        });
    }
}