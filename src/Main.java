import java.util.*;

/**
 * Custom exception for invalid booking attempts
 */
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

/**
 * Reservation class
 */
class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}

/**
 * Inventory Service with validation
 */
class RoomInventory {

    private Map<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single", 2);
        inventory.put("Double", 1);
        inventory.put("Suite", 1);
    }

    /**
     * Check if a room type is valid
     */
    public boolean isValidRoomType(String type) {
        return inventory.containsKey(type);
    }

    /**
     * Get availability (throws if room type invalid)
     */
    public int getAvailability(String type) throws InvalidBookingException {
        if (!isValidRoomType(type)) {
            throw new InvalidBookingException("Invalid room type: " + type);
        }
        return inventory.get(type);
    }

    /**
     * Reduce inventory safely
     */
    public void reduceAvailability(String type) throws InvalidBookingException {
        if (!isValidRoomType(type)) {
            throw new InvalidBookingException("Invalid room type: " + type);
        }

        int available = inventory.get(type);
        if (available <= 0) {
            throw new InvalidBookingException("No availability for room type: " + type);
        }

        inventory.put(type, available - 1);
    }

    /**
     * Display current inventory
     */
    public void displayInventory() {
        System.out.println("\nCurrent Inventory:");
        for (String type : inventory.keySet()) {
            System.out.println(type + ": " + inventory.get(type));
        }
    }
}

/**
 * Booking Service with validation
 */
class BookingService {

    private RoomInventory inventory;

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
    }

    /**
     * Attempt booking with fail-fast validation
     */
    public void bookRoom(Reservation reservation) {
        try {
            System.out.println("\nProcessing booking for: " + reservation.getGuestName());

            // Validate room type and availability
            inventory.getAvailability(reservation.getRoomType());

            // Allocate room safely
            inventory.reduceAvailability(reservation.getRoomType());

            System.out.println("Booking confirmed for " + reservation.getGuestName() +
                    " | Room Type: " + reservation.getRoomType());

        } catch (InvalidBookingException e) {
            System.out.println("Booking failed: " + e.getMessage());
        }
    }
}

/**
 * Main Class
 */
public class UseCase9ErrorHandlingValidation {

    public static void main(String[] args) {

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();

        // Initialize booking service
        BookingService bookingService = new BookingService(inventory);

        // Simulated bookings (including invalid / unavailable)
        List<Reservation> requests = Arrays.asList(
                new Reservation("Alice", "Single"),
                new Reservation("Bob", "Suite"),
                new Reservation("Charlie", "Double"),
                new Reservation("Diana", "Penthouse"),  // invalid type
                new Reservation("Eve", "Single"),
                new Reservation("Frank", "Single")       // no availability
        );

        // Process bookings
        for (Reservation r : requests) {
            bookingService.bookRoom(r);
        }

        // Display remaining inventory
        inventory.displayInventory();
    }
}