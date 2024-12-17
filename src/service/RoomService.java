package service;

import DAO.RoomDAO;
import Model.Room;

import java.sql.SQLException;
import java.util.List;

public class RoomService {
    private final RoomDAO roomDAO;

    public RoomService() {
        this.roomDAO = new RoomDAO();
    }

    public List<Room> getAllRooms() {
        return roomDAO.getAllRooms();
    }

    public Room getRoomByRoomNumber(String roomNumber) {
        return roomDAO.getRoomByRoom_Number(roomNumber);
    }

    public List<Room> searchRoomsByName(String name) {
        return roomDAO.searchByName(name);
    }


    public void updateRoomStatus(int roomId, boolean isOccupied) throws SQLException {
        roomDAO.updateRoomStatus(roomId, isOccupied);
    }


    public boolean addRoom(Room newRoom) {
        return roomDAO.addRoom(newRoom);
    }

    public boolean updateRoom(Room updatedRoom) {
        return roomDAO.updateRoom(updatedRoom);
    }

    public boolean deleteRoom(int roomId) {
        return roomDAO.deleteRoom(roomId);
    }
}
