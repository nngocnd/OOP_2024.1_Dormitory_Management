package Model;

public class Student {
    private int studentId;
    private String fullName;
    private String dateOfBirth;
    private String gender;
    private String Address;
    private String phoneNumber;
    private String email;
    private Room room;
    private Contract contract;

    public Student() {

    }

    public Student(int studentId) {
        this.studentId = studentId;
    }

    public Student(int studentId, String fullName, String dateOfBirth, String gender, String address, String phoneNumber, String email, Room room) {
        this.studentId = studentId;
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        Address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.room = room;
    }

    public Student(int studentId, String fullName, String dateOfBirth, String gender, String address, String phoneNumber, String email, Room room, Contract contract) {
        this.studentId = studentId;
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        Address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.room = room;
        this.contract = contract;
    }

    // Getter and Setter methods
    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        this.Address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Contract getContract() {
        return contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    @Override
    public String toString() {
        return "Student{" +
                "studentId=" + studentId +
                ", fullName='" + fullName + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", gender='" + gender + '\'' +
                ", Address='" + Address + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", room=" + room +
                ", contract=" + contract +
                '}';
    }
}
