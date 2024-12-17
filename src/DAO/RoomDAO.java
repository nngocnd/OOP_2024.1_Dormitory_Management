package DAO;

import Model.Contract;
import Model.Room;
import Model.Student;
import connectionDB.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomDAO implements Search{
    private static Connection connection;

    public RoomDAO() {
        connection = DBConnection.getConnection();
    }


    public boolean addRoom(Room room){
        String query = "INSERT INTO rooms (room_id, room_number, room_type, number_of_beds, is_occupied, price_per_month) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, room.getRoomId());
            stmt.setString(2, room.getRoomNumber());
            stmt.setString(3, room.getRoomType());
            stmt.setInt(4, room.getNumberOfBeds());
            stmt.setInt(5, 0);
            stmt.setDouble(6, room.getRoomPrice());
            int result = stmt.executeUpdate();
            return result > 0;  // Trả về true nếu thêm thành công
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Room getRoomByRoom_Number(String roomNumber){
        Room room = null;
        String sql = "SELECT * FROM rooms WHERE room_number = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, roomNumber);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    room = new Room(
                            rs.getInt("room_id"),          // Mã phòng
                            rs.getString("room_number"),     // Tên phòng
                            rs.getString("room_type"),
                            rs.getInt("number_of_beds"),         // Sức chứ
                            rs.getBoolean("is_occupied"),// Giá phòng
                            rs.getDouble("price_per_month")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return room;
    }

    public List<Room> getAllRooms() {
        String query = "SELECT * FROM rooms";
        List<Room> rooms = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Room room = new Room();
                room.setRoomId(rs.getInt("room_id"));
                room.setRoomNumber(rs.getString("room_number"));
                room.setRoomType(rs.getString("room_type"));
                room.setNumberOfBeds(rs.getInt("number_of_beds"));
                room.setRoomPrice(rs.getDouble("price_per_month"));
                room.setOccupied(rs.getBoolean("is_occupied"));
                rooms.add(room);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rooms;
    }

    public void updateRoomStatus(int roomId, boolean isOccupied) throws SQLException {
        String sql = "UPDATE rooms SET is_occupied = ? WHERE room_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setBoolean(1, isOccupied);
            stmt.setInt(2, roomId);
            stmt.executeUpdate();
        }
    }

    @Override
    public List<Room> searchByName(String name) {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM rooms WHERE room_number LIKE ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "%" + name + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Room room = new Room(
                            rs.getInt("room_id"),
                            rs.getString("room_number"),
                            rs.getString("room_type"),
                            rs.getInt("number_of_beds"),
                            rs.getBoolean("is_occupied"),
                            rs.getDouble("price_per_month")
                    );
                    rooms.add(room);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rooms;
    }

    public boolean updateRoom(Room room) {
        String query = "UPDATE rooms SET room_number = ?, room_type = ?, number_of_beds = ?, price_per_month = ?, is_occupied = ? WHERE room_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, room.getRoomNumber());
            stmt.setString(2, room.getRoomType());
            stmt.setInt(3, room.getNumberOfBeds());
            stmt.setDouble(4, room.getRoomPrice());
            stmt.setBoolean(5, room.getOccupied());
            stmt.setInt(6, room.getRoomId());  // Giả sử sinh viên đã có phòng
            int result = stmt.executeUpdate();
            return result > 0;  // Trả về true nếu cập nhật thành công
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteRoom(int roomId) {
        String deleteSQL = "DELETE FROM rooms WHERE room_id = ?"; // Câu lệnh SQL xóa phòng

        try (PreparedStatement stmt = connection.prepareStatement(deleteSQL)) {
            stmt.setInt(1, roomId); // Gán roomId vào câu lệnh SQL

            int rowsAffected = stmt.executeUpdate(); // Thực thi câu lệnh SQL
            return rowsAffected > 0; // Trả về true nếu xóa thành công (số dòng bị ảnh hưởng > 0)
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Trả về false nếu có lỗi
        }
    }
}