public class Vehicle {

    private String vehicleNumber;
    private String model;
    private String color;
    private int capacity;

    public Vehicle(String vehicleNumber, String model,
                   String color, int capacity) {
        this.vehicleNumber = vehicleNumber;
        this.model = model;
        this.color = color;
        this.capacity = capacity;
    }

    public String getVehicleNumber() { return vehicleNumber; }
    public String getModel() { return model; }
    public String getColor() { return color; }
    public int getCapacity() { return capacity; }
}
