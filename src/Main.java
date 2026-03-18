import java.util.*;


class Service {
    private String name;
    private double cost;

    public Service(String name, double cost) {
        this.name = name;
        this.cost = cost;
    }

    public String getName() {
        return name;
    }

    public double getCost() {
        return cost;
    }
}

/**
 * Add-On Service Manager
 */
class AddOnServiceManager {

    // Map: Reservation ID -> List of Services
    private Map<String, List<Service>> reservationServices;

    public AddOnServiceManager() {
        reservationServices = new HashMap<>();
    }

    /**
     * Add service to a reservation
     */
    public void addService(String reservationId, Service service) {

        reservationServices
                .computeIfAbsent(reservationId, k -> new ArrayList<>())
                .add(service);

        System.out.println("Added Service: " + service.getName() +
                " to Reservation: " + reservationId);
    }

    /**
     * Display services for a reservation
     */
    public void displayServices(String reservationId) {

        System.out.println("\nServices for Reservation: " + reservationId);
        System.out.println("-----------------------------------");

        List<Service> services = reservationServices.get(reservationId);

        if (services == null || services.isEmpty()) {
            System.out.println("No add-on services selected.");
            return;
        }

        for (Service s : services) {
            System.out.println(s.getName() + " - " + s.getCost());
        }
    }

    /**
     * Calculate total cost of services
     */
    public double calculateTotalCost(String reservationId) {

        List<Service> services = reservationServices.get(reservationId);
        double total = 0.0;

        if (services != null) {
            for (Service s : services) {
                total += s.getCost();
            }
        }

        return total;
    }
}

/**
 * Main Class
 */
public class Main {

    public static void main(String[] args) {

        // Simulated reservation IDs (from Use Case 6)
        String reservation1 = "S1";
        String reservation2 = "D2";

        // Initialize service manager
        AddOnServiceManager manager = new AddOnServiceManager();

        // Guest selects add-on services
        manager.addService(reservation1, new Service("Breakfast", 200));
        manager.addService(reservation1, new Service("Airport Pickup", 500));

        manager.addService(reservation2, new Service("Extra Bed", 300));

        // Display services
        manager.displayServices(reservation1);
        manager.displayServices(reservation2);

        // Calculate total cost
        System.out.println("\nTotal Add-On Cost for " + reservation1 + ": "
                + manager.calculateTotalCost(reservation1));

        System.out.println("Total Add-On Cost for " + reservation2 + ": "
                + manager.calculateTotalCost(reservation2));

        System.out.println("\nNote: Booking and inventory remain unchanged.");
    }
}