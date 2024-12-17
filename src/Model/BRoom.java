package Model;

public class BRoom extends Room{

    public BRoom(int roomId){
        super(roomId);
    }
    public BRoom(String roomNumber, String roomType, int numberOfBeds, boolean isOccupied, double roomPrice) {
        super(roomNumber, roomType, numberOfBeds, isOccupied, roomPrice);
    }
    public BRoom(int roomId, String roomNumber, String roomType, int numberOfBeds, boolean isOccupied, double roomPrice) {
        super(roomId, roomNumber, roomType, numberOfBeds, isOccupied, roomPrice);
    }
}
