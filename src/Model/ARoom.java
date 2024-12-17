package Model;

public class ARoom extends Room{
    private double additionalFee;

    public ARoom(int roomId){
        super(roomId);
    }
    public ARoom(String roomNumber, String roomType, int numberOfBeds, boolean isOccupied, double roomPrice, double additionalFee) {
        super(roomNumber, roomType, numberOfBeds, isOccupied, roomPrice);
        this.additionalFee = additionalFee;
    }

    public ARoom(int roomId, String roomNumber, String roomType, int numberOfBeds, boolean isOccupied, double roomPrice, double additionalFee) {
        super(roomId, roomNumber, roomType, numberOfBeds, isOccupied, roomPrice);
        this.additionalFee = additionalFee;
    }

    public double getAdditionalFee() {
        return additionalFee;
    }

    public void setAdditionalFee(double additionalFee) {
        this.additionalFee = additionalFee;
    }

    @Override
    public double getRoomPrice() {
        return super.getRoomPrice() + additionalFee;
    }
}
