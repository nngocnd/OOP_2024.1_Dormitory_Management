package Model;

public class Contract {
    private int contractId;
    private String startDate;
    private String endDate;
    private double roomPrice;
    private String paymentMethod;
    private Student student;
    private Room room;

    public Contract(int contractId) {
        this.contractId = contractId;
    }

    public Contract(int contractId, String startDate, String endDate, double roomPrice, String paymentMethod, Student student, Room room) {
        this.contractId = contractId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.roomPrice = roomPrice;
        this.paymentMethod = paymentMethod;
        this.student = student;
        this.room = room;
    }

    public Contract() {

    }

    // Getter and Setter methods
    public int getContractId() {
        return contractId;
    }

    public void setContractId(int contractId) {
        this.contractId = contractId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public double getRoomPrice() {
        return roomPrice;
    }

    public void setRoomPrice(double roomPrice) {
        this.roomPrice = roomPrice;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }
}
