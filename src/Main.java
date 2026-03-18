import java.util.*;

/**
 * Reservation Domain Model
 */
class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getReservationId() { return reservationId; }
    public String getGuestName() { return guestName; }
    public String getRoomType() { return roomType; }

    @Override
    public String toString() {
        return "ReservationID: " + reservationId + " | Guest: " + guestName + " | Room Type: " + roomType;
    }
}

/**
 * Thread-safe Inventory Service
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
     * Thread-safe allocation
     */
    public synchronized boolean allocateRoom(String roomType) {
        int available = inventory.getOrDefault(roomType, 0);
        if (available > 0) {
            inventory.put(roomType, available - 1);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Thread-safe rollback (cancellation)
     */
    public synchronized void releaseRoom(String roomType) {
        inventory.put(roomType, inventory.getOrDefault(roomType, 0) + 1);
    }

    public synchronized void displayInventory() {
        System.out.println("\nCurrent Inventory:");
        for (String type : inventory.keySet()) {
            System.out.println(type + ": " + inventory.get(type));
        }
    }
}

/**
 * Booking Service (Thread-Safe)
 */
class BookingService {

    private RoomInventory inventory;
    private Map<String, Reservation> confirmedBookings;  // Shared map

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
        confirmedBookings = Collections.synchronizedMap(new LinkedHashMap<>());
    }

    /**
     * Attempt booking (thread-safe)
     */
    public void bookRoom(Reservation reservation) {
        synchronized (inventory) { // critical section
            if (inventory.allocateRoom(reservation.getRoomType())) {
                confirmedBookings.put(reservation.getReservationId(), reservation);
                System.out.println(Thread.currentThread().getName() + " - Booking confirmed: " + reservation);
            } else {
                System.out.println(Thread.currentThread().getName() + " - Booking failed (no availability): " + reservation);
            }
        }
    }

    public void displayConfirmedBookings() {
        synchronized (confirmedBookings) {
            System.out.println("\nConfirmed Bookings:");
            for (Reservation r : confirmedBookings.values()) {
                System.out.println(r);
            }
        }
    }
}

/**
 * Main Class
 */
public class UseCase11ConcurrentBookingSimulation {

    public static void main(String[] args) throws InterruptedException {

        RoomInventory inventory = new RoomInventory();
        BookingService bookingService = new BookingService(inventory);

        // Simulated guest booking requests
        List<Reservation> requests = Arrays.asList(
                new Reservation("S1", "Alice", "Single"),
                new Reservation("S2", "Bob", "Single"),
                new Reservation("D1", "Charlie", "Double"),
                new Reservation("SU1", "Diana", "Suite"),
                new Reservation("S3", "Eve", "Single")  // May fail due to no availability
        );

        // Create threads for each booking request
        List<Thread> threads = new ArrayList<>();
        for (Reservation r : requests) {
            Thread t = new Thread(() -> bookingService.bookRoom(r));
            t.setName("Thread-" + r.getGuestName());
            threads.add(t);
        }

        // Start all threads concurrently
        for (Thread t : threads) {
            t.start();
        }

        // Wait for all threads to complete
        for (Thread t : threads) {
            t.join();
        }

        // Display final confirmed bookings and inventory
        bookingService.displayConfirmedBookings();
        inventory.displayInventory();
    }
}