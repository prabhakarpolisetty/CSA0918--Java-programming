import java.time.LocalDateTime;
import java.util.*;

interface Chargeable {
    double chargingFee(double kWh);
}
interface Pass {
    boolean isValidAt(LocalDateTime time);
}
abstract class Vehicle {
    private final String regNo;
    protected Vehicle(String regNo) {
        if (regNo == null || regNo.isBlank())
            throw new IllegalArgumentException("Registration is required");
        this.regNo = regNo.trim().toUpperCase();
    }
    public String getRegNo() { return regNo; }
    protected abstract double ratePerHour();
    public double calculateFee(long minutes) {
        if (minutes < 0) throw new IllegalArgumentException("Negative duration");
        long hours = (minutes + 59) / 60;
        return hours * ratePerHour();
    }
    @Override public boolean equals(Object o) {
        return o instanceof Vehicle && regNo.equals(((Vehicle)o).regNo);
    }
    @Override public int hashCode() { return regNo.hashCode(); }
    @Override public String toString() { return getClass().getSimpleName()+"["+regNo+"]"; }
}
class TwoWheeler extends Vehicle {
    TwoWheeler(String regNo) { super(regNo); }
    @Override protected double ratePerHour() { return 10.0; }
}
class Car extends Vehicle {
    Car(String regNo) { super(regNo); }
    @Override protected double ratePerHour() { return 30.0; }
}
class ElectricCar extends Car implements Chargeable {
    ElectricCar(String regNo) { super(regNo); }
    @Override public double chargingFee(double kWh) {
        if (kWh < 0) throw new IllegalArgumentException("Negative energy");
        return kWh * 12.0;
    }
}
class EBike extends Vehicle {
    EBike(String regNo) { super(regNo); }
    @Override protected double ratePerHour() { return 0.0; }
    public double flatFee() { return 15.0; }
}
class DeliveryVan extends Vehicle {
    DeliveryVan(String regNo) { super(regNo); }
    @Override protected double ratePerHour() { return 30.0; }
}
class OneDayPass implements Pass {
    private final java.time.LocalDate date;
    OneDayPass(java.time.LocalDate date) { this.date = Objects.requireNonNull(date); }
    @Override public boolean isValidAt(LocalDateTime time) {
        return date.equals(time.toLocalDate());
    }
}
class ParkingTicket {
    private static long nextId = 1;
    private final long id;
    private final Vehicle vehicle;
    private final int floor;
    private final int slot;
    private final LocalDateTime entryTime;
    ParkingTicket(Vehicle vehicle, int floor, int slot) {
        this.id = nextId++;
        this.vehicle = Objects.requireNonNull(vehicle);
        this.floor = floor; this.slot = slot;
        this.entryTime = LocalDateTime.now();
    }
    public long getId() { return id; }
    public Vehicle getVehicle() { return vehicle; }
    public int getFloor() { return floor; }
    public int getSlot() { return slot; }
    public LocalDateTime getEntryTime() { return entryTime; }
}
class ParkingFacility {
    private final Vehicle[][] slots;
    private final Map<String, ParkingTicket> active = new HashMap<>();
    ParkingFacility(int[] capacities) {
        slots = new Vehicle[capacities.length][];
        for (int i=0; i<capacities.length; i++) {
            if (capacities[i] < 0) throw new IllegalArgumentException("Negative capacity");
            slots[i] = new Vehicle[capacities[i]];
        }
    }
    public ParkingTicket enter(Vehicle v) {
        Objects.requireNonNull(v);
        if (active.containsKey(v.getRegNo()))
            throw new IllegalArgumentException("Vehicle already parked");
        for (int f=0; f<slots.length; f++) {
            for (int s=0; s<slots[f].length; s++) {
                if (slots[f][s] == null) {
                    slots[f][s] = v;
                    ParkingTicket t = new ParkingTicket(v, f, s);
                    active.put(v.getRegNo(), t);
                    return t;
                }
            }
        }
        return null;
    }
    public ParkingTicket exit(String regNo) {
        ParkingTicket t = active.remove(regNo.trim().toUpperCase());
        if (t == null) return null;
        slots[t.getFloor()][t.getSlot()] = null;
        return t;
    }
    public int activeCount() { return active.size(); }
}
public class SmartParkingDemo {
    public static void main(String[] args) {
        ParkingFacility facility = new ParkingFacility(new int[]{2, 1});
        ParkingTicket t1 = facility.enter(new TwoWheeler("TN01A1"));
        ParkingTicket t2 = facility.enter(new ElectricCar("TN02B2"));
        System.out.println(t1.getFloor()+","+t1.getSlot());
        System.out.println(t2.getFloor()+","+t2.getSlot());
        System.out.println("Active="+facility.activeCount());
        facility.exit("TN01A1");
        System.out.println("Active="+facility.activeCount());
        Vehicle[] vehicles = {new TwoWheeler("TN03C3"), new Car("TN04D4"),
                              new ElectricCar("TN05E5")};
        for (Vehicle v : vehicles) {
            double fee = v.calculateFee(130);
            if (v instanceof Chargeable c) fee += c.chargingFee(10);
            System.out.println(v + " fee=Rs " + fee);
        }
        System.out.println("E-bike flat fee=Rs " + new EBike("TN06F6").flatFee());
    }
}
